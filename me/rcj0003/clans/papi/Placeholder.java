package me.rcj0003.clans.papi;

import org.bukkit.OfflinePlayer;

import me.rcj0003.clans.group.ClanService;

public interface Placeholder {
	boolean doesIdentifierMatch(String identifier);
	String getResult(ClanService clanService, OfflinePlayer player, String identifier);
}
