package mc.autouhc.hub.command;

import mc.autouhc.hub.AutoUHCHub;

import org.bukkit.command.CommandSender;

public class UHCBaseCommand extends CommandBase {

    final AutoUHCHub main;

    public UHCBaseCommand(AutoUHCHub instance) {
        super("ultrahardcore");
        this.main = instance;

        this.addAlias("uhc");
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        parseUsage();
        sender.sendMessage(getUsage());
    }

}
