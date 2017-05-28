package mc.autouhc.hub.menu;

import mc.autouhc.hub.AutoUHCHub;
import mc.autouhc.hub.Messages;
import mc.autouhc.hub.Settings;
import mc.autouhc.hub.creator.MatchCreator;
import mc.autouhc.hub.util.ConfigUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class MatchCreatorMenu extends Menu {

    protected final Messages msgs;
    protected final Settings settings;
    protected final MatchCreator matchCreator;
    protected boolean opened;

    public MatchCreatorMenu(AutoUHCHub instance, String invTitle, Player player, MatchCreator matchCreator) {
        super(instance, player, Bukkit.createInventory(null, 27, invTitle));
        this.msgs = instance.getMessages();
        this.settings = instance.getSettings();
        this.matchCreator = matchCreator;
        this.opened = false;
    }

    protected static String generateTitle(Messages msgs, String title) {
        return msgs.color(String.format(msgs.getRawMessage("matchCreatorMenuTitle"), msgs.getRawMessage(title)));
    }

    protected void openIfNeeded() {
        if(!opened) {
            viewer.openInventory(ui);
            opened = true;
        }
    }

    public void closed() {  }

    public void show() {
        ui.clear();

        // Let's setup the background.
        for(int i = 0; i < ui.getSize(); i++) {
            final ItemStack bgItem = ConfigUtils.nameItem(main.getSettings().getCreatorBackgroundItem(), " ");
            ui.setItem(i, bgItem);
        }

        // Let's add the next and back buttons
        final ItemStack backBtn = ConfigUtils.nameItem(main.getSettings().getBackPageItem(), msgs.color(msgs.getRawMessage("back")));
        ui.setItem(18, backBtn);

        final ItemStack nextBtn = ConfigUtils.nameItem(main.getSettings().getNextPageItem(), msgs.color(msgs.getRawMessage("next")));
        ui.setItem(26, nextBtn);
    }

    public MatchCreator getMatchCreator() {
        return this.matchCreator;
    }

}
