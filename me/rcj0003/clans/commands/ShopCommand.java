package me.rcj0003.clans.commands;

import java.util.Map;

import me.rcj0003.clans.config.MessageConfiguration;
import me.rcj0003.clans.config.MessageType;
import me.rcj0003.clans.group.ClanMember;
import me.rcj0003.clans.group.ClanService;
import me.rcj0003.clans.shop.ShopItem;
import me.rcj0003.clans.shop.gui.ShopGui;
import me.rcj0003.clans.utils.command.CommandUser;
import me.rcj0003.clans.utils.command.SubCommand;
import me.rcj0003.clans.utils.command.exceptions.InvalidArgumentException;
import me.rcj0003.clans.utils.gui.GuiHandler;

public class ShopCommand implements SubCommand {
	private MessageConfiguration config;
	private ClanService clanService;
	private GuiHandler guiHandler;
	private Map<Integer, ShopItem> items;
	
	public ShopCommand(ClanService clanService, MessageConfiguration config, GuiHandler guiHandler, Map<Integer, ShopItem> items) {
		this.config = config;
		this.clanService = clanService;
		this.guiHandler = guiHandler;
		this.items = items;
	}
	
	public boolean getRequiresPlayer() {
		return true;
	}

	public int getMinimumArguments() {
		return 0;
	}

	public String getName() {
		return "shop";
	}

	public String[] getDescription() {
		return null;
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
		
		guiHandler.openGui(user.getPlayer(), new ShopGui(user.getPlayer(), guiHandler, items));
	}
}