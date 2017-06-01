package mc.autouhc.hub.listener;

import java.util.HashMap;
import java.util.UUID;

import mc.autouhc.hub.AutoUHCHub;
import mc.autouhc.hub.menu.Menu;
import mc.autouhc.hub.menu.ServerSelectorMenu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MenuEventsListener implements Listener {

    final AutoUHCHub main;
    private final HashMap<UUID, Menu> activeMenus;

    public MenuEventsListener(AutoUHCHub instance) {
        this.main = instance;
        this.activeMenus = new HashMap<UUID, Menu>();
    }

    public void setMenu(UUID uuid, Menu menu) {
        activeMenus.put(uuid, menu);
    }

    public boolean removeMenu(UUID uuid) {
        boolean removed = activeMenus.containsKey(uuid);
        activeMenus.remove(uuid);
        return removed;
    }

    public Menu getMenu(UUID uuid) {
        return activeMenus.get(uuid);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent evt) {
        Player p = evt.getPlayer();
        Action action = evt.getAction();

        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = p.getItemInHand();
            ItemStack check = new ItemStack(item.getType(), 1);
            if(check.isSimilar(main.getSettings().getServerSelectorItem())) {
                ServerSelectorMenu srvSel = new ServerSelectorMenu(main, p);
                srvSel.show();
                main.getMenuEventsListener().setMenu(p.getUniqueId(), srvSel);
            }else if(check.isSimilar(main.getSettings().getMatchCreatorItem())) {
                main.beginMatchCreator(p);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent evt) {
        Player p = (Player) evt.getPlayer();
        Menu menu = getMenu(p.getUniqueId());
        if(menu != null) {
            menu.closed();
            activeMenus.remove(p.getUniqueId());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent evt) {
        Player p = (Player) evt.getWhoClicked();
        Menu menu = getMenu(p.getUniqueId());

        if(menu != null && !evt.isCancelled()) {
            menu.clickPerformed(evt);
            evt.setCancelled(true);
        }
    }

}
