package me.rcj0003.clans.utils.command.arguments;

import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class PlayerArgument implements Argument<OfflinePlayer> {
	private static final Pattern pattern = Pattern.compile("[A-Fa-f0-9_$@]{1,16}");
	
	public boolean isValidArgument(String data) {
		return pattern.matcher(data).matches();
	}

	public OfflinePlayer getValue(String data) {
		return Bukkit.getPlayer(data);
	}
}