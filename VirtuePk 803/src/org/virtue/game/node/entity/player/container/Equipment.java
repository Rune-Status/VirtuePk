package org.virtue.game.node.entity.player.container;

import org.virtue.game.item.Item;
import org.virtue.game.node.entity.player.Player;

/**
 * @author Taylor
 * @version 1.0
 */
public class Equipment {

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
	private Player player;

	/**
	 * Represenst the interface ID.
	 */
	public static final int INVENTORY_INTERFACE = 679;
	
	/**
	 * Constructs a new {@code inventory.java}.
	 * @param player The player.
	 */
	public Equipment(Player player) {
		this.player = player;
		Item testItem = new Item(11724, 1);//TODO: This stuff is just for testing, replace when proper rendering is available
		items.set(testItem.getEquipId(), testItem);
		testItem = new Item(4151, 1);
		items.set(testItem.getEquipId(), testItem);
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
		player.getPacketDispatcher().dispatchItems(94, items);
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
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
}
