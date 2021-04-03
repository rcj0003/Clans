package me.rcj0003.clans.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.rcj0003.clans.config.MessageConfiguration;
import me.rcj0003.clans.config.MessageType;
import me.rcj0003.clans.group.ClanResults;
import me.rcj0003.clans.group.ClanService;
import me.rcj0003.clans.utils.MessageBuilder;
import me.rcj0003.clans.utils.command.CommandUser;
import me.rcj0003.clans.utils.command.SubCommand;
import me.rcj0003.clans.utils.command.exceptions.InvalidArgumentException;

public class TopCommand implements SubCommand {
	private static final String[] DESCRIPTION = new String[] { "Gets the list of top 10 clans." };
	private Plugin plugin;
	private ClanService clanService;
	private MessageConfiguration config;
	
	public TopCommand(Plugin plugin, ClanService clanService, MessageConfiguration config) {
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
		return "top";
	}

	public String[] getDescription() {
		return DESCRIPTION;
	}

	public String getUsage() {
		return "";
	}

	public void execute(CommandUser user, String[] arguments) throws InvalidArgumentException {
		List<ClanResults> results = clanService.getLeaderboard();
		user.sendFormattedMessage(config.getMessage(MessageType.LOADING));
		
		new BukkitRunnable() {
			public void run() {
				MessageBuilder message = new MessageBuilder();
				message.addText(config.getMessage(MessageType.LEADERBOARD_HEADER));
				
				for (ClanResults result : results) {
					message.addText(config.getMessage(MessageType.LEADERBOARD_FORMAT, result.getStars(), result.getName(), Bukkit.getPlayer(result.getLeaderId()).getName()));
				}
				
				user.getPlayer().spigot().sendMessage(message.build());
			}
		}.runTaskAsynchronously(plugin);
	}
}