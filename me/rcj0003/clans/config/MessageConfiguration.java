package me.rcj0003.clans.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import me.rcj0003.clans.utils.command.MessageWrapper;

public class MessageConfiguration {
	private FileConfiguration config;

	public MessageConfiguration(FileConfiguration config) {
		this.config = config;
	}

	public String getMessage(MessageType messageType, Object... objects) {
		return String.format(config.getString("prefix") + config.getString(messageType.getUrl(), "Message not configured."), objects).replace('&',
				ChatColor.COLOR_CHAR);
	}

	public MessageWrapper constructWrapper(String url) {
		return new MessageWrapper(config.getStringList(String.format("%s.no-args", url)).toArray(new String[0]),
				config.getString(String.format("%s.command-dne", url)), config.getString(String.format("%s.no-perms", url)),
				config.getString(String.format("%s.player-required", url)), config.getString(String.format("%s.usage", url)));
	}
}