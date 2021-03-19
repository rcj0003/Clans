package me.rcj0003.clans.commands;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.rcj0003.clans.config.MessageConfiguration;
import me.rcj0003.clans.group.ClanService;
import me.rcj0003.clans.utils.command.CommandUser;
import me.rcj0003.clans.utils.command.arguments.Argument;
import me.rcj0003.clans.utils.command.arguments.ArgumentSubCommand;
import me.rcj0003.clans.utils.command.arguments.PlayerArgument;

public class InviteCommand extends ArgumentSubCommand {
	private static final String[] DESCRIPTION = new String[] { "Invites a player to your clan (if you have appropriate permissions to do so." };
	private static final Argument<?>[] arguments = new Argument<?>[] { new PlayerArgument() };
	private Plugin plugin;
	private ClanService clanService;
	private MessageConfiguration config;
	
	public InviteCommand(Plugin plugin, ClanService clanService, MessageConfiguration config) {
		this.plugin = plugin;
		this.clanService = clanService;
		this.config = config;
	}
	
	public boolean getRequiresPlayer() {
		return true;
	}

	public int getMinimumArguments() {
		return 1;
	}

	public String getName() {
		return "invite";
	}

	public String[] getDescription() {
		return DESCRIPTION;
	}

	public String getUsage() {
		return "[Player]";
	}

	public Argument<?>[] getArgumentTypes() {
		return arguments;
	}

	public void executeVerified(CommandUser user, Object[] values) {
		Player player = (Player) values[0];
	}
}