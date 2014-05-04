package org.virtue.game.logic.item;

import org.virtue.cache.def.ItemDefinition;
import org.virtue.cache.def.ItemDefinitionLoader;
import org.virtue.game.logic.node.entity.player.container.EquipSlot;

/**
 * @author Taylor
 * @date Jan 21, 2014
 */
public class Item {

	/**
	 * Represents the ID.
	 */
	private int id;
	
	/**
	 * Represents the amount.
	 */
	private int amount;
	
	/**
	 * The cache definition for the item
	 */
	private ItemDefinition definition;
	
	/**
	 * Constructs a new {@code SendItem.java}.
	 * @param id The id.
	 * @param amount The amount.
	 */
	public Item(int id, int amount) {
		this.id = id;
		this.amount = amount;
		this.definition = ItemDefinitionLoader.forId(id);
	}
	
	public Item (Item item) {
		this.id = item.id;
		this.amount = item.amount;
		this.definition = item.definition;
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	public boolean isStackable () {
		return (definition.isNoted() || definition.isStackable());
	}
	
	public EquipSlot getEquipSlot () {
	    return EquipSlot.forID(definition.getEquiptSlotID());
	}
	
	/**
	 * Gets the cache definition for the item
	 * @return	The definition
	 */
	public ItemDefinition getDefinition () {
		return definition;
	}
}
