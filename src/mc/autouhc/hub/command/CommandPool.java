package mc.autouhc.hub.command;

import java.util.Arrays;
import java.util.HashSet;

import mc.autouhc.hub.AutoUHCHub;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class CommandPool implements Listener {

    final AutoUHCHub main;
    private final UHCBaseCommand uhcBaseCmd;
    private final CreateMatchCommand cmCmd;
    private static HashSet<CommandBase> commands;

    public CommandPool(final AutoUHCHub instance) {
        this.main = instance;
        CommandPool.commands = new HashSet<CommandBase>();

        // Let's instantiate our commands.
        this.uhcBaseCmd = new UHCBaseCommand(instance);
        this.cmCmd = new CreateMatchCommand(instance, uhcBaseCmd);

        // Let's add our UHC base command.
        commands.add(uhcBaseCmd);

        // Let's add our create match command.
        uhcBaseCmd.addSubCommand(cmCmd);
    }

    @EventHandler
    public void onPlayerExecuteCommand(final PlayerCommandPreprocessEvent evt) {
        final Player player = evt.getPlayer();
        handleCommand(player, evt.getMessage());
    }

    private boolean handleCommand(final CommandSender sender, String msg) {
        String cmd = msg;
        String[] args = {};

        if(msg.contains(" ") && !msg.endsWith(" ")) {
            final String[] elements = msg.split(" ");
            cmd = elements[0].substring(1, elements[0].length());
            args = Arrays.copyOfRange(elements, 1, elements.length);
        }else {
            cmd = cmd.substring(1, cmd.length());
        }

        for(final CommandBase command : commands) {
            if(command.getCommand().equalsIgnoreCase(cmd) || command.isAlias(cmd)) {
                if(command.canExecute(sender, args)) {
                    if(!command.handleSubCommand(sender, args)) {
                        command.run(sender, args);
                    }

                    return true;
                }else {
                    return false;
                }
            }
        }

        return false;
    }

    @EventHandler
    public void onServerExecuteCommand(final ServerCommandEvent evt) {
        final CommandSender sender = evt.getSender();
        handleCommand(sender, evt.getCommand());
    }

    public static void addCommand(final CommandBase cmd) {
        CommandPool.commands.add(cmd);
    }

    public HashSet<CommandBase> getCommands() {
        return CommandPool.commands;
    }
}
