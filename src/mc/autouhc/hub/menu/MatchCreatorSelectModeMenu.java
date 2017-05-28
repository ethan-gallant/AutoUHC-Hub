package mc.autouhc.hub.menu;

import mc.autouhc.hub.AutoUHCHub;
import mc.autouhc.hub.creator.MatchCreator;
import mc.autouhc.hub.util.ConfigUtils;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MatchCreatorSelectModeMenu extends MatchCreatorMenu {

    public MatchCreatorSelectModeMenu(AutoUHCHub instance, Player player, MatchCreator matchCreator) {
        super(instance, generateTitle(instance.getMessages(), "selectMode"), player, matchCreator);
    }

    public void show() {
        super.show();

        final ItemStack soloItem = ConfigUtils.nameItem(settings.getSoloItem(), msgs.getColoredString("soloMode"));
        ui.setItem(12, soloItem);

        final ItemStack teamItem = ConfigUtils.nameItem(settings.getTeamItem(), msgs.getColoredString("teamMode"));
        ui.setItem(14, teamItem);

        super.openIfNeeded();
    }

    @Override
    public void clickPerformed(InventoryClickEvent evt) {
        int clickedSlot = evt.getSlot();

        if(clickedSlot == 12) {
            matchCreator.setTeamMode(false);
        }else if(clickedSlot == 14) {
            matchCreator.setTeamMode(true);
        }else if(clickedSlot == 26) {
            MatchCreatorMenu menu = new MatchCreatorSetTeamSizeMenu(main, viewer, matchCreator);
            if(!matchCreator.isTeamMode()) {
                menu = new MatchCreatorSetBorderSizeMenu(main, viewer, matchCreator);
            }

            menu.show();
            main.getMenuEventsListener().setMenu(viewer.getUniqueId(), menu);
        }
    }

}
