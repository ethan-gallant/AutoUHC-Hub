package mc.autouhc.hub.menu;

import java.util.Arrays;
import java.util.List;

import mc.autouhc.hub.AutoUHCHub;
import mc.autouhc.hub.creator.MatchCreator;
import mc.autouhc.hub.creator.MatchCreator.MatchDuration;
import mc.autouhc.hub.util.ConfigUtils;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MatchCreatorSetMatchDurationMenu extends MatchCreatorMenu {

    public MatchCreatorSetMatchDurationMenu(AutoUHCHub instance, Player player, MatchCreator matchCreator) {
        super(instance, generateTitle(instance.getMessages(), "setMatchDuration"), player, matchCreator);
    }

    public void show() {
        super.show();

        String minutes = String.valueOf(matchCreator.getMatchDuration().getMinutes());
        List<String> lore = Arrays.asList(ChatColor.GRAY + String.format("%s Min", minutes));

        final ItemStack fortyMinsItem = ConfigUtils.nameItem(settings.getNextPageItem(), 
                msgs.getColoredString(MatchDuration.FORTY_MINUTES.getMessageKey()));
        ui.setItem(12, fortyMinsItem);

        final ItemStack fiftyMinsItem = ConfigUtils.nameItem(settings.getMiddleButtonItem(), 
                msgs.getColoredString(MatchDuration.FIFTY_MINUTES.getMessageKey()));
        ui.setItem(13, fiftyMinsItem);

        final ItemStack sixtyMinsItem = ConfigUtils.nameItem(settings.getBackPageItem(), 
                msgs.getColoredString(MatchDuration.SIXTY_MINUTES.getMessageKey()));
        ui.setItem(14, sixtyMinsItem);

        final ItemStack headerItem = ConfigUtils.nameAndLoreItem(settings.getMatchDurationItem(), 
                msgs.getColoredString("gameTime"), lore);
        ui.setItem(4, headerItem);

        super.openIfNeeded();
    }

    @Override
    public void clickPerformed(InventoryClickEvent evt) {
        int clickedSlot = evt.getSlot();

        if(clickedSlot == 12) {
            matchCreator.setMatchDuration(MatchDuration.FORTY_MINUTES);
            show();
        }else if(clickedSlot == 13) {
            matchCreator.setMatchDuration(MatchDuration.FIFTY_MINUTES);
            show();
        }else if(clickedSlot == 14) {
            matchCreator.setMatchDuration(MatchDuration.SIXTY_MINUTES);
            show();
        }else if(clickedSlot == 18) {
            MatchCreatorMenu menu = new MatchCreatorSetBorderSizeMenu(main, viewer, matchCreator);
            menu.show();
            main.getMenuEventsListener().setMenu(viewer.getUniqueId(), menu);
        }else if(clickedSlot == 26) {
            MatchCreatorWhitelistMenu whitelistMenu = new MatchCreatorWhitelistMenu(main, viewer, matchCreator);
            whitelistMenu.show();
            main.getMenuEventsListener().setMenu(viewer.getUniqueId(), whitelistMenu);
        }
    }

}
