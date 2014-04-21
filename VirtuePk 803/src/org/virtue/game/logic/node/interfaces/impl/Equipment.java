package org.virtue.game.logic.node.interfaces.impl;

import org.virtue.game.logic.item.Item;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.container.ItemsContainer;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.game.logic.node.interfaces.RSInterface;

/**
 * @author Taylor
 * @version 1.0
 */
public class Equipment extends AbstractInterface {

	/**
	 * Represents the slots.
	 */
	public static final byte SLOT_HAT = 0, SLOT_CAPE = 1, SLOT_AMULET = 2, SLOT_MAINHAND = 3, SLOT_CHEST = 4, SLOT_OFFHAND = 5, SLOT_LEGS = 7, SLOT_HANDS = 9, SLOT_FEET = 10, SLOT_RING = 12, SLOT_ARROWS = 13, SLOT_AURA = 14, SLOT_POCKET = 15;
	private static final int EQUIP_SIZE = 16;
	/**
	 * Represents the items in the inventory.
	 */
	private ItemsContainer<Item> items = new ItemsContainer<>(15, false);

	/**
	 * Represents the player.
	 */
	//private Player player;

	
	/**
	 * Constructs a new {@code inventory.java}.
	 * @param player The player.
	 */
	public Equipment(Player player) {
		super(RSInterface.EQUIPMENT, player);
		//this.player = player;
		Item testItem = new Item(26587, 1);//TODO: This stuff is just for testing, replace when proper rendering is available
		items.set(testItem.getEquipId(), testItem);
		testItem = new Item(26591, 1);
		items.set(testItem.getEquipId(), testItem);
	}
	
	@Override
	public void postSend () {
		sendInterfaceSettings(14, 0, 15, 15302654);//IfaceSettings: 95944718, 15, 0, 15302654
		sendInterfaceSettings(12, 2, 7, 2);//IfaceSettings: 95944716, 7, 2, 2
	}
	
	public void load () {
		refresh();
	}
	
	/**
	 * Adds an item to the inventory.
	 * @param item The item to add.
	 */
	public void add(Item item, boolean refresh) {
		items.add(item);
		if (refresh)
			refresh();
	}
	
	/**
	 * Adds an item to the inventory.
	 * @param item The item to add.
	 */
	public void add(Item item) {
		items.add(item);
		refresh();
	}
	
	/**
	 * Adds an item to the equipment.
	 * @param slot The slot.
	 * @param item The item.
	 */
	public void add(int slot, Item item) {
		items.set(slot, item);
		refresh();
	}
	
	/**
	 * Removes an item from the inventory.
	 * @param slot The slot to remove from.
	 */
	public void remove(int slot) {
		items.remove(items.get(slot));
		refresh();
	}
	
	/**
	 * Removes an item.
	 * @param item The item to remove.
	 */
	public void remove(Item item) {
		items.remove(item);
		refresh();
	}
	
	/**
	 * Returns an item.
	 * @param id The id.
	 * @return The item.
	 */
	public Item getItem(int id) {
		for (Item item : items.toArray()) {
			if (item == null)
				continue;
			if (item.getId() == id)
				return item;
			else
				continue;
		}
		return null;
	}

	/**
	 * Returns a slot.
	 * @param id The id.
	 * @return The slot.
	 */
	public int getSlot(int id) {
		for (int slot = 0; slot < items.getSize(); slot++)
			if (items.get(slot).getId() == id)
				return slot;
			else
				continue;
		return -1;
	}
	
	/**
	 * Refreshes this inventory.
	 */
	public void refresh() {
		getPlayer().getPacketDispatcher().dispatchItems(94, items);
//		player.getStack().sendItemSet(94, items.toArray());
//		player.getUpdateArchive().getAppearance().load();
	}
	
	/**
	 * @return The items
	 */
	public ItemsContainer<Item> getItems() {
		return items;
	}

	/**
	 * @param items The items to set
	 */
	public void setItems(ItemsContainer<Item> items) {
		this.items = items;
	}

	/**
	 * @return the player
	 */
	/*public Player getPlayer() {
		return player;
	}*/

	/**
	 * @param player the player to set
	 */
	/*public void setPlayer(Player player) {
		this.player = player;
	}*/

	
	@Override
	public void handleActionButton(int componentID, int slotID, int itemID, ActionButton button) {
		System.out.println("Equipment button pressed: component="+componentID+", slot="+slotID+", item="+itemID+", button="+button.getID());
	}

	@Override
	public int getTabID() {
		return 3;
	}
}
