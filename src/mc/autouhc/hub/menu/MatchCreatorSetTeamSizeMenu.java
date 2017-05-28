package mc.autouhc.hub.menu;

import java.util.Arrays;
import java.util.List;

import mc.autouhc.hub.AutoUHCHub;
import mc.autouhc.hub.creator.MatchCreator;
import mc.autouhc.hub.util.ConfigUtils;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MatchCreatorSetTeamSizeMenu extends MatchCreatorMenu {

    public MatchCreatorSetTeamSizeMenu(AutoUHCHub instance, Player player, MatchCreator matchCreator) {
        super(instance, generateTitle(instance.getMessages(), "setTeamSize"), player, matchCreator);
    }

    public void show() {
        super.show();

        List<String> lore = Arrays.asList(ChatColor.GRAY + String.valueOf(matchCreator.getTeamSize()));

        final ItemStack teamItem = ConfigUtils.nameAndLoreItem(settings.getTeamItem(), msgs.getColoredString("teamSize"), lore);
        ui.setItem(13, teamItem);

        final ItemStack increaseItem = ConfigUtils.nameItem(settings.getNextPageItem(), msgs.getColoredString("increaseSlots"));
        ui.setItem(4, increaseItem);

        final ItemStack decreaseItem = ConfigUtils.nameItem(settings.getBackPageItem(), msgs.getColoredString("decreaseSlots"));
        ui.setItem(22, decreaseItem);

        super.openIfNeeded();
    }

    @Override
    public void clickPerformed(InventoryClickEvent evt) {
        int clickedSlot = evt.getSlot();

        if(clickedSlot == 4) {
            matchCreator.setTeamSize(matchCreator.getTeamSize() + 1);
            show();
        }else if(clickedSlot == 22) {
            matchCreator.setTeamSize(matchCreator.getTeamSize() - 1);
            show();
        }else if(clickedSlot == 18) {
            MatchCreatorSelectModeMenu selModeMenu = new MatchCreatorSelectModeMenu(main, viewer, matchCreator);
            selModeMenu.show();
            main.getMenuEventsListener().setMenu(viewer.getUniqueId(), selModeMenu);
        }else if(clickedSlot == 26) {
            MatchCreatorTeamOptionsMenu teamOptionsMenu = new MatchCreatorTeamOptionsMenu(main, viewer, matchCreator);
            teamOptionsMenu.show();
            main.getMenuEventsListener().setMenu(viewer.getUniqueId(), teamOptionsMenu);
        }
    }

}
