package mc.autouhc.hub;

import java.io.File;

import mc.autouhc.hub.jedis.UHCJedis;
import mc.autouhc.hub.util.ConfigUtils;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class Settings {

    final AutoUHCHub main;
    private final File configFile;
    private final YamlConfiguration config;
    private UHCJedis jedis;

    // Items
    private ItemStack srvSelItem;
    private ItemStack matchCreatorItem;
    private ItemStack selBgItem;
    private ItemStack joinableSrv;
    private ItemStack inProgressSrv;
    private ItemStack restartingSrv;
    private ItemStack autoJoinItem;

    // Items relating to the match creator
    private ItemStack creatorBg;
    private ItemStack nextBtn;
    private ItemStack backBtn;
    private ItemStack soloModeItem;
    private ItemStack teamModeItem;
    private ItemStack friendlyFireItem;
    private ItemStack randomTeamsItem;
    private ItemStack middleBtnItem;
    private ItemStack borderSizeItem;
    private ItemStack matchDurationItem;
    private ItemStack whitelistItem;
    private ItemStack slotsItem;

    private String serverName;
    private boolean wantPrefixMsgs;

    public Settings(AutoUHCHub instance) {
        this.main = instance;
        this.configFile = new File(main.getDataFolder() + "/config.yml");
        this.srvSelItem = null;
        this.matchCreatorItem = null;
        this.selBgItem = null;
        this.joinableSrv = null;
        this.inProgressSrv = null;
        this.restartingSrv = null;
        this.autoJoinItem = null;
        this.creatorBg = null;
        this.nextBtn = null;
        this.backBtn = null;
        this.soloModeItem = null;
        this.teamModeItem = null;
        this.friendlyFireItem = null;
        this.randomTeamsItem = null;
        this.middleBtnItem = null;
        this.borderSizeItem = null;
        this.matchDurationItem = null;
        this.whitelistItem = null;
        this.slotsItem = null;
        this.serverName = null;
        this.wantPrefixMsgs = false;

        boolean configAvailable = configFile.exists();
        if(!configAvailable) {
            main.saveDefaultConfig();
        }

        this.config = YamlConfiguration.loadConfiguration(configFile);
        this.jedis = null;
    }

    @SuppressWarnings("deprecation")
    private boolean configUpToDate() {
        String version = config.getString("config-version");
        YamlConfiguration jarConfig = YamlConfiguration.loadConfiguration(main.getResource("config.yml"));
        String jarCfgVersion = jarConfig.getString("config-version");

        if(!jarCfgVersion.equalsIgnoreCase(version)) {
            configFile.renameTo(new File(main.getDataFolder() + "/config-old.yml"));

            main.saveDefaultConfig();
            main.sendConsoleMessage(ChatColor.YELLOW + "Your config is out-of-date, "
                    + "a new one has been saved and your old one has been renamed. Please consider your old settings and restart the plugin.");
            main.disable();

            return false;
        }

        main.sendConsoleMessage(ChatColor.DARK_GREEN + String.format("Config version (%s) is up-to-date.", version));
        return true;
    }

    public void load() {
        if(configUpToDate()) {
            serverName = config.getString("server-name");
            wantPrefixMsgs = config.getBoolean("want-prefix-messages");

            // Handling items
            ConfigurationSection itemsSect = config.getConfigurationSection("items");
            srvSelItem = ConfigUtils.handleIconString(itemsSect.getString("serverSelector"));
            matchCreatorItem = ConfigUtils.handleIconString(itemsSect.getString("matchCreator"));
            restartingSrv = ConfigUtils.handleIconString(itemsSect.getString("restartingServer"));
            joinableSrv = ConfigUtils.handleIconString(itemsSect.getString("joinableServer"));
            inProgressSrv = ConfigUtils.handleIconString(itemsSect.getString("inProgressServer"));
            selBgItem = ConfigUtils.handleIconString(itemsSect.getString("selectorBackground"));
            autoJoinItem = ConfigUtils.handleIconString(itemsSect.getString("autoJoin"));

            // Handling match creator items
            creatorBg = ConfigUtils.handleIconString(itemsSect.getString("creatorBackground"));
            nextBtn = ConfigUtils.handleIconString(itemsSect.getString("nextPageButton"));
            backBtn = ConfigUtils.handleIconString(itemsSect.getString("previousPageButton"));
            soloModeItem = ConfigUtils.handleIconString(itemsSect.getString("soloMode"));
            teamModeItem = ConfigUtils.handleIconString(itemsSect.getString("teamMode"));
            friendlyFireItem = ConfigUtils.handleIconString(itemsSect.getString("friendlyFireButton"));
            randomTeamsItem = ConfigUtils.handleIconString(itemsSect.getString("randomTeamsButton"));
            middleBtnItem = ConfigUtils.handleIconString(itemsSect.getString("middleButton"));
            borderSizeItem = ConfigUtils.handleIconString(itemsSect.getString("borderSize"));
            matchDurationItem = ConfigUtils.handleIconString(itemsSect.getString("matchTime"));
            whitelistItem = ConfigUtils.handleIconString(itemsSect.getString("whitelist"));
            slotsItem = ConfigUtils.handleIconString(itemsSect.getString("slots"));

            // Handling Jedis
            ConfigurationSection jedisSect = config.getConfigurationSection("jedis");
            String jedisHost = jedisSect.getString("host");
            String password = jedisSect.getString("password");
            int port = jedisSect.getInt("port");

            jedis = new UHCJedis(main);
            jedis.connect(jedisHost, port, password);
            jedis.subscribeToUHCChannel();
            jedis.handshake();
        }
    }

    public String getServerName() {
        return this.serverName;
    }

    public boolean wantPrefixMessages() {
        return this.wantPrefixMsgs;
    }

    public ConfigurationSection getMessagesSection() {
        if(config != null) {
            return config.getConfigurationSection("messages");
        }

        return null;
    }

    public ItemStack getServerSelectorItem() {
        if(srvSelItem != null) {
            return srvSelItem.clone();
        }

        return srvSelItem;
    }
    
    public ItemStack getMatchCreatorItem() {
        if(matchCreatorItem != null) {
            return matchCreatorItem.clone();
        }
        
        return matchCreatorItem;
    }

    public ItemStack getServerSelectorBackgroundItem() {
        if(selBgItem != null) {
            return selBgItem.clone();
        }

        return selBgItem;
    }

    public ItemStack getJoinableServerItem() {
        if(joinableSrv != null) {
            return joinableSrv.clone();
        }

        return joinableSrv;
    }

    public ItemStack getInProgressServerItem() {
        if(inProgressSrv != null) {
            return inProgressSrv.clone();
        }

        return inProgressSrv;
    }

    public ItemStack getRestartingServerItem() {
        if(restartingSrv != null) {
            return restartingSrv.clone();
        }

        return restartingSrv;
    }

    public ItemStack getAutoJoinItem() {
        if(autoJoinItem != null) {
            return autoJoinItem.clone();
        }

        return autoJoinItem;
    }

    public ItemStack getCreatorBackgroundItem() {
        if(creatorBg != null) {
            return creatorBg.clone();
        }

        return creatorBg;
    }

    public ItemStack getNextPageItem() {
        if(nextBtn != null) {
            return nextBtn.clone();
        }

        return nextBtn;
    }

    public ItemStack getBackPageItem() {
        if(backBtn != null) {
            return backBtn.clone();
        }

        return backBtn;
    }

    public ItemStack getSoloItem() {
        if(soloModeItem != null) {
            return soloModeItem.clone();
        }

        return soloModeItem;
    }

    public ItemStack getTeamItem() {
        if(teamModeItem != null) {
            return teamModeItem.clone();
        }

        return teamModeItem;
    }

    public ItemStack getFriendlyFireItem() {
        if(friendlyFireItem != null) {
            return friendlyFireItem.clone();
        }

        return friendlyFireItem;
    }

    public ItemStack getRandomTeamsItem() {
        if(randomTeamsItem != null) {
            return randomTeamsItem.clone();
        }

        return randomTeamsItem;
    }

    public ItemStack getMiddleButtonItem() {
        if(middleBtnItem != null) {
            return middleBtnItem.clone();
        }

        return middleBtnItem;
    }

    public ItemStack getBorderSizeItem() {
        if(borderSizeItem != null) {
            return borderSizeItem.clone();
        }

        return borderSizeItem;
    }

    public ItemStack getMatchDurationItem() {
        if(matchDurationItem != null) {
            return matchDurationItem.clone();
        }

        return matchDurationItem;
    }

    public ItemStack getWhitelistItem() {
        if(whitelistItem != null) {
            return whitelistItem.clone();
        }

        return whitelistItem;
    }

    public ItemStack getSlotsItem() {
        if(slotsItem != null) {
            return slotsItem.clone();
        }

        return slotsItem;
    }

    public UHCJedis getJedis() {
        return this.jedis;
    }
}
