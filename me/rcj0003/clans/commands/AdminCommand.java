package me.rcj0003.clans.commands;

import org.bukkit.plugin.Plugin;

import me.rcj0003.clans.commands.admin.ForceDemoteCommand;
import me.rcj0003.clans.commands.admin.ForcePromoteCommand;
import me.rcj0003.clans.commands.admin.GiveCurrencyCommand;
import me.rcj0003.clans.commands.admin.SetCapacityCommand;
import me.rcj0003.clans.config.MessageConfiguration;
import me.rcj0003.clans.group.ClanService;
import me.rcj0003.clans.utils.command.CommandProcessor;
import me.rcj0003.clans.utils.command.HelpCommand;
import me.rcj0003.clans.utils.command.MessageWrapper;
import me.rcj0003.clans.utils.command.SubCommand;
import me.rcj0003.clans.utils.command.SuperCommand;

public class AdminCommand extends SuperCommand implements SubCommand {
	private static String[] DESCRIPTION = new String[] { "Admin commands for managing clans and currency." };

	public AdminCommand(Plugin plugin, ClanService clanService, MessageConfiguration config, String name,
			String permission, MessageWrapper wrapper, CommandProcessor parentProcessor) {
		super(name, permission, "clans.admin", wrapper);

		registerSubcommand(
				new HelpCommand(this.registeredCommands, "Clans Admin", "clans admin", "clans.command.admin"),
				new ForceDemoteCommand(plugin, clanService, config),
				new ForcePromoteCommand(plugin, clanService, config),
				new GiveCurrencyCommand(plugin, clanService, config),
				new SetCapacityCommand(plugin, clanService, config));
	}

	public boolean getRequiresPlayer() {
		return false;
	}

	public int getMinimumArguments() {
		return 0;
	}

	public String[] getDescription() {
		return DESCRIPTION;
	}

	public String getUsage() {
		return "(Sub-command)";
	}
}