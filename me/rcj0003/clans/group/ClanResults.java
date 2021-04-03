package me.rcj0003.clans.group;

import java.util.UUID;

public class ClanResults {
	private String name;
	private UUID clanId;
	private UUID leaderId;
	private int stars;
	
	public ClanResults(String name, UUID clanId, UUID leaderId, int stars) {
		this.name = name;
		this.clanId = clanId;
		this.leaderId = leaderId;
		this.stars = stars;
	}
	
	public String getName() {
		return name;
	}
	
	public UUID getClanId() {
		return clanId;
	}
	
	public UUID getLeaderId() {
		return leaderId;
	}
	
	public int getStars() {
		return stars;
	}
}