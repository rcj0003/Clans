package me.rcj0003.clans.events;

import me.rcj0003.clans.group.Clan;
import me.rcj0003.clans.group.ClanMember;

public class ClanLeaveEvent extends ClanEvent {
	public enum LeaveReason {
		PLUGIN, KICK, LEAVE
	}
	
	private ClanMember member;
	private LeaveReason reason;
	
	public ClanLeaveEvent(Clan clan, ClanMember member, LeaveReason reason) {
		super(clan);
		this.member = member;
		this.reason = reason;
	}
	
	public ClanMember getMember() {
		return member;
	}
	
	public LeaveReason getReason() {
		return reason;
	}
}