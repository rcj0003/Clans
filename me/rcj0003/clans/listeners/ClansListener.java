package me.rcj0003.clans.listeners;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

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
	public void onPlayerJoin(PlayerJoinEvent e) {
		clanService.preloadData(e.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		ClanMember clanMember = clanService.getClanMember(e.getPlayer());
		
		if (clanMember.isChatActive() && clanMember.getClanId() != null) {
			Clan clan = clanService.getClanById(clanMember.getClanId());
			String message = ChatColor.stripColor(StringUtils.convertColorCodes(e.getMessage()));
			
			if (message.length() > 0) {
				String formattedMessage = config.getMessage(MessageType.CLAN_CHAT, clanMember.getTag() == null || clanMember.getTag().length() == 0 ? "" : clanMember.getTag() + " ", e.getPlayer().getName(), message).replace("  ", " ");
				clan.message(formattedMessage);
				Bukkit.getLogger().log(Level.ALL, String.format("[Clan %s] ", clan.getUniqueId()) + formattedMessage);
			}
			
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onCreate(ClanCreateEvent e) {
		e.getClan().message(config.getMessage(MessageType.CREATE_CLAN));
	}
	
	@EventHandler
	public void onDisband(ClanDisbandEvent e) {
		e.getClan().message(config.getMessage(MessageType.DISBAND_CLAN));
	}
	
	@EventHandler
	public void onJoin(ClanJoinEvent e) {
		e.getClan().message(config.getMessage(MessageType.JOIN_CLAN, e.getMember().getName()));
	}
	
	@EventHandler
	public void onLeave(ClanLeaveEvent e) {
		if (e.getReason() == LeaveReason.KICK) {
			e.getClan().message(config.getMessage(MessageType.KICK_ANNOUNCEMENT, e.getMember().getName()));
		} else {
			e.getClan().message(config.getMessage(MessageType.LEAVE_CLAN, e.getMember().getName()));
		}
	}
	
	@EventHandler
	public void onPromote(ClanPromoteEvent e) {
		e.getClan().message(config.getMessage(MessageType.PROMOTE, e.getMember().getName()));
	}
	
	@EventHandler
	public void onDemote(ClanDemoteEvent e) {
		e.getClan().message(config.getMessage(MessageType.DEMOTE, e.getMember().getName()));
	}
	
	@EventHandler
	public void onInvite(ClanInviteEvent e) {
		e.getClan().message(config.getMessage(MessageType.INVITE_ANNOUNCEMENT, Bukkit.getOfflinePlayer(e.getInvited()).getName()));
	}
	
	@EventHandler
	public void onRevokeInvite(ClanRevokeInviteEvent e) {
		e.getClan().message(config.getMessage(MessageType.REVOKE_INVITE, Bukkit.getOfflinePlayer(e.getInvited()).getName()));
	}
}