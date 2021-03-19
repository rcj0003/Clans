package me.rcj0003.clans.utils.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SuperCommand extends CommandProcessor implements CommandExecutor {
	public SuperCommand(String pluginName, String name, String permission, MessageWrapper wrapper) {
		super(name, permission, wrapper);
		registerSubcommand(new HelpCommand(this.registeredCommands, pluginName, parentCommand, permission));
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		execute(new CommandUser(sender), args);
		return true;
	}
}