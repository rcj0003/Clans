package me.rcj0003.clans.group;

public enum ClanRole {
	LEADER("Leader", 3), OFFICER("Officer", 2), MEMBER("Member", 1), NONE("None", 0);
	
	private String displayName;
	private int weight;
	
	private ClanRole(String displayName, int weight) {
		this.displayName = displayName;
		this.weight = weight;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public boolean hasHigherWeightThan(ClanRole role) {
		return this.weight > role.weight;
	}
	
	public ClanRole getNextHigherRole() {
		ClanRole nextRole = LEADER;
		
		for (ClanRole role : values())
			if (role.weight > weight && nextRole.weight > role.weight)
				nextRole = role;
		
		return nextRole;
	}
	
	public ClanRole getNextLowerRole() {
		ClanRole nextRole = NONE;
		
		for (ClanRole role : values())
			if (role.weight < weight && nextRole.weight < role.weight)
				nextRole = role;
		
		return nextRole;
	}
}