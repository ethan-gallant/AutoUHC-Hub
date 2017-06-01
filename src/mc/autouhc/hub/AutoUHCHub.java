package mc.autouhc.hub;

import java.util.HashMap;
import java.util.UUID;

import mc.autouhc.hub.command.CommandPool;
import mc.autouhc.hub.creator.MatchCreator;
import mc.autouhc.hub.listener.MenuEventsListener;
import mc.autouhc.hub.listener.TrafficEventsListener;
import mc.autouhc.hub.menu.MatchCreatorSelectModeMenu;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoUHCHub extends JavaPlugin {

    private static Settings settings;
    private static Messages messages;
    private static MenuEventsListener menuListener;
    private static CommandPool cmdPool;
    private static HashMap<UUID, MatchCreator> matchCreators;

    static {
        matchCreators = new HashMap<UUID, MatchCreator>();
    }

    public void onEnable() {
        AutoUHCHub.settings = new Settings(this);
        AutoUHCHub.settings.load();
        AutoUHCHub.messages = new Messages(this);
        AutoUHCHub.menuListener = new MenuEventsListener(this);
        AutoUHCHub.cmdPool = new CommandPool(this);

        getServer().getPluginManager().registerEvents(new TrafficEventsListener(this), this);
        getServer().getPluginManager().registerEvents(AutoUHCHub.menuListener, this);
        getServer().getPluginManager().registerEvents(cmdPool, this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    public void onDisable() {
        if(AutoUHCHub.settings != null && AutoUHCHub.settings.getJedis() != null) {
            AutoUHCHub.settings.getJedis().farewell();
        }
    }

    /**
     * Creates and assigns a {@link mc.autouhc.hub.creator.MatchCreator}
     * to the assigned {@link org.bukkit.entity.Player}'s UUID.
     * @param Player player
     */

    public void beginMatchCreator(Player player) {
        UUID uuid = player.getUniqueId();
        if(getMatchCreator(uuid) == null) {
            MatchCreator matchCreator = new MatchCreator(uuid);
            MatchCreatorSelectModeMenu selModeMenu = new MatchCreatorSelectModeMenu(this, player, matchCreator);
            selModeMenu.show();

            menuListener.setMenu(uuid, selModeMenu);
            matchCreators.put(uuid, matchCreator);
            sendConsoleMessage("OPENED");
        }else {
            sendConsoleMessage("HMMM");
        }
    }

    /**
     * Removes the {@link mc.autouhc.hub.creator.MatchCreator} object assigned
     * to the specified {@link org.bukkit.entity.Player}'s UUID. (if it exists)
     * @param Player player
     * @return true/false if a MatchCreator was removed.
     */

    public boolean removeMatchCreator(Player player) {
        UUID uuid = player.getUniqueId();
        MatchCreator matchCreator = matchCreators.remove(uuid);
        return (matchCreator != null);
    }

    /**
     * Obtains the {@link mc.autouhc.hub.creator.MatchCreator} assigned to
     * the specified UUID.
     * @param UUID uuid
     * @return {@link mc.autouhc.hub.creator.MatchCreator} object or null
     */

    public MatchCreator getMatchCreator(UUID uuid) {
        return matchCreators.get(uuid);
    }


    public void sendConsoleMessage(String msg) {
        String pluginName = getDescription().getName();
        String prefix = ChatColor.DARK_GRAY +  "[" + ChatColor.GOLD + pluginName + ChatColor.DARK_GRAY + "]";
        getServer().getConsoleSender().sendMessage(prefix + " " + msg);
    }

    public void disable() {
        setEnabled(false);
    }

    public Settings getSettings() {
        return AutoUHCHub.settings;
    }

    public Messages getMessages() {
        return AutoUHCHub.messages;
    }

    public MenuEventsListener getMenuEventsListener() {
        return AutoUHCHub.menuListener;
    }
}
