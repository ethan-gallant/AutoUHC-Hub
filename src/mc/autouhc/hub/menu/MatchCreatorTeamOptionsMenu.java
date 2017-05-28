package mc.autouhc.hub.menu;

import java.util.Arrays;
import java.util.List;

import mc.autouhc.hub.AutoUHCHub;
import mc.autouhc.hub.creator.MatchCreator;
import mc.autouhc.hub.util.ConfigUtils;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MatchCreatorTeamOptionsMenu extends MatchCreatorMenu {

    public MatchCreatorTeamOptionsMenu(AutoUHCHub instance, Player player, MatchCreator matchCreator) {
        super(instance, generateTitle(instance.getMessages(), "setTeamOptions"), player, matchCreator);
    }

    public void show() {
        super.show();

        final List<String> ffLore = Arrays.asList((matchCreator.allowsFriendlyFire()) ? msgs.getColoredString("turnOn") : msgs.getColoredString("turnOff"),
                msgs.getColoredString("clickToToggle"));
        final ItemStack ffItem = ConfigUtils.nameAndLoreItem(settings.getFriendlyFireItem(), 
                msgs.getColoredString("friendlyFire"), ffLore);
        ui.setItem(12, ffItem);

        final ItemStack headerItem = ConfigUtils.nameItem(settings.getTeamItem(), msgs.getColoredString("teamOptions"));
        ui.setItem(4, headerItem);

        final List<String> rndTeamsLore = Arrays.asList((matchCreator.allowsRandomTeams()) ? msgs.getColoredString("turnOn") : msgs.getColoredString("turnOff"),
                msgs.getColoredString("clickToToggle"));
        final ItemStack rndTeamsItem = ConfigUtils.nameAndLoreItem(settings.getRandomTeamsItem(), msgs.getColoredString("randomTeams"), rndTeamsLore);
        ui.setItem(14, rndTeamsItem);

        super.openIfNeeded();
    }

    @Override
    public void clickPerformed(InventoryClickEvent evt) {
        int clickedSlot = evt.getSlot();

        if(clickedSlot == 12) {
            matchCreator.setFriendlyFire(!matchCreator.allowsFriendlyFire());
            show();
        }else if(clickedSlot == 14) {
            matchCreator.setRandomTeams(!matchCreator.allowsRandomTeams());
            show();
        }else if(clickedSlot == 18) {
            MatchCreatorSetTeamSizeMenu setTeamSizeMenu = new MatchCreatorSetTeamSizeMenu(main, viewer, matchCreator);
            setTeamSizeMenu.show();
            main.getMenuEventsListener().setMenu(viewer.getUniqueId(), setTeamSizeMenu);
        }else if(clickedSlot == 26) {
            MatchCreatorSetBorderSizeMenu setBorderSizeMenu = new MatchCreatorSetBorderSizeMenu(main, viewer, matchCreator);
            setBorderSizeMenu.show();
            main.getMenuEventsListener().setMenu(viewer.getUniqueId(), setBorderSizeMenu);
        }
    }

}
