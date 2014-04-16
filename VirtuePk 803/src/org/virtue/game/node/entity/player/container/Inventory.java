package org.virtue.game.node.entity.player.container;

import org.virtue.game.item.Item;
import org.virtue.game.node.entity.player.Player;

/**
 * @author Taylor
 * @version 1.0
 */
public class Inventory {

	/**
	 * Represents the items in the inventory.
	 */
	private ItemsContainer<Item> items = new ItemsContainer<>(28, false);

	/**
	 * Represents the oplayer.
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
	public Inventory(Player player) {
		this.player = player;
		items.add(new Item(54, 1));//Just for testing purposes; This can be updated with the actual items later
	}
	
	/**
	 * Loads the interface settings.
	 */
	public void load() {
		player.getPacketDispatcher().dispatchItems(93, items);
//		player.getStack().sendWidgetSettings(INVENTORY_INTERFACE, 0, 0, 27, 4554126);
//		player.getStack().sendWidgetSettings(INVENTORY_INTERFACE, 0, 28, 55, 2097152);
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
		for (Item item : items.toArray())
			if (item.getId() == id)
				return item;
			else
				continue;
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
		player.getPacketDispatcher().dispatchItems(93, items);
//		player.getStack().sendItemSet(93, items.toArray());
	}

	/**
	 * Returns if this {@link Inventory} contains an item.
	 * @param id The item to check.
	 * @return True if so; false otherwise.
	 */
	public boolean contains(int id) {
		for (Item item : items.toArray()) {
			if (item == null)
				continue;
			if (item.getId() == id)
				return true;
			else
				continue;
		}
		return false;
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
