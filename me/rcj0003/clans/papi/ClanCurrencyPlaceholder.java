package me.rcj0003.clans.papi;

import org.bukkit.OfflinePlayer;

import me.rcj0003.clans.group.ClanMember;
import me.rcj0003.clans.group.ClanService;

public class ClanCurrencyPlaceholder implements Placeholder {
	public boolean doesIdentifierMatch(String identifier) {
		return identifier.equalsIgnoreCase("currency");
	}

	public String getResult(ClanService clanService, OfflinePlayer player, String identifier) {
		ClanMember member = clanService.getClanMember(player.getUniqueId());
		return "" + member.getCurrency();
	}
}