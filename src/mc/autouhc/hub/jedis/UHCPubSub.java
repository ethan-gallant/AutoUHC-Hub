package mc.autouhc.hub.jedis;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.scheduler.BukkitRunnable;

import mc.autouhc.hub.AutoUHCHub;
import redis.clients.jedis.JedisPubSub;

public class UHCPubSub extends JedisPubSub {

    final AutoUHCHub main;

    public UHCPubSub(AutoUHCHub instance) {
        this.main = instance;
    }

    public void onMessage(String channel, String message) {
        if(channel.equalsIgnoreCase("uhc")) {
            String[] split = message.split(" ");
            final String srvName = split[0];
            final int status = Integer.valueOf(split[1]);

            new BukkitRunnable() {

                public void run() {
                    main.sendConsoleMessage(ChatColor.GREEN + String.format("%s sent a status update.", srvName));
                    main.sendConsoleMessage(String.valueOf(status));
                }

            }.runTask(main);
        }
    }
}
