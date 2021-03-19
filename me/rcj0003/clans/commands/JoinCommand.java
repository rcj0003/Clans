package me.rcj0003.clans.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.rcj0003.clans.config.MessageConfiguration;
import me.rcj0003.clans.events.ClanJoinEvent;
import me.rcj0003.clans.group.Clan;
import me.rcj0003.clans.group.ClanMember;
import me.rcj0003.clans.group.ClanRole;
import me.rcj0003.clans.group.ClanService;
import me.rcj0003.clans.group.exceptions.ClanDoesNotExistException;
import me.rcj0003.clans.utils.command.CommandUser;
import me.rcj0003.clans.utils.command.SubCommand;
import me.rcj0003.clans.utils.command.exceptions.InvalidArgumentException;

public class JoinCommand implements SubCommand {
	private static final String[] DESCRIPTION = new String[] { "Joins a clan. You can specify a players name to join their clan", "or specify a Clan ID." };
	private Plugin plugin;
	private ClanService clanService;
	private MessageConfiguration config;
	
	public JoinCommand(ClanService clanService, MessageConfiguration config) {
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
		return "join";
	}

	public String[] getDescription() {
		return DESCRIPTION;
	}

	public String getUsage() {
		return "[Clan ID or Username]";
	}
	
	private Clan getClan(String argument) {
		try {
			try {
				UUID clanId = UUID.fromString(argument);
				return clanService.getClanById(clanId);
			}
			catch (Exception e) {
				Player player = Bukkit.getPlayer(argument);
				return clanService.getClanForPlayer(player);
			}
		}
		catch (ClanDoesNotExistException e2) {
			return null;
		}
	}

	public void execute(CommandUser user, String[] arguments) throws InvalidArgumentException {
		new BukkitRunnable() {
			public void run() {
				Clan clan = getClan(arguments[0]);
				ClanMember member = clanService.getClanMember(user.getPlayer());
				
				if (member.getClanId() != null) {
					return;
				}
				
				if (!clan.isInvited(member.getUniqueId())) {
					return;
				}
				
				member.setRole(ClanRole.MEMBER).setClanId(clan.getUniqueId());
				clan.revokeInvite(member.getUniqueId());
				Bukkit.getPluginManager().callEvent(new ClanJoinEvent(clan, member));
				
				member.update();
				clan.update();
			}
		}.runTaskAsynchronously(plugin);
	}
}
