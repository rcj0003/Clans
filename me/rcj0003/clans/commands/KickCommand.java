package me.rcj0003.clans.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.rcj0003.clans.config.MessageConfiguration;
import me.rcj0003.clans.events.ClanLeaveEvent;
import me.rcj0003.clans.events.ClanLeaveEvent.LeaveReason;
import me.rcj0003.clans.group.Clan;
import me.rcj0003.clans.group.ClanMember;
import me.rcj0003.clans.group.ClanService;
import me.rcj0003.clans.utils.command.CommandUser;
import me.rcj0003.clans.utils.command.SubCommand;
import me.rcj0003.clans.utils.command.arguments.Argument;
import me.rcj0003.clans.utils.command.arguments.ArgumentSubCommand;
import me.rcj0003.clans.utils.command.arguments.PlayerArgument;
import me.rcj0003.clans.utils.command.exceptions.InvalidArgumentException;

public class KickCommand extends ArgumentSubCommand {
	private static final String[] DESCRIPTION = new String[] { "Kick members with a lower role than you have." };
	private static final Argument<?>[] arguments = new Argument<?>[] { new PlayerArgument() };
	private Plugin plugin;
	private ClanService clanService;
	private MessageConfiguration config;
	
	public KickCommand(Plugin plugin, ClanService clanService, MessageConfiguration config) {
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
		return "kick";
	}

	public String[] getDescription() {
		return DESCRIPTION;
	}

	public String getUsage() {
		return "[Player]";
	}

	public Argument<?>[] getArgumentTypes() {
		return null;
	}

	public void executeVerified(CommandUser user, Object[] values) {
		Player player = (Player) values[0];
		
		new BukkitRunnable() {
			public void run() {
				Clan clan = clanService.getClanForPlayer(player);
				ClanMember member = clanService.getClanMember(player);
				
				Bukkit.getPluginManager().callEvent(new ClanLeaveEvent(clan, member, LeaveReason.KICK));
				
				clan.removeMember(player.getUniqueId());
				member.setClanId(null).update();
			}
		}.runTaskAsynchronously(plugin);
	}
}
