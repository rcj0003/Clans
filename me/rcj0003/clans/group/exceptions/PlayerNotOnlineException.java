package me.rcj0003.clans.group.exceptions;

import java.util.UUID;

import org.bukkit.OfflinePlayer;

public class PlayerNotOnlineException extends RuntimeException {
	private static final long serialVersionUID = 010000L;
	
	public PlayerNotOnlineException(UUID id) {
		super(String.format("Player '%s' is not online.", id));
	}
	
	public PlayerNotOnlineException(OfflinePlayer player) {
		this(player.getUniqueId());
	}
	
	public PlayerNotOnlineException(String message) {
		super(message);
	}
}