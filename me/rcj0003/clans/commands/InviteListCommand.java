package me.rcj0003.clans.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.rcj0003.clans.config.MessageConfiguration;
import me.rcj0003.clans.config.MessageType;
import me.rcj0003.clans.group.Clan;
import me.rcj0003.clans.group.ClanMember;
import me.rcj0003.clans.group.ClanRole;
import me.rcj0003.clans.group.ClanService;
import me.rcj0003.clans.group.exceptions.ClanDoesNotExistException;
import me.rcj0003.clans.utils.MessageBuilder;
import me.rcj0003.clans.utils.command.CommandUser;
import me.rcj0003.clans.utils.command.SubCommand;
import me.rcj0003.clans.utils.command.exceptions.InvalidArgumentException;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;

public class InviteListCommand implements SubCommand {
	private static final String[] DESCRIPTION = new String[] { "Lists out current active invites." };
	private ClanService clanService;
	private MessageConfiguration config;

	public InviteListCommand(ClanService clanService, MessageConfiguration config) {
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
		return "invitelist";
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

		if (!member.getRole().hasHigherWeightThan(ClanRole.OFFICER)) {
			user.sendFormattedMessage(config.getMessage(MessageType.HIGHER_ROLE_REQUIRED));
			return;
		}

		user.sendFormattedMessage(config.getMessage(MessageType.INVITE_LIST_HEADER));

		for (UUID id : clan.getInvited()) {
			OfflinePlayer player = Bukkit.getOfflinePlayer(id);
			MessageBuilder message = new MessageBuilder();
			user.getPlayer().spigot()
					.sendMessage(message.addText(config.getMessage(MessageType.INVITE_LIST_FORMAT, player.getName()))
							.addEvent(new ClickEvent(Action.RUN_COMMAND, "/clans revokeinvite " + player.getName()))
							.build());
		}
	}
}