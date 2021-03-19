package me.rcj0003.clans.commands.admin;

import java.util.UUID;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.rcj0003.clans.config.MessageConfiguration;
import me.rcj0003.clans.group.Clan;
import me.rcj0003.clans.group.ClanService;
import me.rcj0003.clans.utils.command.CommandUser;
import me.rcj0003.clans.utils.command.arguments.Argument;
import me.rcj0003.clans.utils.command.arguments.ArgumentSubCommand;
import me.rcj0003.clans.utils.command.arguments.IntegerArgument;
import me.rcj0003.clans.utils.command.arguments.UUIDArgument;

public class SetCapacityCommand extends ArgumentSubCommand {
	private static final String[] DESCRIPTION = new String[] { "Sets clan capacity." };
	private static final Argument<?>[] arguments = new Argument<?>[] { new UUIDArgument(), new IntegerArgument(true) };
	private Plugin plugin;
	private ClanService clanService;
	private MessageConfiguration config;
	
	public SetCapacityCommand(Plugin plugin, ClanService clanService, MessageConfiguration config) {
		this.plugin = plugin;
		this.clanService = clanService;
		this.config = config;
	}
	
	public boolean getRequiresPlayer() {
		return false;
	}

	public int getMinimumArguments() {
		return 2;
	}

	public String getName() {
		return "setcapacity";
	}

	public String[] getDescription() {
		return DESCRIPTION;
	}

	public String getUsage() {
		return "[Clan ID] [Capacity]";
	}

	public Argument<?>[] getArgumentTypes() {
		return arguments;
	}

	public void executeVerified(CommandUser user, Object[] values) {
		UUID clanId = (UUID) values[0];
		Integer capacity = (Integer) values[1];
		
		new BukkitRunnable() {
			public void run() {
				Clan clan = clanService.getClanById(clanId);
				
				if (capacity < clan.getMemberCount()) {
					user.sendFormattedMessage();
					return;
				}
				
				clan.setCapacity(capacity).message(clanService).update();
			}
		}.runTaskAsynchronously(plugin);
	}
}