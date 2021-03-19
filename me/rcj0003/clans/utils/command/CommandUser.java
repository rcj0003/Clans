package me.rcj0003.clans.utils.command;

import java.util.Collection;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import me.rcj0003.clans.utils.Utils.StringUtils;

public class CommandUser {
	private CommandSender sender;

	public CommandUser(final CommandSender sender) {
		this.sender = sender;
	}

	public CommandSender getSender() {
		return sender;
	}
	
	public void executeCommand(String command) {
		Bukkit.dispatchCommand(sender, command.replace("{sender}", getName()));
	}
	
	public boolean hasPermission(String permission) {
		return sender.isOp() || sender.hasPermission(permission);
	}

	public void sendFormattedMessage(final String... message) {
		sender.sendMessage(Stream.of(message).map(e -> getFormattedMessage(e)).toArray(e -> new String[e]));
	}
	
	public void sendFormattedMessage(final Collection<String> message) {
		sender.sendMessage(message.stream().map(e -> getFormattedMessage(e)).toArray(e -> new String[e]));
	}

	public String getFormattedMessage(String message) {
		if (isPlayer()) {
			message = PlaceholderAPI.setPlaceholders(getPlayer(), message);
		}
		
		return StringUtils.convertColorCodes(message.replace("{sender}", sender.getName()));
	}

	public String getName() {
		return sender.getName();
	}

	public boolean isPlayer() {
		return sender instanceof Player;
	}
	
	public Player getPlayer() {
		return (Player) sender;
	}
}
