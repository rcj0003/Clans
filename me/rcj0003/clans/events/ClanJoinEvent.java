package me.rcj0003.clans.events;

import me.rcj0003.clans.group.Clan;
import me.rcj0003.clans.group.ClanMember;

public class ClanJoinEvent extends ClanEvent {
	private ClanMember member;
	
	public ClanJoinEvent(Clan clan, ClanMember member) {
		super(clan);
		this.member = member;
	}
	
	public ClanMember getMember() {
		return member;
	}
}
