package org.virtue.game.logic.node.interfaces.impl;

import org.virtue.game.logic.item.Item;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.container.ItemsContainer;
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.game.logic.node.interfaces.RSInterface;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Bank extends AbstractInterface {
	
	public static int MAX_ITEMS = 500;
	
	/**
	 * Represents the items in the bank.
	 */
	private ItemsContainer<Item> items = new ItemsContainer<>(MAX_ITEMS, true);
	
	private int lastDepositAmount = 1;

	public Bank(Player p) {
		super(RSInterface.BANK, p);
		depositItem(new Item(54, 10));
		depositItem(new Item(200, 1));
	}

	@Override
	public void postSend() {
		getPlayer().getInventory().setLock(true);
		sendInterfaceSettings(39, 0, 871, 2622718);
		sendInterfaceSettings(54, 0, 27, 2361214);
		sendInterfaceSettings(132, 0, 27, 4260990);
		refresh();
	}
	
	@Override
	public void close() {
		getPlayer().getInventory().setLock(false);
		super.close();
	}
	
	public void depositInventoryItem(int itemID, int slot, int amount) {
		if (getFreeSlots() <= 0) {
			return;//TODO: Print a message indicating full bank
		}
		Item item = getPlayer().getInventory().getItemAtSlot(slot);
		if (item == null || item.getId() != itemID) {
			return;
		}
		item = new Item(item);
		item.setAmount(amount);
		int removed = getPlayer().getInventory().remove(item);
		item.setAmount(removed);
		depositItem(item);
		refresh();
	}
	
	public boolean depositItem (Item item) {
		if (item.getDefinition().isNoted()) {
			item = new Item(item.getDefinition().getNotedID(), item.getAmount());
		}
		return items.add(item);
	}
	
	public int getFreeSlots () {
		return items.getFreeSlots();
	}
	
	public void withdrawItem (int itemID, int slot, int amount) {
		Item item = items.get(slot);
		if (item == null || item.getId() != itemID) {
			return;//Something went wrong in the request
		}
		item = new Item(item);
		int requiredSlots = amount;
		if (item.isStackable()) {
			requiredSlots = 1;
		}
		
		if (getPlayer().getInventory().getFreeSlots() < requiredSlots) {
			amount = getPlayer().getInventory().getFreeSlots();
		}
		item.setAmount(amount);
		int removed = items.remove(item);
		item.setAmount(removed);
		getPlayer().getInventory().add(item);
		refresh(slot);
	}
	
	/**
	 * Refreshes the bank.
	 */
	public void refresh(int... slots) {
		getPlayer().getPacketDispatcher().dispatchItems(95, items, slots);
	}

	@Override
	public void handleActionButton(int component, int slotID, int itemID, ActionButton button) {
		int amount = -1;
		switch (component) {
		case 39://Main tab
			switch (button) {
			case ONE://Withdraw 1
				amount = 1;
				break;
			case TWO://Withdraw 5
				amount = 5;
				break;
			case THREE://Withdraw 10
				amount = 10;
				break;
			case FOUR://Withdraw last
				amount = lastDepositAmount;
				break;
			case FIVE://Withdraw x
				
				break;
			case SIX://Withdraw all
				break;
			case SEVEN://Withdraw all-but-one
				break;
			case TEN://Examine
				break;
			default:				
				break;
			}
			System.out.println("Attempt to withdraw "+amount+" of item "+itemID+" at slot "+slotID+" (button="+button.getID()+")");
			if (amount > 0) {
				withdrawItem(itemID, slotID, amount);
				return;
			}
			break;
		case 54://Inventory
			switch (button) {
			case ONE://Deposit 1
				amount = 1;
				break;
			case TWO://Deposit 5
				amount = 5;
				break;
			case THREE://Deposit 10
				amount = 10;
				break;
			case FOUR://Deposit last
				amount = lastDepositAmount;
				break;
			case FIVE://Deposit x
				
				break;
			case SIX://Deposit all
				break;
			case EIGHT://Item action (eat, wield, etc)
				
				return;
			case TEN://Examine
				return;
			default:				
				break;
			}
			if (amount > 0) {
				depositInventoryItem(itemID, slotID, amount);
				return;
			}
			System.out.println("Attempt to deposit "+amount+" of item "+itemID+" at slot "+slotID+" (button="+button.getID()+")");
			break;
		case 50:
			close();
			return;
		default:
			System.out.println("Bank button pressed: component="+component+", slot="+slotID+", item="+itemID+", button="+button.getID());
			break;
		}	
	}
	
	public void switchItems (int fromSlot, int fromComponent, int toSlot, int toComponent) {
		switchItems(fromSlot, fromComponent, toSlot, toComponent, true);
	}
	
	public void switchItems (int fromSlot, int fromComponent, int toSlot, int toComponent, boolean refresh) {
		if (fromComponent == toComponent && toComponent == 50) {
			getPlayer().getInventory().switchItem(fromSlot, toSlot);
			return;
		}
		if (fromComponent != toComponent || toComponent != 39) {
			System.out.println("Unhandled request to swap item tabs: fromComp="+fromComponent+", toComp="+toComponent+", fromSlot="+fromSlot+", toSlot="+toSlot);
			return;
		}
		Item item = items.get(fromSlot);
		Item item2 = items.get(toSlot);
		items.set(fromSlot, item2);
		items.set(toSlot, item);
		if (refresh) {
			refresh(fromSlot, toSlot);
		}
	}

	@Override
	public int getTabID() {
		return -1;
	}
	
	/**
	 * Serialises the current bank data into a {@link com.google.gson.JsonArray}
	 * @return	A JsonArray containing the inventory data
	 */
	public JsonArray serialise () {
		JsonArray inventory = new JsonArray();
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
			inventory.add(item);
			slotID++;
		}
		return inventory;
	}
	
	/**
	 * Deserialises the bank data from the specified JSON array
	 * @param inventory The {@link com.google.gson.JsonArray} containing the inventory data
	 */
	public void deserialise (JsonArray inventory) {
		items.reset();
		for (JsonElement itemData : inventory) {
			JsonObject data = (JsonObject) itemData;
			int slotID = data.get("slot").getAsInt();
			int itemID = data.get("item").getAsInt();
			int amount = data.get("amount").getAsInt();
			Item item = new Item(itemID, amount);
			if (item.getDefinition() == null) {
				continue;//Item does not exist
			}
			items.set(slotID, item);
		}
	}

}
