package org.virtue.game.logic.item;

import java.util.HashMap;
import java.util.Map;

import org.virtue.cache.def.ItemDefinition;
import org.virtue.cache.def.ItemDefinitionLoader;
import org.virtue.game.logic.content.skills.firemaking.Fire;
import org.virtue.game.logic.content.skills.firemaking.FiremakingAction;
import org.virtue.game.logic.item.Logs.LogType;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.container.EquipSlot;
import org.virtue.game.logic.node.interfaces.ActionButton;

/**
 * @author Taylor
 * @date Jan 21, 2014
 */
public class Item {
	
	private static final Map<Integer, Class<? extends Item>> transforms = new HashMap<Integer, Class<? extends Item>>();
	
	static {
		for (LogType logs : LogType.values()) {
			transforms.put(logs.getID(), Logs.class);
		}
	}
	
	public static Item create (int id, int amount) {
		if (transforms.containsKey(id)) {
			Class<? extends Item> transform = transforms.get(id);
			if (transform.equals(Logs.class)) {
				return new Logs(LogType.forID(id), amount);
			}
		}
		return new Item(id, amount);
	}

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
	 * Constructs a new {@code Item.java}.
	 * @param id The id.
	 * @param amount The amount.
	 */
	protected Item(int id, int amount) {
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
		this.definition = ItemDefinitionLoader.forId(id);
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
	
	public void handleItemOnItem (Player player, int invSlot, Item item2, int item2Slot) {
		switch (id) {
		case 590://Tinderbox
			if (Fire.Type.forLogs(item2.getId()) != null) {
				player.setActionEvent(new FiremakingAction(item2, item2Slot));
			}
			break;
		default:
			System.out.println("Item "+getId()+" at slot "+invSlot+" used with item "+item2.getId()+" at slot "+item2Slot);
			break;
		}
		
 	}
	
	public void handleInventoryOption (Player player, String optionName, ActionButton option, int slotID) {
		System.out.println("Inventory item pressed: slot="+slotID+", itemID="+id+", option="+optionName+" ("+option.getID()+")");
	}
}
