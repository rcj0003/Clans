package me.rcj0003.clans.utils.command.arguments;

import java.util.regex.Pattern;

public class BooleanArgument implements Argument<Boolean> {
	private static final Pattern pattern = Pattern.compile("(?:true)|(?:false)", Pattern.CASE_INSENSITIVE);
	
	public boolean isValidArgument(String data) {
		return pattern.matcher(data).matches();
	}

	public Boolean getValue(String data) {
		return Boolean.valueOf(data);
	}
	
}