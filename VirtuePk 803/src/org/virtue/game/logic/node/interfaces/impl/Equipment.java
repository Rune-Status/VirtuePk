package org.virtue.game.logic.node.interfaces.impl;

import org.virtue.game.logic.item.Item;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.container.EquipSlot;
import org.virtue.game.logic.node.entity.player.container.ItemsContainer;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.game.logic.node.interfaces.RSInterface;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Taylor
 * @version 1.0
 */
public class Equipment extends AbstractInterface {

	/**
	 * Represents the slots.
	 */
	public static final byte SLOT_HAT = 0, SLOT_CAPE = 1, SLOT_AMULET = 2, SLOT_MAINHAND = 3, SLOT_CHEST = 4, SLOT_OFFHAND = 5, SLOT_LEGS = 7, SLOT_HANDS = 9, SLOT_FEET = 10, SLOT_RING = 12, SLOT_ARROWS = 13, SLOT_AURA = 14, SLOT_POCKET = 15;
	public static final int EQUIP_SIZE = 16;
	/**
	 * Represents the items in the inventory.
	 */
	private ItemsContainer<Item> items = new ItemsContainer<>(EQUIP_SIZE, false);

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
		/*Item testItem = Item.create(26587, 1);//TODO: This stuff is just for testing, replace when proper rendering is available
		items.set(testItem.getDefinition().getEquiptSlotID(), testItem);
		testItem = Item.create(26591, 1);
		items.set(testItem.getDefinition().getEquiptSlotID(), testItem);*/
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
		if (refresh) {
			refresh();
		}
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
		refresh(slot);
	}
	
	public void add(EquipSlot slot, Item item) {
		items.set(slot.getSlotID(), item);
		refresh(slot.getSlotID());
	}
	
	/**
	 * Switches the item in the given slot with the given item
	 * @param slot	The slot to switch
	 * @param item	The item to switch to
	 * @return		The old item
	 */
	public Item swapItem (EquipSlot slot, Item item) {
		Item oldItem = items.get(slot.getSlotID());
		items.set(slot.getSlotID(), item);
		refresh(slot.getSlotID());
		return oldItem;
	}
	
	/**
	 * Removes an item from the inventory.
	 * @param slot The slot to remove from.
	 */
	public void remove(int slot) {
		items.remove(items.get(slot));
		refresh(slot);
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
			if (item == null) {
				continue;
			}
			if (item.getId() == id) {
				return item;
			} else {
				continue;
			}
		}
		return null;
	}
	
	public Item getAtSlot (EquipSlot slot) {
		return items.get(slot.getSlotID());
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
	 * Refreshes the equipment.
	 */
	public void refresh() {
		getPlayer().getPacketDispatcher().dispatchItems(94, items);
//		player.getStack().sendItemSet(94, items.toArray());
//		player.getUpdateArchive().getAppearance().load();
	}
	
	public void refresh (int... slots) {
		getPlayer().getPacketDispatcher().dispatchItems(94, items, slots);
	}
	
	/**
	 * Serialises the current equipment data into a {@link com.google.gson.JsonArray}
	 * @return	A JsonArray containing the inventory data
	 */
	public JsonArray serialise () {
		JsonArray equipment = new JsonArray();
		int slotID = 0;
		for (Item i : items.getItems()) {
			if (i == null) {
				slotID++;
				continue;
			}
			JsonObject item = new JsonObject();
			item.addProperty("slot", slotID);
			item.addProperty("item", i.getId());
			item.addProperty("amount", i.getAmount());
			equipment.add(item);
			slotID++;
		}
		return equipment;
	}
	
	/**
	 * Deserialises the equipment data from the specified JSON array
	 * @param equipment The {@link com.google.gson.JsonArray} containing the equipment data
	 */
	public void deserialise (JsonArray equipment) {
		items.reset();
		for (JsonElement itemData : equipment) {
			JsonObject data = (JsonObject) itemData;
			int slotID = data.get("slot").getAsInt();
			int itemID = data.get("item").getAsInt();
			int amount = data.get("amount").getAsInt();
			Item item = Item.create(itemID, amount);
			if (item.getDefinition() == null) {
				continue;//Item does not exist
			}
			items.set(slotID, item);
		}
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
	
	@Override
	public void handleActionButton(int componentID, int slotID, int itemID, ActionButton button) {
		if (componentID == 14) {
			Item item = items.get(slotID);
            if (item == null) {
                return;//Invalid item
            }
            if (item.getId() != itemID) {
            	refresh();//Something is out of sync, resynchronised the equipment
            	return;
            }
            if (button.equals(ActionButton.ONE)) {
            	handleRemove(item, slotID);
            }
		}
		System.out.println("Equipment button pressed: component="+componentID+", slot="+slotID+", item="+itemID+", button="+button.getID());
	}
	
	public void handleRemove (Item item, int slot) {
		if (getPlayer().getInventory().add(item)) {
			items.remove(slot, item);
			refresh();
			getPlayer().getUpdateArchive().getAppearance().packBlock();
		}
	}

	@Override
	public int getTabID() {
		return 3;
	}
}
