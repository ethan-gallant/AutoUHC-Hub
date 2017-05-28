package mc.autouhc.hub.menu;

import java.util.Arrays;
import java.util.List;

import mc.autouhc.hub.AutoUHCHub;
import mc.autouhc.hub.creator.MatchCreator;
import mc.autouhc.hub.util.ConfigUtils;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MatchCreatorWhitelistMenu extends MatchCreatorMenu {

    public MatchCreatorWhitelistMenu(AutoUHCHub instance, Player player, MatchCreator matchCreator) {
        super(instance, generateTitle(instance.getMessages(), "toggleWhitelist"), player, matchCreator);
    }

    public void show() {
        super.show();

        List<String> lore = Arrays.asList((matchCreator.isWhitelisted()) ? msgs.getColoredString("turnOn") : msgs.getColoredString("turnOff"));

        final ItemStack offItem = ConfigUtils.nameItem(settings.getBackPageItem(), 
                msgs.getColoredString("turnOff"));
        ui.setItem(12, offItem);

        final ItemStack onItem = ConfigUtils.nameItem(settings.getNextPageItem(), 
                msgs.getColoredString("turnOn"));
        ui.setItem(14, onItem);

        final ItemStack headerItem = ConfigUtils.nameAndLoreItem(settings.getWhitelistItem(), 
                msgs.getColoredString("whitelist"), lore);
        ui.setItem(4, headerItem);

        super.openIfNeeded();
    }

    @Override
    public void clickPerformed(InventoryClickEvent evt) {
        int clickedSlot = evt.getSlot();

        if(clickedSlot == 12) {
            matchCreator.setWhitelisted(false);
            show();
        }else if(clickedSlot == 14) {
            matchCreator.setWhitelisted(true);
            show();
        }else if(clickedSlot == 18) {
            MatchCreatorMenu menu = new MatchCreatorSetMatchDurationMenu(main, viewer, matchCreator);
            menu.show();
            main.getMenuEventsListener().setMenu(viewer.getUniqueId(), menu);
        }else if(clickedSlot == 26) {
            MatchCreatorSelectScenariosMenu selScenariosMenu = new MatchCreatorSelectScenariosMenu(main, viewer, matchCreator);
            selScenariosMenu.show();
            main.getMenuEventsListener().setMenu(viewer.getUniqueId(), selScenariosMenu);
        }
    }

}
