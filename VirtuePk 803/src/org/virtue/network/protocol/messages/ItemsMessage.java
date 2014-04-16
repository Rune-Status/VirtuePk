package org.virtue.network.protocol.messages;

import org.virtue.game.item.Item;

public class ItemsMessage {
	private Item[] items;
	private int key;
	
	public ItemsMessage (int key, Item[] items) {
		this.key = key;
		this.items = items;
	}
	
	public int getKey () {
		return key;
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
