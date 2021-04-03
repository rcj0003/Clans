package me.rcj0003.clans.commands;

import java.util.Iterator;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.rcj0003.clans.config.MessageConfiguration;
import me.rcj0003.clans.config.MessageType;
import me.rcj0003.clans.group.Clan;
import me.rcj0003.clans.group.ClanMember;
import me.rcj0003.clans.group.ClanService;
import me.rcj0003.clans.utils.command.CommandUser;
import me.rcj0003.clans.utils.command.arguments.Argument;
import me.rcj0003.clans.utils.command.arguments.ArgumentSubCommand;
import me.rcj0003.clans.utils.command.arguments.PlayerArgument;

public class InfoCommand extends ArgumentSubCommand {
	private static final String[] DESCRIPTION = new String[] { "Lists info for a clan." };
	private static final Argument<?>[] arguments = new Argument<?>[] { new PlayerArgument() };
	private Plugin plugin;
	private ClanService clanService;
	private MessageConfiguration config;
	
	public InfoCommand(Plugin plugin, ClanService clanService, MessageConfiguration config) {
		this.plugin = plugin;
		this.clanService = clanService;
		this.config = config;
	}
	
	public boolean getRequiresPlayer() {
		return false;
	}

	public int getMinimumArguments() {
		return 0;
	}

	public String getName() {
		return "info";
	}

	public String[] getDescription() {
		return DESCRIPTION;
	}

	public String getUsage() {
		return "[Player Name]";
	}

	public Argument<?>[] getArgumentTypes() {
		return arguments;
	}

	public void executeVerified(CommandUser user, Object[] values) {
		if (!user.isPlayer()) {
			if (values.length < 1) {
				user.sendFormattedMessage(config.getMessage(MessageType.NO_ARGUMENTS));
				return;
			}
		}
		
		OfflinePlayer player = values.length < 1 ? user.getPlayer() : (OfflinePlayer) values[0];
		
		new BukkitRunnable() {
			public void run() {
				Clan clan = clanService.getClanForPlayer(player.getUniqueId());
				
				if (clan == null) {
					user.sendFormattedMessage(config.getMessage(MessageType.PLAYER_NO_CLAN));
					return;
				}
				
				user.sendFormattedMessage(config.getMessage(MessageType.LOADING));
				
				List<ClanMember> members = clanService.getClanMembers(clan);
				user.sendFormattedMessage(config.getMessage(MessageType.INFO_HEADER));
				user.sendFormattedMessage(config.getMessage(MessageType.INFO_NAME_FORMAT, clan.getName()));
				user.sendFormattedMessage(config.getMessage(MessageType.INFO_MOTD_FORMAT, clan.getMOTD()));
				user.sendFormattedMessage(config.getMessage(MessageType.INFO_STARS_FORMAT, clan.getStars()));
				user.sendFormattedMessage(config.getMessage(MessageType.INFO_MEMBER_FORMAT, getMemberNamesFormatted(members)));
			}
		}.runTaskAsynchronously(plugin);
	}
	
	private String getMemberNamesFormatted(List<ClanMember> members) {
		StringBuilder names = new StringBuilder();
		Iterator<ClanMember> iterator = members.iterator();
		
		while (iterator.hasNext()) {
			ClanMember member = iterator.next();
			
			names.append(new String(new char[member.getRole().getWeight() - 1]).replace('\0', '*'));
			
			if (member.getTag() != null && member.getTag().length() > 0)
				names.append(member.getTag());
			
			names.append(member.getName());
			
			if (iterator.hasNext())
				names.append(", ");
		}
		
		return names.toString();
	}
}