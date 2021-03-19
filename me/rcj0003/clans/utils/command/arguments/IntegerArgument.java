package me.rcj0003.clans.utils.command.arguments;

import java.util.regex.Pattern;

public class IntegerArgument implements Argument<Integer> {
	private static final Pattern PATTERN = Pattern.compile("-?[0-9]{1,6}");
	private boolean positiveRequired;
	
	public IntegerArgument(boolean positiveRequired) {
		this.positiveRequired = positiveRequired;
	}

	public boolean isValidArgument(String data) {
		if (PATTERN.matcher(data).matches())
			return !positiveRequired || (Integer.valueOf(data) >= 0);
		return false;
	}

	public Integer getValue(String data) {
		return Integer.valueOf(data);
	}
}