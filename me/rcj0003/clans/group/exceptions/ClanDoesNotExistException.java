package me.rcj0003.clans.group.exceptions;

public class ClanDoesNotExistException extends RuntimeException {
	private static final long serialVersionUID = 010000L;

	public ClanDoesNotExistException() {
		super("Clan does not exist");
	}
	
	public ClanDoesNotExistException(String message) {
		super(message);
	}
	
	public ClanDoesNotExistException(Throwable t) {
		super(t);
	}
}