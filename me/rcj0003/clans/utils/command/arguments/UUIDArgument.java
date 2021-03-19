package me.rcj0003.clans.utils.command.arguments;

import java.util.UUID;
import java.util.regex.Pattern;

public class UUIDArgument implements Argument<UUID> {
	private static final Pattern pattern = Pattern.compile("[A-Fa-f0-9]{8}-[A-Fa-f0-9]{4}-[A-Fa-f0-9]{4}-[A-Fa-f0-9]{4}-[A-Fa-f0-9]{12}");
	
	public boolean isValidArgument(String data) {
		return pattern.matcher(data).matches();
	}

	public UUID getValue(String data) {
		return UUID.fromString(data);
	}

}
