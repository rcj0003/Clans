package me.rcj0003.clans.commands;

import me.rcj0003.clans.config.MessageConfiguration;
import me.rcj0003.clans.group.ClanService;
import me.rcj0003.clans.utils.command.CommandUser;
import me.rcj0003.clans.utils.command.SubCommand;
import me.rcj0003.clans.utils.command.exceptions.InvalidArgumentException;

public class SetNameCommand implements SubCommand {
	private static final String[] DESCRIPTION = new String[] { "Sets the name for your clan." };
	private ClanService clanService;
	private MessageConfiguration config;
	
	public SetNameCommand(ClanService clanService, MessageConfiguration config) {
		this.clanService = clanService;
		this.config = config;
	}
	
	public boolean getRequiresPlayer() {
		return true;
	}

	public int getMinimumArguments() {
		return 0;
	}

	public String getName() {
		return "setname";
	}

	public String[] getDescription() {
		return DESCRIPTION;
	}

	public String getUsage() {
		return null;
	}

	public void execute(CommandUser user, String[] arguments) throws InvalidArgumentException {
	}
}
