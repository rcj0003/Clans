package me.rcj0003.clans.events;

import me.rcj0003.clans.group.Clan;

public class ClanDisbandEvent extends ClanEvent {
	public ClanDisbandEvent(Clan clan) {
		super(clan);
	}
}