package me.rcj0003.clans.shop.gui;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

import me.rcj0003.clans.shop.ShopItem;
import me.rcj0003.clans.utils.ItemBuilder;
import me.rcj0003.clans.utils.Utils.StringUtils;
import me.rcj0003.clans.utils.gui.Gui;
import me.rcj0003.clans.utils.gui.GuiHandler;

public class ShopGui extends Gui {
	private GuiHandler guiHandler;
	private Map<Integer, ShopItem> items;

	public ShopGui(Player owner, GuiHandler guiHandler, Map<Integer, ShopItem> items) {
		super(owner);
		this.guiHandler = guiHandler;
		this.items = items;
	}

	public Inventory createInventory() {
		Inventory inventory = Bukkit.createInventory(null, 36, StringUtils.convertColorCodes("&c&lClan Shop"));

		for (ShopItem item : items.values()) {
			inventory.setItem(item.getSlot(), new ItemBuilder(item.getMaterial()).setDisplayName(item.getDisplayName())
					.setLore(item.getDescription()).addLore("", "&7Price: &c" + item.getPrice(owner)).createItem());
		}

		return inventory;
	}

	public boolean onClick(int slot, ClickType clickType) {
		if (items.containsKey(slot))
			guiHandler.openGui(owner, new ConfirmationGui(owner, guiHandler, items.get(slot)));
		return true;
	}

	public void onBeforeClose() {
	}

	public void onClose() {
	}
}