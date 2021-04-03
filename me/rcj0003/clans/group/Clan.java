package me.rcj0003.clans.group;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.rcj0003.clans.group.exceptions.AlreadyMemberException;
import me.rcj0003.clans.group.exceptions.ClanMaxCapacityException;
import me.rcj0003.clans.utils.Utils.StringUtils;

public class Clan {
	private ClanFactory clanFactory;
	private UUID id;
	private String name, motd;
	private int maxCapacity, stars, awardedStars;
	private Set<UUID> members;
	private Set<UUID> inviteList;
	private Set<ClanPerk> availablePerks;
	
	public Clan(ClanFactory clanFactory, UUID id, String name, String motd, int maxCapacity, int stars, Set<UUID> members, Set<UUID> inviteList, Set<ClanPerk> availablePerks) {
		this.clanFactory = clanFactory;
		this.id = id;
		this.name = name;
		this.motd = motd;
		this.maxCapacity = maxCapacity;
		this.stars = stars;
		this.awardedStars = 0;
		this.members = new HashSet<>(maxCapacity);
		this.inviteList = inviteList;
		this.availablePerks = availablePerks;
		
		if (members.size() > maxCapacity)
			throw new ClanMaxCapacityException(String.format("%s is the maximum capacity for a clan. %s occupants given.", maxCapacity,  members.size()));
		
		this.members.addAll(members);
	}

	public UUID getUniqueId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Clan setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getMOTD() {
		return motd;
	}
	
	public Clan setMOTD(String motd) {
		this.motd = motd;
		return this;
	}
	
	public Set<UUID> getMembers() {
		return Collections.unmodifiableSet(members);
	}
	
	public int getMemberCount() {
		return members.size();
	}
	
	public int getCapacity() {
		return maxCapacity;
	}
	
	public Clan setCapacity(int newCapacity) {
		maxCapacity = newCapacity;
		return this;
	}
	
	public Clan removeMember(UUID id) {
		members.remove(id);
		return this;
	}
	
	public boolean isMember(UUID id) {
		return members.contains(id);
	}
	
	public Clan invite(UUID id) {
		inviteList.add(id);
		return this;
	}
	
	public Clan revokeInvite(UUID id) {
		inviteList.remove(id);
		return this;
	}
	
	public boolean isInvited(UUID id) {
		return inviteList.contains(id);
	}
	
	public Set<UUID> getInvited() {
		return inviteList;
	}
	
	public Clan message(String... message) {
		for (UUID id : members) {
			Player player = Bukkit.getServer().getPlayer(id);
			
			if (player.isOnline())
				player.sendMessage(StringUtils.convertColorCodes(message));
		}
		
		return this;
	}
	
	public ClanMember addMember(ClanService clanService, UUID id) throws ClanMaxCapacityException, AlreadyMemberException {
		if (members.size() >= maxCapacity)
			throw new ClanMaxCapacityException();	
		
		if (members.contains(id))
			throw new AlreadyMemberException(id, this.id);
		
		ClanMember clanMember = clanService.getClanMember(id);
		
		if (clanMember.getClanId() != null)
			throw new AlreadyMemberException(id, clanMember.getClanId());
		
		clanMember.setRole(ClanRole.MEMBER).setClanId(this.id).update();
		members.add(id);
		
		return clanMember;
	}
	
	public Clan update() {
		clanFactory.updateClan(this);
		return this;
	}
	
	public boolean hasPerk(ClanPerk perk) {
		return availablePerks.contains(perk);
	}
	
	public Clan addPerk(ClanPerk perk) {
		availablePerks.add(perk);
		return this;
	}
	
	public Clan removePerk(ClanPerk perk) {
		availablePerks.remove(perk);
		return this;
	}
	
	public Set<ClanPerk> getPerks() {
		return availablePerks;
	}
	
	public int getStars() {
		return stars + awardedStars;
	}
	
	public Clan modifyStars(int value) {
		awardedStars += value;
		return this;
	}
	
	public int getAwardedStars() {
		return awardedStars;
	}
	
	public Clan resetAwardedStars() {
		awardedStars = 0;
		return this;
	}
}