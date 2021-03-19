package me.rcj0003.clans.papi;

import org.bukkit.OfflinePlayer;

import me.rcj0003.clans.group.Clan;
import me.rcj0003.clans.group.ClanService;
import me.rcj0003.clans.group.exceptions.ClanDoesNotExistException;

public class ClanNamePlaceholder implements Placeholder {
	public boolean doesIdentifierMatch(String identifier) {
		return identifier.equalsIgnoreCase("clan_name");
	}

	public String getResult(ClanService clanService, OfflinePlayer player, String identifier) {
		try {
			Clan clan = clanService.getClanForPlayer(player.getUniqueId());
			return clan.getName();
		}
		catch (ClanDoesNotExistException e) {
			return "None";
		}
	}
}