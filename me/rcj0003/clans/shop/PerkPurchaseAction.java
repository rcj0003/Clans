package me.rcj0003.clans.shop;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.rcj0003.clans.events.ClanGivePerkEvent;
import me.rcj0003.clans.group.Clan;
import me.rcj0003.clans.group.ClanMember;
import me.rcj0003.clans.group.ClanPerk;
import me.rcj0003.clans.group.ClanService;

public class PerkPurchaseAction implements PurchaseAction {
	private Plugin plugin;
	private ClanService clanService;
	private ClanPerk perk;
	private int price;
	
	public PerkPurchaseAction(Plugin plugin, ClanService clanService, ClanPerk perk, int price) {
		this.plugin = plugin;
		this.clanService = clanService;
		this.perk = perk;
		this.price = price;
	}
	
	public void purchase(Player player) {
		Clan clan = clanService.getClanForPlayer(player);
		
		new BukkitRunnable() {
			public void run() {
				clanService.getClanMember(player).modifyCurrency(-getPrice(player)).update();
				clan.addPerk(perk).update();
				Bukkit.getPluginManager().callEvent(new ClanGivePerkEvent(clan, perk));
			}
		}.runTaskAsynchronously(plugin);
	}

	public boolean canPurchase(Player player) {
		ClanMember member = clanService.getClanMember(player);
		
		if (member.getClanId() == null || member.getCurrency() < price)
			return false;
		
		return member.getCurrency() > getPrice(player) && !clanService.getClanForPlayer(player).hasPerk(perk);
	}

	public int getPrice(Player player) {
		return price;
	}
}