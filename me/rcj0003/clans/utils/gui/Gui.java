package me.rcj0003.clans.utils.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

public abstract class Gui {
	protected Player owner;
	
	public Gui(Player owner) {
		this.owner = owner;
	}
	
	public final Player getOwner() {
		return owner;
	}
	
	public final void openGui() {
		owner.openInventory(createInventory());
	}
	
	public abstract Inventory createInventory();
	public abstract boolean onClick(int slot, ClickType clickType);
	public abstract void onBeforeClose();
	public abstract void onClose();
}