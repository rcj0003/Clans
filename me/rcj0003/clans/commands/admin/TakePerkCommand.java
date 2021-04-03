package me.rcj0003.clans.commands.admin;

import me.rcj0003.clans.utils.command.CommandUser;
import me.rcj0003.clans.utils.command.SubCommand;
import me.rcj0003.clans.utils.command.exceptions.InvalidArgumentException;

public class TakePerkCommand implements SubCommand {
	public boolean getRequiresPlayer() {
		return false;
	}

	public int getMinimumArguments() {
		return 1;
	}

	public String getName() {
		return "takeperk";
	}

	public String[] getDescription() {
		return null;
	}

	public String getUsage() {
		return "[Perk Name]";
	}

	public void execute(CommandUser user, String[] arguments) throws InvalidArgumentException {
	}
}