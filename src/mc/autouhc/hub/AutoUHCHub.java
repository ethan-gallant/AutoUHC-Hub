package mc.autouhc.hub;

import mc.autouhc.hub.listener.MenuEventsListener;
import mc.autouhc.hub.listener.TrafficEventsListener;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.plugin.java.JavaPlugin;

public class AutoUHCHub extends JavaPlugin {

    private static Settings settings;
    private static Messages messages;
    private static MenuEventsListener menuListener;

    public void onEnable() {
        AutoUHCHub.settings = new Settings(this);
        AutoUHCHub.settings.load();
        AutoUHCHub.messages = new Messages(this);
        AutoUHCHub.menuListener = new MenuEventsListener(this);

        getServer().getPluginManager().registerEvents(new TrafficEventsListener(this), this);
        getServer().getPluginManager().registerEvents(AutoUHCHub.menuListener, this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    public void onDisable() {
        if(AutoUHCHub.settings != null && AutoUHCHub.settings.getJedis() != null) {
            AutoUHCHub.settings.getJedis().farewell();
        }
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
