package me.rcj0003.clans.shop;

import org.bukkit.entity.Player;

public interface PurchaseAction {
	boolean canPurchase(Player player);
	void purchase(Player player);
	int getPrice(Player player);
}