package me.rcj0003.clans.utils.command.arguments;

public interface Argument<T> {
	boolean isValidArgument(String data);
	T getValue(String data);
}