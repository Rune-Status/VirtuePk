package org.virtue.network.protocol.messages;

import org.virtue.game.logic.item.Item;

public class ItemsMessage {
	private Item[] items;
	private int key;
	private int[] slots;
	
	public ItemsMessage (int key, Item[] items, int... slots) {
		this.key = key;
		this.items = items;
		this.slots = slots;
	}
	
	public boolean isFullUpdate () {
		return (slots == null || slots.length == 0);
	}
	
	public int getKey () {
		return key;
	}
	
	public int[] getUpdateSlots () {
		return slots;
	}
	
	public boolean isNegitiveKey () {
		return key < 0;
	}
	
	/**
	 * Gets the items list for this message
	 * @return	An array of items
	 */
	public Item[] getItems () {
		return items;
	}
}
