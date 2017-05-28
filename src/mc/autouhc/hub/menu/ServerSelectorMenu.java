package mc.autouhc.hub.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import mc.autouhc.hub.AutoUHCHub;
import mc.autouhc.hub.game.GameState;
import mc.autouhc.hub.jedis.UHCJedis;
import mc.autouhc.hub.jedis.UHCServerResponse;
import mc.autouhc.hub.util.ConfigUtils;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class ServerSelectorMenu extends Menu {

    private ArrayList<UHCServerResponse> availableServers;
    private HashMap<Integer, UHCServerResponse> indexToResponse;
    private boolean opened;

    public ServerSelectorMenu(AutoUHCHub instance, Player viewer) {
        super(instance, viewer, Bukkit.createInventory(null, 54, instance.getMessages().color(instance.getMessages().getRawMessage("serverSelectorMenuTitle"))));
        this.availableServers = new ArrayList<UHCServerResponse>();
        this.indexToResponse = new HashMap<Integer, UHCServerResponse>();
        this.opened = false;
    }

    @Override
    public void show() {
        ui.clear();
        indexToResponse.clear();

        // Let's fill up the inventory with the background item.
        final ItemStack bgItem = ConfigUtils.nameItem(main.getSettings().getServerSelectorBackgroundItem(), " ");
        final ItemStack joinableSrvItem = main.getSettings().getJoinableServerItem();
        final ItemStack inProgressSrvItem = main.getSettings().getInProgressServerItem();
        final ItemStack restartingSrvItem = main.getSettings().getRestartingServerItem();
        for(int i = 0; i < ui.getSize(); i++) {
            // The slots where servers are supposed to go shouldn't be covered.
            if(i >= 11 && i <= 15 || i >= 20 && i <= 24 || i >= 29 && i <= 33 || i >= 38 && i <= 42) continue;
            ui.setItem(i, bgItem);
        }

        // Let's set the servers that are available.
        availableServers = main.getSettings().getJedis().getUHCServers();
        int startIndex = 11;
        int index = startIndex;

        sortServerResponsesByStatus();
        for(UHCServerResponse response : availableServers) {
            int status = Integer.valueOf(response.getData().get(UHCJedis.GAME_STATE_KEY));
            ItemStack srvItem = joinableSrvItem.clone();
            ChatColor statusColor = ChatColor.GREEN;

            if(status >= GameState.PREPARING.toIndex() && status <= GameState.DEATHMATCH.toIndex()) {
                srvItem = inProgressSrvItem.clone();
                if(status >= GameState.GRACE_PERIOD.toIndex()) {
                    statusColor = ChatColor.LIGHT_PURPLE;
                }
            }else if(status > GameState.DEATHMATCH.toIndex()) {
                srvItem = restartingSrvItem.clone();
                statusColor = ChatColor.RED;
            }


            ArrayList<String> lore = new ArrayList<String>();
            lore.add(ChatColor.GOLD + "-------------");
            lore.add(ChatColor.translateAlternateColorCodes('&', String.format("&ePlayers: &b%s&e/&b%s", 
                    response.getData().get(UHCJedis.ONLINE_PLAYERS_KEY), response.getData().get(UHCJedis.MAX_PLAYERS_KEY))));

            String gameMode = response.getData().get(UHCJedis.GAME_MODE_KEY);
            if(gameMode.equals("team")) {
                lore.add(ChatColor.translateAlternateColorCodes('&', String.format("&eType: &dTeams (%s)",
                        response.getData().get(UHCJedis.TEAM_SIZE_KEY))));
            }else {
                lore.add(ChatColor.translateAlternateColorCodes('&', "&eType: %cFFA"));
            }

            lore.add(ChatColor.YELLOW + "Status: " + statusColor + getStateName(status));
            lore.add(ChatColor.YELLOW + "Scenarios");

            String[] scenarios = response.getData().get(UHCJedis.ACTIVE_SCENARIOS_KEY).split(",");
            for(String s : scenarios) {
                lore.add(ChatColor.translateAlternateColorCodes('&', s));
            }

            lore.add(ChatColor.GOLD + "-------------");
            srvItem = ConfigUtils.nameAndLoreItem(srvItem, ChatColor.GREEN + response.getName(), lore);
            ui.setItem(index, srvItem);
            indexToResponse.put(index, response);

            if(Arrays.asList(15, 24, 33, 42).contains(index + 1)) {
                index += 9;
            }else {
                index++;
            }
        }

        ui.setItem(49, ConfigUtils.nameItem(main.getSettings().getAutoJoinItem(), main.getMessages().color(main.getMessages().getRawMessage("autoJoin"))));

        if(!opened) {
            viewer.openInventory(ui);
            opened = true;
        }
    }

    private String getStateName(int stateIndex) {
        GameState state = GameState.fromIndex(stateIndex);
        String name = null;

        if(state != null) {
            // We want just the first letter of the state name to be capitalized.
            name = state.name().substring(0, 1) + state.name().substring(1).toLowerCase();
        }

        return name;
    }

    private void sortServerResponsesByStatus() {
        // Let's sort the server responses by status.
        Collections.sort(availableServers, new Comparator<UHCServerResponse>() {

            public int compare(UHCServerResponse r1, UHCServerResponse r2) {
                int firstStatus = Integer.valueOf(r1.getData().get(UHCJedis.GAME_STATE_KEY));
                int secondStatus = Integer.valueOf(r2.getData().get(UHCJedis.GAME_STATE_KEY));
                return firstStatus - secondStatus;
            }

        });
    }

    /**
     * Returns a list of public and joinable UHC servers.
     * @return ArrayList<UHCServerResponse>
     */

    private ArrayList<UHCServerResponse> getAvailableServers() {
        ArrayList<UHCServerResponse> responses = new ArrayList<UHCServerResponse>();

        for(UHCServerResponse resp : availableServers) {
            if(resp.getData().get(UHCJedis.GAME_PRIVACY_KEY).equals("public") 
                    && Integer.valueOf(resp.getData().get(UHCJedis.GAME_STATE_KEY)) < GameState.PREPARING.toIndex()) {
                responses.add(resp);
            }
        }

        return responses;
    }

    @Override
    public void closed() {

    }

    public void sendPlayerToServer(Player p, UHCServerResponse response) {
        p.closeInventory();
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(main.getMessages().getRawMessage("attemptingJoin"),
                response.getName())));
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(response.getName());
        p.sendPluginMessage(main, "BungeeCord", out.toByteArray());
    }

    @Override
    public void clickPerformed(InventoryClickEvent evt) {
        Player p = (Player) evt.getWhoClicked();
        int slot = evt.getSlot();
        UHCServerResponse serverResponse = indexToResponse.get(slot);

        if(serverResponse != null) {
            int state = Integer.valueOf(serverResponse.getData().get(UHCJedis.GAME_STATE_KEY));

            if(state < GameState.PREPARING.toIndex()) {
                sendPlayerToServer(p, serverResponse);
            }
        }else if(slot == 49) {
            ArrayList<UHCServerResponse> responses = getAvailableServers();
            serverResponse = responses.get(ThreadLocalRandom.current().nextInt(0, responses.size()));

            if(serverResponse == null) {
                p.sendMessage(main.getMessages().getMessage("noServersAvailable"));
            }else {
                sendPlayerToServer(p, serverResponse);
            }
        }
    }

}
