package me.rcj0003.clans.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
import me.rcj0003.clans.utils.command.SubCommand;
import me.rcj0003.clans.utils.command.exceptions.InvalidArgumentException;

public class LeaveCommand implements SubCommand {
	private static final String[] DESCRIPTION = new String[] { "Leaves a clan." };
	private Plugin plugin;
	private ClanService clanService;
	private MessageConfiguration config;
	
	public LeaveCommand(Plugin plugin, ClanService clanService, MessageConfiguration config) {
		this.plugin = plugin;
		this.clanService = clanService;
		this.config = config;
	}
	
	public boolean getRequiresPlayer() {
		return true;
	}

	public int getMinimumArguments() {
		return 0;
	}

	public String getName() {
		return "leave";
	}

	public String[] getDescription() {
		return DESCRIPTION;
	}

	public String getUsage() {
		return "";
	}

	public void execute(CommandUser user, String[] arguments) throws InvalidArgumentException {
		Player player = user.getPlayer();
		ClanMember member = clanService.getClanMember(player);
		
		if (member.getRole() == ClanRole.LEADER) {
			user.sendFormattedMessage(config.getMessage(MessageType.TRANSFER_REQUIRED));
			return;
		}
		
		new BukkitRunnable() {
			public void run() {
				Clan clan = clanService.getClanForPlayer(player);
				Bukkit.getPluginManager().callEvent(new ClanLeaveEvent(clan, member, LeaveReason.LEAVE));
				clan.removeMember(player.getUniqueId());
				member.setClanId(null).update();
			}
		}.runTaskAsynchronously(plugin);
	}
}
