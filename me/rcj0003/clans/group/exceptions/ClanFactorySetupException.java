package me.rcj0003.clans.group.exceptions;

public class ClanFactorySetupException extends Exception {
	private static final long serialVersionUID = 010000L;
	
	public ClanFactorySetupException(String message) {
		super(message);
	}
	
	public ClanFactorySetupException(Throwable t) {
		super(t);
	}
	
	public ClanFactorySetupException(String message, Throwable t) {
		super(message, t);
	}
}