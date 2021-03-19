package me.rcj0003.clans.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.rcj0003.clans.group.Clan;

public abstract class ClanEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	private Clan clan;
	
	public ClanEvent(Clan clan) {
		this.clan = clan;
	}
	
	public Clan getClan() {
		return clan;
	}
}