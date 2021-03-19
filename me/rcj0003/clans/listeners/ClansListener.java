package me.rcj0003.clans.listeners;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.rcj0003.clans.config.MessageConfiguration;
import me.rcj0003.clans.config.MessageType;
import me.rcj0003.clans.events.ClanCreateEvent;
import me.rcj0003.clans.events.ClanDemoteEvent;
import me.rcj0003.clans.events.ClanDisbandEvent;
import me.rcj0003.clans.events.ClanInviteEvent;
import me.rcj0003.clans.events.ClanJoinEvent;
import me.rcj0003.clans.events.ClanLeaveEvent;
import me.rcj0003.clans.events.ClanLeaveEvent.LeaveReason;
import me.rcj0003.clans.events.ClanPromoteEvent;
import me.rcj0003.clans.events.ClanRevokeInviteEvent;
import me.rcj0003.clans.group.Clan;
import me.rcj0003.clans.group.ClanMember;
import me.rcj0003.clans.group.ClanService;
import me.rcj0003.clans.utils.Utils.StringUtils;
import net.md_5.bungee.api.ChatColor;

public class ClansListener implements Listener {
	private MessageConfiguration config;
	private ClanService clanService;
	
	public ClansListener(ClanService clanService, MessageConfiguration config) {
		this.config = config;
		this.clanService = clanService;
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		ClanMember clanMember = clanService.getClanMember(e.getPlayer());
		
		if (clanMember.isChatActive()) {
			Clan clan = clanService.getClanById(clanMember.getClanId());
			String message = ChatColor.stripColor(StringUtils.convertColorCodes(e.getMessage()));
			
			if (message.length() > 0) {
				String formattedMessage = config.getMessage(MessageType.CLAN_CHAT, message);
				clan.message(clanService, formattedMessage);
				Bukkit.getLogger().log(Level.ALL, String.format("[Clan %s] ", clan.getUniqueId()) + formattedMessage);
			}
			
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onCreate(ClanCreateEvent e) {
		e.getClan().message(clanService, config.getMessage(MessageType.CREATE_CLAN));
	}
	
	@EventHandler
	public void onDisband(ClanDisbandEvent e) {
		e.getClan().message(clanService, config.getMessage(MessageType.DISBAND_CLAN));
	}
	
	@EventHandler
	public void onJoin(ClanJoinEvent e) {
		
	}
	
	@EventHandler
	public void onLeave(ClanLeaveEvent e) {
		if (e.getReason() == LeaveReason.KICK) {
			e.getClan().message(clanService, config.getMessage(MessageType.KICK_ANNOUNCEMENT), e.getMember().getName());
		} else {
			e.getClan().message(clanService, config.getMessage(MessageType.LEAVE_CLAN), e.getMember().getName());
		}
	}
	
	@EventHandler
	public void onPromote(ClanPromoteEvent e) {
	}
	
	@EventHandler
	public void onDemote(ClanDemoteEvent e) {
	}
	
	@EventHandler
	public void onInvite(ClanInviteEvent e) {
	}
	
	@EventHandler
	public void onRevokeInvite(ClanRevokeInviteEvent e) {
	}
}