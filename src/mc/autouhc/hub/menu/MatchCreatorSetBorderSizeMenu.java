package mc.autouhc.hub.menu;

import java.util.Arrays;
import java.util.List;

import mc.autouhc.hub.AutoUHCHub;
import mc.autouhc.hub.creator.MatchCreator;
import mc.autouhc.hub.creator.MatchCreator.BorderSize;
import mc.autouhc.hub.util.ConfigUtils;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MatchCreatorSetBorderSizeMenu extends MatchCreatorMenu {

    public MatchCreatorSetBorderSizeMenu(AutoUHCHub instance, Player player, MatchCreator matchCreator) {
        super(instance, generateTitle(instance.getMessages(), "setBorderSize"), player, matchCreator);
    }

    public void show() {
        super.show();

        String borderSize = String.valueOf(matchCreator.getBorderSize().getSize());
        List<String> lore = Arrays.asList(ChatColor.GRAY + String.format("%sx%s", borderSize, borderSize));

        final ItemStack oneThousandItem = ConfigUtils.nameItem(settings.getNextPageItem(), 
                msgs.getColoredString(BorderSize.ONE_K.getMessageKey()));
        ui.setItem(12, oneThousandItem);

        final ItemStack fifteenHundredItem = ConfigUtils.nameItem(settings.getMiddleButtonItem(), 
                msgs.getColoredString(BorderSize.FIFTEEN_K.getMessageKey()));
        ui.setItem(13, fifteenHundredItem);

        final ItemStack twoThousandItem = ConfigUtils.nameItem(settings.getBackPageItem(), 
                msgs.getColoredString(BorderSize.TWO_K.getMessageKey()));
        ui.setItem(14, twoThousandItem);

        final ItemStack headerItem = ConfigUtils.nameAndLoreItem(settings.getBorderSizeItem(), 
                msgs.getColoredString("borderSize"), lore);
        ui.setItem(4, headerItem);

        super.openIfNeeded();
    }

    @Override
    public void clickPerformed(InventoryClickEvent evt) {
        int clickedSlot = evt.getSlot();

        if(clickedSlot == 12) {
            matchCreator.setBorderSize(BorderSize.ONE_K);
            show();
        }else if(clickedSlot == 13) {
            matchCreator.setBorderSize(BorderSize.FIFTEEN_K);
            show();
        }else if(clickedSlot == 14) {
            matchCreator.setBorderSize(BorderSize.TWO_K);
            show();
        }else if(clickedSlot == 18) {
            MatchCreatorMenu menu = new MatchCreatorTeamOptionsMenu(main, viewer, matchCreator);
            if(!matchCreator.isTeamMode()) {
                menu = new MatchCreatorSelectModeMenu(main, viewer, matchCreator);
            }

            menu.show();
            main.getMenuEventsListener().setMenu(viewer.getUniqueId(), menu);
        }else if(clickedSlot == 26) {
            MatchCreatorSetMatchDurationMenu setMatchDurationMenu = new MatchCreatorSetMatchDurationMenu(main, viewer, matchCreator);
            setMatchDurationMenu.show();
            main.getMenuEventsListener().setMenu(viewer.getUniqueId(), setMatchDurationMenu);
        }
    }

}
