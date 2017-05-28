package mc.autouhc.hub.menu;

import mc.autouhc.hub.AutoUHCHub;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public abstract class Menu implements IMenu {

    protected final AutoUHCHub main;
    protected final Player viewer;
    protected final Inventory ui;

    public Menu(AutoUHCHub instance, Player Player, Inventory gui) {
        this.main = instance;
        this.viewer = Player;
        this.ui = gui;
    }

    public Inventory getUI() {
        return this.ui;
    }
}
