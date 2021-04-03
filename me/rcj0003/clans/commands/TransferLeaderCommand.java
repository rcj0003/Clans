package me.rcj0003.clans.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.rcj0003.clans.config.MessageConfiguration;
import me.rcj0003.clans.config.MessageType;
import me.rcj0003.clans.events.ClanLeaveEvent;
import me.rcj0003.clans.events.ClanLeaveEvent.LeaveReason;
import me.rcj0003.clans.group.Clan;
import me.rcj0003.clans.group.ClanMember;
import me.rcj0003.clans.group.ClanRole;
import me.rcj0003.clans.group.ClanService;
import me.rcj0003.clans.utils.command.CommandUser;
import me.rcj0003.clans.utils.command.arguments.Argument;
import me.rcj0003.clans.utils.command.arguments.ArgumentSubCommand;
import me.rcj0003.clans.utils.command.arguments.PlayerArgument;

public class TransferLeaderCommand extends ArgumentSubCommand {
	private static final String[] DESCRIPTION = new String[] { "Transfers leadership to other player." };
	private static final Argument<?>[] arguments = new Argument<?>[] { new PlayerArgument() };
	private Plugin plugin;
	private ClanService clanService;
	private MessageConfiguration config;
	
	public TransferLeaderCommand(Plugin plugin, ClanService clanService, MessageConfiguration config) {
		this.plugin = plugin;
		this.clanService = clanService;
		this.config = config;
	}
	
	public boolean getRequiresPlayer() {
		return true;
	}

	public int getMinimumArguments() {
		return 1;
	}

	public String getName() {
		return "transferleader";
	}

	public String[] getDescription() {
		return DESCRIPTION;
	}

	public String getUsage() {
		return "[Player]";
	}

	public Argument<?>[] getArgumentTypes() {
		return arguments;
	}

	public void executeVerified(CommandUser user, Object[] values) {
		Clan clan = clanService.getClanForPlayer(user.getPlayer());

		if (clan == null) {
			user.sendFormattedMessage(config.getMessage(MessageType.NOT_IN_CLAN_ERROR));
			return;
		}
		
		OfflinePlayer player = (OfflinePlayer) values[0];
		
		if (player.getUniqueId() == user.getPlayer().getUniqueId()) {
			user.sendFormattedMessage(config.getMessage(MessageType.CANT_DO_ON_SELF));
			return;
		}
		
		if (!clan.getMembers().contains(player.getUniqueId())) {
			user.sendFormattedMessage(config.getMessage(MessageType.MEMBER_NOT_IN_CLAN));
			return;
		}
		
		new BukkitRunnable() {
			public void run() {
				ClanMember member = clanService.getClanMember(player.getUniqueId());
				ClanMember oldLeader = clanService.getClanMember(user.getPlayer());
				
				member.setRole(ClanRole.LEADER).update();
				oldLeader.setRole(oldLeader.getRole().getNextLowerRole()).update();
				
				clan.message(config.getMessage(MessageType.TRANSFER_LEADERSHIP, member.getName()));
			}
		}.runTaskAsynchronously(plugin);
	}
}
