package me.rcj0003.clans.group.exceptions;

import java.util.UUID;

import org.bukkit.OfflinePlayer;

public class AlreadyMemberException extends RuntimeException {
	private static final long serialVersionUID = 010000L;
	
	public AlreadyMemberException(UUID player, UUID clan) {
		super(String.format("Player '%s' is already member of clan '%s'.", clan));
	}
	
	public AlreadyMemberException(OfflinePlayer player, UUID clan) {
		this(player.getUniqueId(), clan);
	}
	
	public AlreadyMemberException(String message) {
		super(message);
	}
}