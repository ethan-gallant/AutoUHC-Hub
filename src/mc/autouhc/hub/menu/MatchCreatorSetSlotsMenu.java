package mc.autouhc.hub.menu;

import mc.autouhc.hub.AutoUHCHub;
import mc.autouhc.hub.creator.MatchCreator;
import mc.autouhc.hub.util.ConfigUtils;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MatchCreatorSetSlotsMenu extends MatchCreatorMenu {

    public MatchCreatorSetSlotsMenu(AutoUHCHub instance, Player player, MatchCreator matchCreator) {
        super(instance, generateTitle(instance.getMessages(), "setSlots"), player, matchCreator);
    }

    public void show() {
        super.show();

        final ItemStack decreaseItem = ConfigUtils.nameItem(settings.getBackPageItem(), 
                msgs.getColoredString("decreaseSlots"));
        ui.setItem(12, decreaseItem);

        final ItemStack infoItem = ConfigUtils.nameItem(settings.getTeamItem(), 
                msgs.color(String.format("&b&lSlots: %d", matchCreator.getSlots())));
        ui.setItem(13, infoItem);

        final ItemStack increaseItem = ConfigUtils.nameItem(settings.getNextPageItem(), 
                msgs.getColoredString("increaseSlots"));
        ui.setItem(14, increaseItem);

        final ItemStack headerItem = ConfigUtils.nameItem(settings.getSlotsItem(), 
                msgs.getColoredString("slots"));
        ui.setItem(4, headerItem);

        super.openIfNeeded();
    }

    @Override
    public void clickPerformed(InventoryClickEvent evt) {
        int clickedSlot = evt.getSlot();

        if(clickedSlot == 12) {
            matchCreator.setSlots(matchCreator.getSlots() - 1);
            show();
        }else if(clickedSlot == 14) {
            matchCreator.setSlots(matchCreator.getSlots() + 1);
            show();
        }else if(clickedSlot == 18) {
            MatchCreatorMenu menu = new MatchCreatorSelectScenariosMenu(main, viewer, matchCreator);
            menu.show();
            main.getMenuEventsListener().setMenu(viewer.getUniqueId(), menu);
        }else if(clickedSlot == 26) {
            MatchCreatorConfirmationMenu confirmationMenu = new MatchCreatorConfirmationMenu(main, viewer, matchCreator);
            confirmationMenu.show();
            main.getMenuEventsListener().setMenu(viewer.getUniqueId(), confirmationMenu);
        }
    }

}
