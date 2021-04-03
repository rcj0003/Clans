package me.rcj0003.clans.utils.command.arguments;

import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class PlayerArgument implements Argument<OfflinePlayer> {
	private static final Pattern pattern = Pattern.compile("[A-Z0-9\\_\\$\\@]{1,24}", Pattern.CASE_INSENSITIVE);
	
	public boolean isValidArgument(String data) {
		return pattern.matcher(data).matches();
	}

	@SuppressWarnings("deprecation")
	public OfflinePlayer getValue(String data) {
		return Bukkit.getOfflinePlayer(data);
	}
}