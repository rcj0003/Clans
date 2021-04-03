package me.rcj0003.clans.utils.gui;

import java.util.List;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

public abstract class GuiWidget<T> {
	protected int widgetOffset = 0;
	protected Inventory inventory;
	protected List<T> objectList;
	protected int inventoryOffset, widgetSize;
	protected boolean isHorizontal;

	public GuiWidget(Inventory inventory, List<T> objectList, int inventoryOffset, int widgetSize,
			boolean isHorizontal) {
		this.inventory = inventory;
		this.objectList = objectList;
		this.inventoryOffset = inventoryOffset;
		this.widgetSize = widgetSize;
		this.isHorizontal = isHorizontal;
	}

	public List<T> getWidgetObjects() {
		return objectList.size() <= widgetSize ? objectList
				: objectList.subList(widgetOffset, widgetOffset + widgetSize);
	}

	public void decreaseOffset() {
		widgetOffset--;
		correctOffset();
	}

	public void increaseOffset() {
		widgetOffset++;
		correctOffset();
	}

	protected void correctOffset() {
		widgetOffset = Math.max(0, Math.min(widgetOffset, objectList.size() - widgetSize));
	}

	protected int getInventorySlot(int widgetSlot) {
		if (isHorizontal)
			return inventoryOffset + widgetSlot;
		else
			return inventoryOffset + (widgetSlot * 9);
	}

	protected int getWidgetSlot(int inventorySlot) {
		int widgetOffset = -1;

		if (isHorizontal) {
			widgetOffset = inventorySlot - inventoryOffset;
			if (widgetOffset < 0 || widgetOffset >= widgetSize)
				widgetOffset = -1;
		} else {
			if (inventorySlot % 9 == inventoryOffset % 9) {
				widgetOffset = (inventorySlot / 9) - (inventoryOffset / 9);
				if (widgetOffset < 0 || widgetOffset >= widgetSize)
					widgetOffset = -1;
			}
		}

		return widgetOffset;
	}

	protected T getWidgetObject(int widgetSlot) {
		return widgetSlot != -1 && widgetSlot + widgetOffset < objectList.size() ? objectList.get(widgetOffset + widgetSlot) : null;
	}

	protected abstract void update();

	protected abstract void onClick(int inventorySlot, ClickType clickType);
}