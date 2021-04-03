package me.rcj0003.clans.papi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.OfflinePlayer;

import me.rcj0003.clans.group.ClanMember;
import me.rcj0003.clans.group.ClanService;

public class ClanHasCurrencyPlaceholder implements Placeholder {
	private static final Pattern PATTERN = Pattern.compile("has-currency-([0-9]+)");
	
	public boolean doesIdentifierMatch(String identifier) {
		return PATTERN.matcher(identifier).matches();
	}

	public String getResult(ClanService clanService, OfflinePlayer player, String identifier) {
		Matcher matcher = PATTERN.matcher(identifier);
		Integer value = Integer.valueOf(matcher.group());
		ClanMember member = clanService.getClanMember(player.getUniqueId());
		return member.getCurrency() >= value ? "true" : "false";
	}
}