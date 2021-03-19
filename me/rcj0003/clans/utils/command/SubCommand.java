package me.rcj0003.clans.utils.command;

import me.rcj0003.clans.utils.command.exceptions.InvalidArgumentException;

public interface SubCommand {
	boolean getRequiresPlayer();
	int getMinimumArguments();
	
	String getName();
	String[] getDescription();
	String getUsage();
	
	void execute(CommandUser user, String[] arguments) throws InvalidArgumentException;
}