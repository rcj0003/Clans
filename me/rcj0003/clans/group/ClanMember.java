package me.rcj0003.clans.group;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.rcj0003.clans.group.exceptions.PlayerNotOnlineException;

public class ClanMember {
	private boolean chatActive = false;
	private ClanFactory clanFactory;
	private UUID id;
	private UUID clanId;
	private ClanRole role;
	private int currency;
	private String tag;
	
	public ClanMember(ClanFactory clanFactory, UUID id, UUID clanId, ClanRole role, int currency, String tag) {
		this.clanFactory = clanFactory;
		this.id = id;
		this.clanId = clanId;
		this.role = role;
		this.currency = currency;
		this.tag = tag;
	}
	
	public UUID getUniqueId() {
		return id;
	}
	
	public String getName() {
		return Bukkit.getOfflinePlayer(id).getName();
	}
	
	public Player getOnlinePlayer() throws PlayerNotOnlineException {
		Player player = Bukkit.getPlayer(id);
		
		if (player == null || !player.isOnline())
			throw new PlayerNotOnlineException(id);
		
		return player;
	}
	
	public boolean isOnline() {
		Player player = Bukkit.getPlayer(id);
		
		if (player == null)
			return false;
		
		return player.isOnline();
	}
	
	public ClanMember message(String... message) throws PlayerNotOnlineException {
		Player player = getOnlinePlayer();
		player.sendMessage(message);
		return this;
	}
	
	public ClanRole getRole() {
		return role;
	}
	
	public ClanMember setRole(ClanRole role) {
		this.role = role;
		return this;
	}
	
	public ClanMember update() {
		clanFactory.updateClanMember(this);
		return this;
	}
	
	public ClanMember setClanId(UUID id) {
		if (id == null) {
			role = ClanRole.NONE;
		}
		
		if (id != null && clanId == null) {
			role = ClanRole.MEMBER;
		}
			
		this.clanId = id;
		return this;
	}
	
	public UUID getClanId() {
		return clanId;
	}
	
	public int getCurrency() {
		return currency;
	}

	public ClanMember setCurrency(int currency) {
		this.currency = currency;
		return this;
	}
	
	public ClanMember modifyCurrency(int amount) {
		currency += amount;
		return this;
	}
	
	public String getTag() {
		return tag;
	}
	
	public ClanMember setTag(String tag) {
		this.tag = tag;
		return this;
	}
	
	public boolean isChatActive() {
		return chatActive;
	}
	
	public ClanMember setChatActive(boolean chatActive) {
		this.chatActive = chatActive;
		return this;
	}
}