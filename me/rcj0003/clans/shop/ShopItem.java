package me.rcj0003.clans.shop;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ShopItem {
	private int slot;
	private Material material;
	private String[] description;
	private String internalName, displayName;
	private PurchaseAction action;
	
	public ShopItem(int slot, Material material, String internalName, String displayName, String[] description, PurchaseAction action) {
		this.slot = slot;
		this.material = material;
		this.displayName = displayName;
		this.description = description;
		this.action = action;
	}
	
	public int getSlot() {
		return slot;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public String getInternalName() {
		return internalName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String[] getDescription() {
		return description;
	}
	
	public PurchaseAction getPurchaseAction() {
		return action;
	}
	
	public int getPrice(Player player) {
		return action.getPrice(player);
	}
	
	public boolean canPurchase(Player player) {
		return action.canPurchase(player);
	}
}