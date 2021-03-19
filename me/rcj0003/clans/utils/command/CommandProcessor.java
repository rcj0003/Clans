package me.rcj0003.clans.utils.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandProcessor {
	protected Map<String, SubCommand> registeredCommands = new HashMap<>();
	protected String name;
	protected String permission;
	protected MessageWrapper messageWrapper;
	protected CommandProcessor parentProcessor;
	
	protected String parentCommand;
	
	public CommandProcessor(String name, String permission, MessageWrapper messageWrapper) {
		this(name, permission, messageWrapper, null);
	}
	
	public CommandProcessor(String name, String permission, MessageWrapper messageWrapper, CommandProcessor parentProcessor) {
		this.name = name;
		this.permission = permission;
		this.messageWrapper = messageWrapper;
		this.parentProcessor = parentProcessor;
		
		parentCommand = getParentCommandName();
	}
	
	private String getParentCommandName() {
		CommandProcessor processor = this;
		String parentCommand = "";
		
		while (processor != null) {
			parentCommand = (processor.hasParent() ? " " : "") + processor.getName().toLowerCase();
			processor = processor.getParentProcessor();
		}
		
		return "/" + parentCommand;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean hasParent() {
		return parentProcessor != null;
	}
	
	public CommandProcessor getParentProcessor() {
		return parentProcessor;
	}
	
	public String getPermission() {
		return permission;
	}
	
	public List<SubCommand> getRegisteredSubcommands() {
		return new ArrayList<SubCommand>(registeredCommands.values());
	}

	public List<String> getSubcommandNames() {
		return new ArrayList<String>(registeredCommands.keySet());
	}

	public boolean registerSubcommand(SubCommand command) {
		if (registeredCommands.containsKey(command.getName()))
			return false;
		registeredCommands.put(command.getName(), command);
		return true;
	}

	public void registerSubcommand(SubCommand... command) {
		for (SubCommand cmd : command)
			registerSubcommand(cmd);
	}

	public SubCommand getExactSubcommandByName(String command) {
		return registeredCommands.get(command);
	}

	public List<SubCommand> getSubcommandsByName(String search) {
		return new ArrayList<SubCommand>(getRegisteredSubcommands().stream().filter(e -> e.getName().contains(search))
				.collect(Collectors.toList()));
	}
	
	public void registerCommands(SubCommand... commands) {
		for (SubCommand command : commands)
			registeredCommands.put(command.getName().toLowerCase(), command);
	}
	
	public void execute(CommandUser user, String[] arguments) {
		if (arguments.length == 0) {
			user.sendFormattedMessage(messageWrapper.getNoArgumentsMessage());
			return;
		}

		SubCommand command = registeredCommands.get(arguments[0]);

		if (command == null) {
			user.sendFormattedMessage(messageWrapper.getCommandNotFoundMessage());
			return;
		}

		if (!user.hasPermission(permission + (permission.endsWith(".") ? "" : ".") + command.getName().toLowerCase())) {
			user.sendFormattedMessage(messageWrapper.getNoPermsMessage());
			return;
		}

		if (command.getRequiresPlayer() && !user.isPlayer()) {
			user.sendFormattedMessage(messageWrapper.getRequiresPlayerMessage());
			return;
		}

		ArrayList<String> newArguments = new ArrayList<String>(Arrays.asList(arguments));
		newArguments.remove(0);

		if (newArguments.size() < command.getMinimumArguments()) {
			user.sendFormattedMessage(messageWrapper.getUsageMessage().replace("{usage}",
					parentCommand + " " + command.getName() + " " + command.getUsage()));
			return;
		}

		command.execute(user, newArguments.toArray(new String[0]));
		
	}
}