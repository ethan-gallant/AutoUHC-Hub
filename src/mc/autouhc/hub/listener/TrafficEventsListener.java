package mc.autouhc.hub.listener;

import mc.autouhc.hub.AutoUHCHub;
import mc.autouhc.hub.menu.Menu;
import mc.autouhc.hub.util.ConfigUtils;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class TrafficEventsListener implements Listener {

    final AutoUHCHub main;

    public TrafficEventsListener(AutoUHCHub instance) {
        this.main = instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evt) {
        final Player p = evt.getPlayer();

        // We need to wait a tick so we can modify the player's inventory.
        new BukkitRunnable() {

            public void run() {
                p.getInventory().clear();
                p.getInventory().setItem(4, ConfigUtils.nameItem(main.getSettings().getServerSelectorItem(), 
                   main.getMessages().color(main.getMessages().getRawMessage("serverSelectorItem"))));
            }

        }.runTaskLater(main, 1L);
    }
    
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent evt) {
        final Player p = evt.getPlayer();
        final Menu menu = main.getMenuEventsListener().getMenu(p.getUniqueId());
        
        if(menu != null) {
            main.getMenuEventsListener().removeMenu(p.getUniqueId());
        }
    }
}
