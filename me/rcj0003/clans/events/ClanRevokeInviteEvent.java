package me.rcj0003.clans.events;

import java.util.UUID;

import me.rcj0003.clans.group.Clan;

public class ClanRevokeInviteEvent extends ClanEvent {
	private UUID invited;
	
	public ClanRevokeInviteEvent(Clan clan, UUID invited) {
		super(clan);
		this.invited = invited;
	}
	
	public UUID getInvited() {
		return invited;
	}
}