package me.rcj0003.clans.commands;

import me.rcj0003.clans.config.MessageConfiguration;
import me.rcj0003.clans.config.MessageType;
import me.rcj0003.clans.group.ClanMember;
import me.rcj0003.clans.group.ClanService;
import me.rcj0003.clans.utils.command.CommandUser;
import me.rcj0003.clans.utils.command.SubCommand;
import me.rcj0003.clans.utils.command.exceptions.InvalidArgumentException;

public class ToggleChatCommand implements SubCommand {
	private static final String[] DESCRIPTION = new String[] { "Toggles clan chat." };
	private ClanService clanService;
	private MessageConfiguration config;
	
	public ToggleChatCommand(ClanService clanService, MessageConfiguration config) {
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
		return "togglechat";
	}

	public String[] getDescription() {
		return DESCRIPTION;
	}

	public String getUsage() {
		return "";
	}

	public void execute(CommandUser user, String[] arguments) throws InvalidArgumentException {
		ClanMember member = clanService.getClanMember(user.getPlayer());
		
		if (member.getClanId() == null) {
			user.sendFormattedMessage(config.getMessage(MessageType.NOT_IN_CLAN_ERROR));
			return;
		}
		
		member.setChatActive(!member.isChatActive());
		
		if (member.isChatActive()) {
			user.sendFormattedMessage(config.getMessage(MessageType.TOGGLE_CHAT_ON));
		} else {
			user.sendFormattedMessage(config.getMessage(MessageType.TOGGLE_CHAT_OFF));
		}
	}
}