package me.rcj0003.clans.utils.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;

public class GuiHandler implements Listener {
	private Map<UUID, Gui> guiMap = new HashMap<>();
	
	public Gui getGui(Player player) {
		return guiMap.get(player.getUniqueId());
	}
	
	public void openGui(Player player, Gui gui) {
		gui.openGui();
		guiMap.put(player.getUniqueId(), gui);
	}

	public void closeGui(Gui gui) {
		closeGui(gui, true);
	}

	public void closeGui(Gui gui, boolean doClose) {
		gui.onBeforeClose();
		
		if (doClose)
			gui.getOwner().closeInventory();
		
		guiMap.remove(gui.getOwner().getUniqueId());
		gui.onClose();
	}
	
	public void closeGui(HumanEntity player) {
		closeGui(player, true);
	}

	public void closeGui(HumanEntity player, boolean doClose) {
		Gui gui = guiMap.remove(player.getUniqueId());
		gui.onBeforeClose();
		
		if (doClose)
			gui.getOwner().closeInventory();
		
		gui.onClose();
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (!guiMap.containsKey(e.getWhoClicked().getUniqueId()) || e.getSlotType() == SlotType.OUTSIDE
				|| e.getClickedInventory() != e.getWhoClicked().getOpenInventory().getTopInventory())
			return;
		
		if (guiMap.get(e.getWhoClicked().getUniqueId()).onClick(e.getSlot(), e.getClick()))
			e.setCancelled(true);
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if (guiMap.containsKey(e.getPlayer().getUniqueId()))
			closeGui(e.getPlayer(), false);
	}
}