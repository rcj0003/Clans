package me.rcj0003.clans.shop;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.rcj0003.clans.group.Clan;
import me.rcj0003.clans.group.ClanMember;
import me.rcj0003.clans.group.ClanService;

public class SlotIncreaseAction implements PurchaseAction {
	private Plugin plugin;
	private ClanService clanService;
	
	public SlotIncreaseAction(Plugin plugin, ClanService clanService) {
		this.plugin = plugin;
		this.clanService = clanService;
	}
	
	public boolean canPurchase(Player player) {
		ClanMember member = clanService.getClanMember(player);
		Clan clan = clanService.getClanForPlayer(player);
		return member.getCurrency() >= getPrice(player) && (clan.getCapacity() == 5 || clan.getCapacity() == 7 || clan.getCapacity() == 9);
	}

	public void purchase(Player player) {
		Clan clan = clanService.getClanForPlayer(player);
		ClanMember member = clanService.getClanMember(player);
		
		switch (clan.getCapacity()) {
		case 5: {
			clan.setCapacity(7);
			break;
		}
		case 7: {
			clan.setCapacity(9);
			break;
		}
		case 9: {
			clan.setCapacity(12);
			break;
		}
		}
		
		new BukkitRunnable() {
			public void run() {
				member.modifyCurrency(-getPrice(player)).update();
				clan.update();
			}
		}.runTaskAsynchronously(plugin);
	}

	public int getPrice(Player player) {
		Clan clan = clanService.getClanForPlayer(player);
		
		switch (clan.getCapacity()) {
		case 5:
			return 2000;
		case 7:
			return 5000;
		case 9:
			return 10000;
		}
		
		return 0;
	}
}