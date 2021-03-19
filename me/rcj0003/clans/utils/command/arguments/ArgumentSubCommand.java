package me.rcj0003.clans.utils.command.arguments;

import me.rcj0003.clans.utils.command.CommandUser;
import me.rcj0003.clans.utils.command.SubCommand;
import me.rcj0003.clans.utils.command.exceptions.InvalidArgumentException;

public abstract class ArgumentSubCommand implements SubCommand {
	public abstract Argument<?>[] getArgumentTypes();

	public void execute(CommandUser user, String[] arguments) {
		Argument<?>[] argumentTypes = getArgumentTypes();
		Object[] values = new Object[arguments.length];
		
		for (int i = 0; i < arguments.length; i++) {
			if (i < argumentTypes.length) {
				if (argumentTypes[i].isValidArgument(arguments[i]))
					values[i] = argumentTypes[i].getValue(arguments[i]);
				else
					throw new InvalidArgumentException("Invalid argument at position " + i);
			} else {
				values[i] = arguments[i];
			}
		}
		
		executeVerified(user, arguments);
	}
	
	public abstract void executeVerified(CommandUser user, Object[] values);
}