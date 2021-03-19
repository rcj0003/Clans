package me.rcj0003.clans.commands;

import me.rcj0003.clans.config.MessageConfiguration;
import me.rcj0003.clans.group.ClanService;
import me.rcj0003.clans.utils.command.CommandUser;
import me.rcj0003.clans.utils.command.SubCommand;
import me.rcj0003.clans.utils.command.exceptions.InvalidArgumentException;

public class LeaveCommand implements SubCommand {
	private static final String[] DESCRIPTION = new String[] { "Creates a clan. You must specify a name for your clan." };
	private ClanService clanService;
	private MessageConfiguration config;
	
	public LeaveCommand(ClanService clanService, MessageConfiguration config) {
		this.clanService = clanService;
		this.config = config;
	}
	
	public boolean getRequiresPlayer() {
		return false;
	}

	public int getMinimumArguments() {
		return 0;
	}

	public String getName() {
		return null;
	}

	public String[] getDescription() {
		return null;
	}

	public String getUsage() {
		return null;
	}

	public void execute(CommandUser user, String[] arguments) throws InvalidArgumentException {
	}
}
