package mc.autouhc.hub.menu;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import mc.autouhc.hub.AutoUHCHub;
import mc.autouhc.hub.creator.MatchCreator;
import mc.autouhc.hub.creator.ScenarioType;
import mc.autouhc.hub.util.ConfigUtils;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MatchCreatorSelectScenariosMenu extends MatchCreatorMenu {

    private int page;
    private int maxPages;
    private HashMap<Integer, ScenarioType> slotToScenario;

    public MatchCreatorSelectScenariosMenu(AutoUHCHub instance, Player player, MatchCreator matchCreator) {
        super(instance, generateTitle(instance.getMessages(), "selectScenarios"), player, matchCreator);
        this.page = 0;
        this.maxPages = (int) Math.ceil(ScenarioType.values().length / 7);
        this.slotToScenario = new HashMap<Integer, ScenarioType>();
    }

    public void show() {
        super.show();
        slotToScenario.clear();

        List<ScenarioType> scenarios = Arrays.asList(ScenarioType.values());
        int scenario = (page * 7);

        for(int i = 10; i <= 16; i++) {
            if(scenario >= scenarios.size()) break;

            ScenarioType s = scenarios.get(scenario);
            List<String> lore = Arrays.asList((matchCreator.hasScenario(s)) ? msgs.getColoredString("turnOn") : msgs.getColoredString("turnOff"),
                    msgs.getColoredString("clickToToggle"));
            ItemStack scenarioItem = ConfigUtils.nameAndLoreItem(settings.getNextPageItem(), 
                    ChatColor.AQUA + s.getName(), lore);
            slotToScenario.put(i, s);
            ui.setItem(i, scenarioItem);
            scenario++;
        }

        if(page != 0) {
            final ItemStack prevScenarioPage = ConfigUtils.nameItem(settings.getRestartingServerItem(), msgs.getColoredString("previousScenariosPage"));
            ui.setItem(9, prevScenarioPage);
        }

        if(page < maxPages) {
            final ItemStack nextScenarioPage = ConfigUtils.nameItem(settings.getJoinableServerItem(), msgs.getColoredString("nextScenariosPage"));
            ui.setItem(17, nextScenarioPage);
        }

        super.openIfNeeded();
    }

    @Override
    public void clickPerformed(InventoryClickEvent evt) {
        int clickedSlot = evt.getSlot();

        if(clickedSlot == 9 && page > 0) {
            page--;
            evt.setCurrentItem(null);
            show();
        }else if(clickedSlot == 17 && page < maxPages) {
            page++;
            evt.setCurrentItem(null);
            show();
        }else if(clickedSlot == 18) {
            MatchCreatorWhitelistMenu whitelistMenu = new MatchCreatorWhitelistMenu(main, viewer, matchCreator);
            whitelistMenu.show();
            main.getMenuEventsListener().setMenu(viewer.getUniqueId(), whitelistMenu);
        }else if(clickedSlot == 26) {
            MatchCreatorSetSlotsMenu setSlotsMenu = new MatchCreatorSetSlotsMenu(main, viewer, matchCreator);
            setSlotsMenu.show();
            main.getMenuEventsListener().setMenu(viewer.getUniqueId(), setSlotsMenu);
        }else if(slotToScenario.get(clickedSlot) != null) {
            ScenarioType type = slotToScenario.get(clickedSlot);

            if(matchCreator.hasScenario(type)) {
                matchCreator.removeScenario(type);
            }else {
                matchCreator.addScenario(type);
            }

            show();
        }
    }

}
