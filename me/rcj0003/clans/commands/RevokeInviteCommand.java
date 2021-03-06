package me.rcj0003.clans.commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.rcj0003.clans.config.MessageConfiguration;
import me.rcj0003.clans.config.MessageType;
import me.rcj0003.clans.group.Clan;
import me.rcj0003.clans.group.ClanMember;
import me.rcj0003.clans.group.ClanRole;
import me.rcj0003.clans.group.ClanService;
import me.rcj0003.clans.utils.command.CommandUser;
import me.rcj0003.clans.utils.command.arguments.Argument;
import me.rcj0003.clans.utils.command.arguments.ArgumentSubCommand;
import me.rcj0003.clans.utils.command.arguments.PlayerArgument;

public class RevokeInviteCommand extends ArgumentSubCommand {
	private static final String[] DESCRIPTION = new String[] { "Revokes an invite for a player." };
	private static final Argument<?>[] arguments = new Argument<?>[] { new PlayerArgument() };
	private Plugin plugin;
	private ClanService clanService;
	private MessageConfiguration config;
	
	public RevokeInviteCommand(Plugin plugin, ClanService clanService, MessageConfiguration config) {
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
		return "revokeinvite";
	}

	public String[] getDescription() {
		return DESCRIPTION;
	}

	public String getUsage() {
		return "[Username]";
	}

	public Argument<?>[] getArgumentTypes() {
		return arguments;
	}

	public void executeVerified(CommandUser user, Object[] values) {
		ClanMember member = clanService.getClanMember(user.getPlayer());
		
		if (member.getClanId() == null) {
			user.sendFormattedMessage(config.getMessage(MessageType.NOT_IN_CLAN_ERROR));
			return;
		}
		
		if (!member.getRole().hasHigherWeightThan(ClanRole.MEMBER)) {
			user.sendFormattedMessage(config.getMessage(MessageType.HIGHER_ROLE_REQUIRED));
			return;
		}
		
		OfflinePlayer player = (OfflinePlayer) values[0];
		Clan clan = clanService.getClanForPlayer(user.getPlayer());
		clan.revokeInvite(player.getUniqueId());
		
		user.sendFormattedMessage(config.getMessage(MessageType.REVOKE_INVITE, player.getName()));
		
		new BukkitRunnable() {
			public void run() {
				clan.update();
			}
		}.runTaskAsynchronously(plugin);
	}
}
