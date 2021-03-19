package me.rcj0003.clans.events;

import me.rcj0003.clans.group.Clan;

public class ClanCreateEvent extends ClanEvent {
	public ClanCreateEvent(Clan clan) {
		super(clan);
	}
}