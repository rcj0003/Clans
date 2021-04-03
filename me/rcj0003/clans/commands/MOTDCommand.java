package me.rcj0003.clans.commands;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.rcj0003.clans.config.MessageConfiguration;
import me.rcj0003.clans.config.MessageType;
import me.rcj0003.clans.group.Clan;
import me.rcj0003.clans.group.ClanMember;
import me.rcj0003.clans.group.ClanPerk;
import me.rcj0003.clans.group.ClanRole;
import me.rcj0003.clans.group.ClanService;
import me.rcj0003.clans.group.exceptions.ClanDoesNotExistException;
import me.rcj0003.clans.utils.command.CommandUser;
import me.rcj0003.clans.utils.command.SubCommand;
import me.rcj0003.clans.utils.command.exceptions.InvalidArgumentException;

public class MOTDCommand implements SubCommand {
	private static final String[] DESCRIPTION = new String[] { "Sets the MOTD for your clan (if you've unlocked the perk)." };
	private Plugin plugin;
	private ClanService clanService;
	private MessageConfiguration config;
	
	public MOTDCommand(Plugin plugin, ClanService clanService, MessageConfiguration config) {
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
		return "motd";
	}

	public String[] getDescription() {
		return DESCRIPTION;
	}

	public String getUsage() {
		return "[MOTD]";
	}

	public void execute(CommandUser user, String[] arguments) throws InvalidArgumentException {
		Clan clan = clanService.getClanForPlayer(user.getPlayer());

		if (clan == null) {
			user.sendFormattedMessage(config.getMessage(MessageType.NOT_IN_CLAN_ERROR));
			return;
		}
		
		if (!clan.hasPerk(ClanPerk.MOTD)) {
			user.sendFormattedMessage(config.getMessage(MessageType.MOTD_NOT_UNLOCKED));
			return;
		}
		
		ClanMember member = clanService.getClanMember(user.getPlayer());
		
		if (!member.getRole().hasHigherWeightThan(ClanRole.MEMBER)) {
			user.sendFormattedMessage(config.getMessage(MessageType.HIGHER_ROLE_REQUIRED));
			return;
		}
		
		String motd = String.join(" ", arguments);
		
		if (motd.length() > 64) {
			user.sendFormattedMessage(config.getMessage(MessageType.MOTD_TOO_LONG));
			return;
		}
		
		clan.setMOTD(motd);
		
		new BukkitRunnable() {
			public void run() {
				clan.update();
			}
		}.runTaskAsynchronously(plugin);
	}
}
