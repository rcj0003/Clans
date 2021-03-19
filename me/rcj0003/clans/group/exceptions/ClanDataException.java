package me.rcj0003.clans.group.exceptions;

public class ClanDataException extends RuntimeException {
	private static final long serialVersionUID = 010000L;
	
	public ClanDataException(Throwable t) {
		super(t);
	}
	
	public ClanDataException(String message) {
		super(message);
	}
}