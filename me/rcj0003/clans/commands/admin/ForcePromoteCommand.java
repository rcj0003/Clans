package me.rcj0003.clans.commands.admin;

import org.bukkit.Bukkit;
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

public class ForcePromoteCommand extends ArgumentSubCommand {
	private static final String[] DESCRIPTION = new String[] { "Force promotes a player to next available role." };
	private static final Argument<?>[] arguments = new Argument<?>[] { new PlayerArgument() };
	private Plugin plugin;
	private ClanService clanService;
	private MessageConfiguration config;
	
	public ForcePromoteCommand(Plugin plugin, ClanService clanService, MessageConfiguration config) {
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
		return "forcepromote";
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
		Player player = (Player) values[0];
		
		new BukkitRunnable() {
			public void run() {
				Clan clan = clanService.getClanForPlayer(user.getPlayer());
				
				if (!clan.getMembers().contains(player.getUniqueId())) {
					user.sendFormattedMessage(config.getMessage(MessageType.MEMBER_NOT_IN_CLAN, player.getName()));
					return;
				}
				
				ClanMember promotee = clanService.getClanMember(player);
				
				ClanRole newRole = promotee.getRole().getNextHigherRole();
				Bukkit.getPluginManager().callEvent(new ClanPromoteEvent(clan, promotee, promotee.getRole(), newRole));
				
				promotee.setRole(newRole).update();
			}
		}.runTaskAsynchronously(plugin);
	}
}
