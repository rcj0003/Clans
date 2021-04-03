package me.rcj0003.clans.listeners;

import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import me.packetslayer.annihilation.event.game.GameEndEvent;
import me.packetslayer.annihilation.event.player.PlayerKillEvent;
import me.rcj0003.clans.config.MessageConfiguration;
import me.rcj0003.clans.config.MessageType;
import me.rcj0003.clans.group.Clan;
import me.rcj0003.clans.group.ClanMember;
import me.rcj0003.clans.group.ClanService;

public class AnniListener implements Listener {
	private ClanService clanService;
	private MessageConfiguration config;
	
	public AnniListener(ClanService clanService, MessageConfiguration config) {
		this.clanService = clanService;
		this.config = config;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onKill(PlayerKillEvent e) {
		Player killer = e.getKiller();
		ClanMember member = clanService.getClanMember(killer);
		
		if (member.getClanId() == null)
			return;
		
		Clan clan = clanService.getClanForPlayer(killer);
		
		member.setCurrency(member.getCurrency() + 2);
		member.message(config.getMessage(MessageType.AWARD_CURRENCY, 2));
		clan.modifyStars(2);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onNexusBreak(BlockBreakEvent e){
		if(!e.isCancelled() || e.getBlock().getType().equals(Material.ENDER_STONE)){
			if (e.getBlock().getType().equals(Material.ENDER_STONE)){
				ClanMember member = clanService.getClanMember(e.getPlayer());
				
				if (member.getClanId() == null)
					return;
				
				Clan clan = clanService.getClanForPlayer(e.getPlayer());
				member.setCurrency(member.getCurrency() + 2).message(config.getMessage(MessageType.AWARD_CURRENCY, 2));
				clan.modifyStars(2);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onNexusBreak(GameEndEvent e){
		Set<ClanMember> winners = e.getWinner().getOnlinePlayers().stream().map(p -> clanService.getClanMember(p.getUniqueId())).collect(Collectors.toSet());
		Set<Clan> winningClans = e.getWinner().getOnlinePlayers().stream().map(p -> clanService.getClanForPlayer(p.getUniqueId())).collect(Collectors.toSet());
		
		for (ClanMember member : winners) {
			if (member.getClanId() == null)
				continue;
			member.setCurrency(member.getCurrency() + 100).message(config.getMessage(MessageType.AWARD_CURRENCY, 100));
		}
			
		for (Clan clan : winningClans)
			clan.modifyStars(100);
	}
}