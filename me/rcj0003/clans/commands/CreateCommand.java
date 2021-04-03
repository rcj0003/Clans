package me.rcj0003.clans.commands;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.rcj0003.clans.config.MessageConfiguration;
import me.rcj0003.clans.config.MessageType;
import me.rcj0003.clans.group.Clan;
import me.rcj0003.clans.group.ClanService;
import me.rcj0003.clans.utils.command.CommandUser;
import me.rcj0003.clans.utils.command.SubCommand;
import me.rcj0003.clans.utils.command.exceptions.InvalidArgumentException;

public class CreateCommand implements SubCommand {
	private static final String[] DESCRIPTION = new String[] { "Creates a clan. You must specify a name for your clan." };
	private Plugin plugin;
	private ClanService clanService;
	private MessageConfiguration config;
	
	public CreateCommand(Plugin plugin, ClanService clanService, MessageConfiguration config) {
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
		return "create";
	}

	public String[] getDescription() {
		return DESCRIPTION;
	}

	public String getUsage() {
		return "[Clan Name]";
	}

	public void execute(CommandUser user, String[] arguments) throws InvalidArgumentException {
		Clan clan = clanService.getClanForPlayer(user.getPlayer());
		
		if (clan != null) {
			user.sendFormattedMessage(config.getMessage(MessageType.IN_CLAN_ERROR));
		} else {
			new BukkitRunnable() {
				public void run() {
					clanService.createClan(user.getPlayer(), arguments[0]);
				}
			}.runTaskAsynchronously(plugin);
		}
	}
}