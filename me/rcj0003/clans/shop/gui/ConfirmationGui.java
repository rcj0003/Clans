package me.rcj0003.clans.shop.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

import me.rcj0003.clans.shop.ShopItem;
import me.rcj0003.clans.utils.ItemBuilder;
import me.rcj0003.clans.utils.Utils.StringUtils;
import me.rcj0003.clans.utils.gui.Gui;
import me.rcj0003.clans.utils.gui.GuiHandler;

public class ConfirmationGui extends Gui {
	private GuiHandler guiHandler;
	private ShopItem item;
	
	public ConfirmationGui(Player owner, GuiHandler guiHandler, ShopItem item) {
		super(owner);
		this.guiHandler = guiHandler;
		this.item = item;
	}
	
	public Inventory createInventory() {
		Inventory inventory = Bukkit.createInventory(null, 9, StringUtils.convertColorCodes("&c&lConfirm " + item.getDisplayName()));
		
		inventory.setItem(2, new ItemBuilder(Material.WOOL).setDisplayName("&a&lConfirm").setDamage((short) 5).createItem());
		inventory.setItem(6, new ItemBuilder(Material.WOOL).setDisplayName("&c&lDeny").setDamage((short) 14).createItem());
		
		return inventory;
	}

	public boolean onClick(int slot, ClickType clickType) {		
		if (slot == 2) {
			if (!item.canPurchase(owner)) {
				owner.sendMessage(StringUtils.convertColorCodes("&8[&cClans&8]&c You cannot purchase this."));
				guiHandler.closeGui(this);
				return true;
			}
			item.getPurchaseAction().purchase(owner);
			owner.sendMessage(StringUtils.convertColorCodes("&8[&cClans&8]&a Purchased!"));
			guiHandler.closeGui(this);
		} else if (slot == 6) {
			guiHandler.closeGui(this);
		}
		
		return true;
	}

	public void onBeforeClose() {
	}

	public void onClose() {
	}
}