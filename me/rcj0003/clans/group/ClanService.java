package me.rcj0003.clans.group;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.rcj0003.clans.events.ClanCreateEvent;
import me.rcj0003.clans.events.ClanDisbandEvent;
import me.rcj0003.clans.events.ClanLeaveEvent;
import me.rcj0003.clans.events.ClanLeaveEvent.LeaveReason;
import me.rcj0003.clans.group.exceptions.ClanDoesNotExistException;

public class ClanService {
	private static final int CLAN_MAX_CAPACITY = 24;
	
	private Map<UUID, Clan> clans = new ConcurrentHashMap<>();
	private Map<UUID, ClanMember> clanMembers = new ConcurrentHashMap<>();
	
	private List<ClanResults> leaderboard;
	
	private ClanFactory clanFactory;
	private Plugin plugin;
	
	public ClanService(Plugin plugin, ClanFactory clanFactory) {
		this.plugin = plugin;
		this.clanFactory = clanFactory;
		
		new BukkitRunnable() {
			public void run() {
				List<ClanResults> newLeaderboard = clanFactory.getTopClans();
				leaderboard = newLeaderboard;
			}
		}.runTaskTimerAsynchronously(plugin, 1, 20 * 300);
	}
	
	public void shutdown() {
		for (Clan clan : clans.values())
			clan.update();
		
		for (ClanMember member : clanMembers.values())
			member.update();
	}
	
	public List<ClanMember> getClanMembers(Clan clan) {
		List<ClanMember> clanMembers = new ArrayList<>(CLAN_MAX_CAPACITY);
		
		for (UUID memberId : clan.getMembers())
			clanMembers.add(getClanMember(memberId));
		
		return clanMembers;
	}
	
	public Clan getClanForPlayer(Player player) throws ClanDoesNotExistException {
		return getClanForPlayer(player.getUniqueId());
	}
	
	public Clan getClanForPlayer(UUID id) throws ClanDoesNotExistException {
		ClanMember member = getClanMember(id);
		
		if (member.getClanId() == null)
			return null;

		if (clans.containsKey(member.getClanId())) {
			return clans.get(member.getClanId());
		} else {
			try {
				Clan clan = clanFactory.getClanById(id);
				clans.put(clan.getUniqueId(), clan);
				return clan;
			}
			catch (ClanDoesNotExistException e) {
				return null;
			}
		}
	}
	
	public Clan getClanById(UUID id) throws ClanDoesNotExistException {
		if (clans.containsKey(id)) {
			return clans.get(id);
		} else {
			try {
				Clan clan = clanFactory.getClanById(id);
				clans.put(clan.getUniqueId(), clan);
				return clan;
			}
			catch (ClanDoesNotExistException e) {
				return null;
			}
		}
	}
	
	public ClanMember getClanMember(Player player) {
		return getClanMember(player.getUniqueId());
	}
	
	public ClanMember getClanMember(UUID id) {
		if (clanMembers.containsKey(id)) {
			return clanMembers.get(id);
		} else {
			ClanMember member = clanFactory.getOrCreateClanMemberById(id);
			clanMembers.put(id, member);
			return member;
		}
	}
	
	public void updateClanAsync(Clan clan) {
		new BukkitRunnable() {
			public void run() {
				clan.update();
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public void updateClanMemberAsync(ClanMember member) {
		new BukkitRunnable() {
			public void run() {
				member.update();
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public Clan createClan(Player player, String name) {
		Clan clan = clanFactory.createClan(player, name);
		clans.put(clan.getUniqueId(), clan);
		ClanMember member = getClanMember(player.getUniqueId());
		member.setClanId(clan.getUniqueId()).setRole(ClanRole.LEADER);
		Bukkit.getPluginManager().callEvent(new ClanCreateEvent(clan));
		return clan;
	}
	
	public void createClanAsync(Player player, String name) {
		new BukkitRunnable() {
			public void run() {
				createClan(player, name);
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public void deleteClan(Clan clan) {
		Bukkit.getPluginManager().callEvent(new ClanDisbandEvent(clan));
		
		for (UUID member : clan.getMembers()) {
			ClanMember clanMember = clanMembers.get(member);
			
			if (clanMember != null)
				clanMember.setClanId(null);
		}
		
		clans.remove(clan.getUniqueId());
		
		new BukkitRunnable() {
			public void run() {
				clanFactory.deleteClan(clan.getUniqueId());
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public void leaveClan(ClanMember member, LeaveReason reason) {
		Bukkit.getPluginManager().callEvent(new ClanLeaveEvent(getClanById(member.getClanId()), member, reason));

		if (clans.containsKey(member.getClanId())) {
			Clan clan = clans.get(member.getClanId());
			clan.removeMember(member.getUniqueId());
		}
		
		member.setClanId(null).setRole(ClanRole.NONE);
		
		new BukkitRunnable() {
			public void run() {
				member.update();
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public List<ClanResults> getLeaderboard() {
		return leaderboard;
	}
	
	public void preloadData(UUID id) {
		new BukkitRunnable() {
			public void run() {
				ClanMember member = clanFactory.getOrCreateClanMemberById(id);
				clanMembers.put(id, member);
				
				if (member.getClanId() != null) {
					try {
						Clan clan = clanFactory.getClanById(member.getClanId());
						clans.put(clan.getUniqueId(), clan);
					}
					catch (ClanDoesNotExistException e) {
						e.printStackTrace();
						return;
					}
				}
			}
		}.runTaskAsynchronously(plugin);
	}
}