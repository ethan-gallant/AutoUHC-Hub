package mc.autouhc.hub.command;

import mc.autouhc.hub.AutoUHCHub;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateMatchCommand extends SubCommandBase {

    final AutoUHCHub main;

    public CreateMatchCommand(AutoUHCHub instance, UHCBaseCommand baseCmd) {
        super("creatematch", baseCmd);
        this.main = instance;

        this.addAlias("cm");
        this.setDescription(ChatColor.YELLOW + "Opens the Match Creator so you can create a match.");
        this.addRequirement(new PlayerRequirement());
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        // This is a safe casting because of our {@link mc.autohub.hub.PlayerRequirement}.
        main.beginMatchCreator((Player) sender);
    }

}
