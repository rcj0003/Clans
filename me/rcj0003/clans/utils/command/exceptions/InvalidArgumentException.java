package me.rcj0003.clans.utils.command.exceptions;

public class InvalidArgumentException extends RuntimeException {
	private static final long serialVersionUID = 010000L;
	
	public InvalidArgumentException(String message) {
		super(message);
	}
}