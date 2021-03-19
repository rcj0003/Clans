package me.rcj0003.clans.events;

import me.rcj0003.clans.group.Clan;
import me.rcj0003.clans.group.ClanMember;
import me.rcj0003.clans.group.ClanRole;

public class ClanDemoteEvent extends ClanEvent {
	private ClanMember member;
	private ClanRole oldRole;
	private ClanRole newRole;
	
	public ClanDemoteEvent(Clan clan, ClanMember member, ClanRole oldRole, ClanRole newRole) {
		super(clan);
		this.member = member;
		this.oldRole = oldRole;
		this.newRole = newRole;
	}
	
	public ClanMember getMember() {
		return member;
	}
	
	public ClanRole getNewRole() {
		return newRole;
	}
	
	public ClanRole getOldRole() {
		return oldRole;
	}
}