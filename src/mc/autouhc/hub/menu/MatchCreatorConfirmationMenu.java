package mc.autouhc.hub.menu;

import java.util.Arrays;

import mc.autouhc.hub.AutoUHCHub;
import mc.autouhc.hub.creator.MatchCreator;
import mc.autouhc.hub.util.ConfigUtils;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MatchCreatorConfirmationMenu extends MatchCreatorMenu {

    public MatchCreatorConfirmationMenu(AutoUHCHub instance, Player player, MatchCreator matchCreator) {
        super(instance, generateTitle(instance.getMessages(), "confirmation"), player, matchCreator);
    }

    public void show() {
        super.show();
        int currentSlot = 0;

        // Let's create the deny items
        for(int i = 0; i < 12; i++) {
            ItemStack denyItem = ConfigUtils.nameItem(settings.getBackPageItem(), msgs.getColoredString("deny"));
            ui.setItem(currentSlot, denyItem);

            if(Arrays.asList(4, 13).contains(currentSlot + 1)) {
                currentSlot += 6;
            }else {
                currentSlot++;
            }
        }

        currentSlot = 5;

        // Let's create the confirm items
        for(int i = 0; i < 12; i++) {
            ItemStack confirmItem = ConfigUtils.nameItem(settings.getNextPageItem(), msgs.getColoredString("confirm"));
            ui.setItem(currentSlot, confirmItem);

            if(Arrays.asList(9, 18).contains(currentSlot + 1)) {
                currentSlot += 6;
            }else {
                currentSlot++;
            }
        }

        super.openIfNeeded();
    }

    @Override
    public void clickPerformed(InventoryClickEvent evt) {
        int clickedSlot = evt.getSlot();

        if(clickedSlot < 4 || (9 <= clickedSlot && clickedSlot < 13) || (18 <= clickedSlot && clickedSlot < 23)) {
            viewer.sendMessage(msgs.getMessage("matchCreationCanceled"));
            viewer.closeInventory();
        }else if((5 <= clickedSlot && clickedSlot <= 8) || (14 <= clickedSlot && clickedSlot <= 17) || (23 <= clickedSlot && clickedSlot <= 26)) {
            viewer.sendMessage(msgs.getMessage("matchCreationComplete"));
            viewer.closeInventory();
        }
    }

}
