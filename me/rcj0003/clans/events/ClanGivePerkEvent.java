package me.rcj0003.clans.events;

import me.rcj0003.clans.group.Clan;
import me.rcj0003.clans.group.ClanPerk;

public class ClanGivePerkEvent extends ClanEvent {
	private ClanPerk perk;
	
	public ClanGivePerkEvent(Clan clan, ClanPerk perk) {
		super(clan);
	}
	
	public ClanPerk getClanPerk() {
		return perk;
	}
}