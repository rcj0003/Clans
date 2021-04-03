package me.rcj0003.clans.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.rcj0003.clans.config.MessageConfiguration;
import me.rcj0003.clans.config.MessageType;
import me.rcj0003.clans.events.ClanPromoteEvent;
import me.rcj0003.clans.group.Clan;
import me.rcj0003.clans.group.ClanMember;
import me.rcj0003.clans.group.ClanRole;
import me.rcj0003.clans.group.ClanService;
import me.rcj0003.clans.utils.command.CommandUser;
import me.rcj0003.clans.utils.command.arguments.Argument;
import me.rcj0003.clans.utils.command.arguments.ArgumentSubCommand;
import me.rcj0003.clans.utils.command.arguments.PlayerArgument;

public class PromoteCommand extends ArgumentSubCommand {
	private static final String[] DESCRIPTION = new String[] { "Promotes a member of your clan." };
	private static final Argument<?>[] arguments = new Argument<?>[] { new PlayerArgument() };
	private Plugin plugin;
	private ClanService clanService;
	private MessageConfiguration config;
	
	public PromoteCommand(Plugin plugin, ClanService clanService, MessageConfiguration config) {
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
		return "promote";
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

	@Override
	public void executeVerified(CommandUser user, Object[] values) {
		OfflinePlayer player = (OfflinePlayer) values[0];
		
		if (player.getUniqueId() == user.getPlayer().getUniqueId()) {
			user.sendFormattedMessage(config.getMessage(MessageType.CANT_DO_ON_SELF));
			return;
		}
		
		Clan clan = clanService.getClanForPlayer(user.getPlayer());

		if (clan == null) {
			user.sendFormattedMessage(config.getMessage(MessageType.NOT_IN_CLAN_ERROR));
			return;
		}
		
		if (!clan.getMembers().contains(player.getUniqueId())) {
			user.sendFormattedMessage(config.getMessage(MessageType.MEMBER_NOT_IN_CLAN, player.getName()));
			return;
		}
		
		ClanMember promotee = clanService.getClanMember(player.getUniqueId());
		ClanMember you = clanService.getClanMember(user.getPlayer());
		
		if (!you.getRole().hasHigherWeightThan(promotee.getRole())) {
			user.sendFormattedMessage(config.getMessage(MessageType.HIGHER_ROLE_REQUIRED));
			return;
		}
		
		ClanRole newRole = promotee.getRole().getNextHigherRole();
		
		if (newRole == ClanRole.LEADER) {
			user.sendFormattedMessage(config.getMessage(MessageType.CANT_PROMOTE));
			return;
		}
		
		Bukkit.getPluginManager().callEvent(new ClanPromoteEvent(clan, promotee, promotee.getRole(), newRole));
		
		new BukkitRunnable() {
			public void run() {
				promotee.setRole(newRole).update();
			}
		}.runTaskAsynchronously(plugin);
	}
}
