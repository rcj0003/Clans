package me.rcj0003.clans.utils.command;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.chat.ComponentSerializer;

public class HelpCommand implements SubCommand {
	private Map<String, SubCommand> commands;
	private String headerName, command, permission;

	public HelpCommand(Map<String, SubCommand> commands, String headerName, String command, String permission) {
		this.commands = commands;
		this.headerName = headerName;
		this.command = command;
		this.permission = permission;
	}

	public boolean getRequiresPlayer() {
		return false;
	}

	public int getMinimumArguments() {
		return 0;
	}

	public String getName() {
		return "help";
	}

	public String[] getDescription() {
		return new String[] { "Gives detailed information about all " + headerName + " commands." };
	}

	public String getUsage() {
		return "(Search Term)";
	}

	public void execute(CommandUser user, String[] arguments) {
		switch (arguments.length) {
		case 0: {
			user.sendFormattedMessage("&8=[&c" + headerName + " Command List&8]=",
					"&7" + String.join(", ", commands.values().stream()
							.filter(e -> user.hasPermission(
									permission + (permission.endsWith(".") ? "" : ".") + e.getName().toLowerCase()))
							.map(e -> e.getName()).sorted().collect(Collectors.toList())));
			break;
		}
		default: {
			List<SubCommand> foundCommands = commands.values().stream()
					.filter(e -> e.getName().toLowerCase().contains(arguments[0].toLowerCase()))
					.filter(e -> user.hasPermission(
							permission + (permission.endsWith(".") ? "" : ".") + e.getName().toLowerCase()))
					.sorted((e, y) -> e.getName().toLowerCase().compareTo(y.getName().toLowerCase()))
					.collect(Collectors.toList());

			user.sendFormattedMessage("&8=[&c" + headerName + " Command Search&8]=", "&7Search: &c" + arguments[0],
					"&c" + foundCommands.size() + "&7 results found.");

			if (foundCommands.size() == 0)
				return;

			user.sendFormattedMessage("", "&7Commands:");

			for (SubCommand c : foundCommands) {
				if (user.isPlayer()) {
					TextComponent newLine = new TextComponent(ComponentSerializer.parse("{text: \"\n\"}"));

					TextComponent textComponent = new TextComponent(new ComponentBuilder("[").color(ChatColor.DARK_GRAY)
							.append("Info").color(ChatColor.RED).append("]").color(ChatColor.DARK_GRAY).create());

					textComponent.addExtra(newLine);

					textComponent.addExtra(new TextComponent(new ComponentBuilder("Minimum arguments: ")
							.color(ChatColor.GRAY).append("" + c.getMinimumArguments()).color(ChatColor.RED).create()));

					textComponent.addExtra(newLine);

					textComponent.addExtra(
							new TextComponent(new ComponentBuilder("[").color(ChatColor.DARK_GRAY).append("Description")
									.color(ChatColor.RED).append("]").color(ChatColor.DARK_GRAY).create()));

					if (c.getDescription() != null)
						for (String line : c.getDescription()) {
							textComponent.addExtra(newLine);
							textComponent.addExtra(
									new TextComponent(new ComponentBuilder(line).color(ChatColor.GRAY).create()));
						}

					user.getPlayer().spigot()
							.sendMessage(new ComponentBuilder(c.getName()).color(ChatColor.RED)
									.event(new HoverEvent(Action.SHOW_TEXT, new BaseComponent[] { textComponent }))
									.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
											"/" + command + " " + c.getName().toLowerCase() + " "))
									.create());
				} else {
					user.sendFormattedMessage("&7Command name: &c" + c.getName(),
							c.getRequiresPlayer() ? "&cCannot be executed from console."
									: "&6Can be executed from console.",
							"&7Minimum arguments: &c" + c.getMinimumArguments());
					if (c.getDescription() != null)
						user.sendFormattedMessage(c.getDescription());
					user.sendFormattedMessage("");
				}
			}
		}
		}
	}
}