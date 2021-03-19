package me.rcj0003.clans.group.exceptions;

public class ClanMaxCapacityException extends RuntimeException {
	private static final long serialVersionUID = 010000L;

	public ClanMaxCapacityException() {
		super("Clan at maximum capacity");
	}
	
	public ClanMaxCapacityException(String message) {
		super(message);
	}
}