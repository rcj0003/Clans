package me.rcj0003.clans.commands.admin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.rcj0003.clans.config.MessageConfiguration;
import me.rcj0003.clans.group.ClanMember;
import me.rcj0003.clans.group.ClanService;
import me.rcj0003.clans.utils.command.CommandUser;
import me.rcj0003.clans.utils.command.arguments.Argument;
import me.rcj0003.clans.utils.command.arguments.ArgumentSubCommand;
import me.rcj0003.clans.utils.command.arguments.IntegerArgument;
import me.rcj0003.clans.utils.command.arguments.PlayerArgument;

public class GiveCurrencyCommand extends ArgumentSubCommand {
	private static final String[] DESCRIPTION = new String[] { "Gives/takes player currency." };
	private static final Argument<?>[] arguments = new Argument<?>[] { new PlayerArgument(), new IntegerArgument(false) };
	private Plugin plugin;
	private ClanService clanService;
	private MessageConfiguration config;
	
	public GiveCurrencyCommand(Plugin plugin, ClanService clanService, MessageConfiguration config) {
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
		return "givecurrency";
	}

	public String[] getDescription() {
		return DESCRIPTION;
	}

	public String getUsage() {
		return "[Player] [Integer Amount]";
	}
	
	public Argument<?>[] getArgumentTypes() {
		return arguments;
	}

	public void executeVerified(CommandUser user, Object[] arguments) {
		Player player = (Player) arguments[0];
		Integer value = (Integer) arguments[1];
		
		new BukkitRunnable() {
			public void run() {
				ClanMember member = clanService.getClanMember(player);
				member.setCurrency(member.getCurrency() + value);
				
				if (member.isOnline())
					member.message();
				
				user.sendFormattedMessage();
				
				member.update();
			}
		}.runTaskAsynchronously(plugin);
	}
}