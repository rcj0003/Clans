package me.rcj0003.clans.commands;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.rcj0003.clans.config.MessageConfiguration;
import me.rcj0003.clans.config.MessageType;
import me.rcj0003.clans.group.Clan;
import me.rcj0003.clans.group.ClanMember;
import me.rcj0003.clans.group.ClanRole;
import me.rcj0003.clans.group.ClanService;
import me.rcj0003.clans.utils.MessageBuilder;
import me.rcj0003.clans.utils.command.CommandUser;
import me.rcj0003.clans.utils.command.SubCommand;
import me.rcj0003.clans.utils.command.exceptions.InvalidArgumentException;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;

public class DisbandCommand implements SubCommand {
	private static final String[] DESCRIPTION = new String[] { "Disbands your clan. Commands requires confirmation." };
	private Plugin plugin;
	private ClanService clanService;
	private MessageConfiguration config;

	public DisbandCommand(Plugin plugin, ClanService clanService, MessageConfiguration config) {
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
		return "disband";
	}

	public String[] getDescription() {
		return DESCRIPTION;
	}

	public String getUsage() {
		return "";
	}

	public void execute(CommandUser user, String[] arguments) throws InvalidArgumentException {
		Clan clan = clanService.getClanForPlayer(user.getPlayer());

		if (clan == null) {
			user.sendFormattedMessage(config.getMessage(MessageType.NOT_IN_CLAN_ERROR));
			return;
		}
		
		ClanMember member = clanService.getClanMember(user.getPlayer());
		
		if (member.getRole() != ClanRole.LEADER) {
			user.sendFormattedMessage(config.getMessage(MessageType.HIGHER_ROLE_REQUIRED));
			return;
		}
		
		if (arguments.length > 0 && arguments[0].equalsIgnoreCase("--confirm")) {
			new BukkitRunnable() {
				public void run() {
					clanService.deleteClan(clan);
				}
			}.runTaskAsynchronously(plugin);
		} else {
			user.sendFormattedMessage(config.getMessage(MessageType.DISBAND_CLAN_CONFIRM));
			MessageBuilder message = new MessageBuilder();
			message.addText(false, config.getMessage(MessageType.CLICK_TO_ACCEPT, "Confirm"));
			user.getPlayer().spigot().sendMessage(new BaseComponent[] { message
					.addEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clans disband --confirm")).build() });
		}
	}
}