package org.virtue.game.logic.node.interfaces.impl;

import org.virtue.game.logic.events.InputEnteredEvent;
import org.virtue.game.logic.item.Item;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.container.ItemsContainer;
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.game.logic.node.interfaces.RSInterface;
import org.virtue.game.logic.node.object.RS3Object;
import org.virtue.network.protocol.messages.ClientScriptVar;
import org.virtue.network.protocol.messages.VarMessage;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.virtue.game.logic.node.entity.npc.NPC;
import org.virtue.game.logic.node.entity.player.container.EquipSlot;

public class Bank extends AbstractInterface {
	
	public static int MAX_ITEMS = 500;
	
	/**
	 * Represents the items in the bank.
	 */
	private final ItemsContainer<Item> items = new ItemsContainer<>(MAX_ITEMS, true);
	
	private int lastDepositAmount = 1;
	//private int currentTab = 0;

	public Bank(Player p) {
		super(RSInterface.BANK, p);
	}

	@Override
	public void postSend() {
		getPlayer().getInventory().setLock(true);
		sendInterfaceSettings(39, 0, 871, 2622718);
		sendInterfaceSettings(54, 0, 27, 2361214);
		sendInterfaceSettings(132, 0, 27, 4260990);
		getPlayer().getPacketDispatcher().dispatchVar(new VarMessage(111, lastDepositAmount));
		refresh();
	}
	
	@Override
	public void close() {
		getPlayer().getInventory().setLock(false);
		super.close();
	}
	
	public boolean depositInventoryItem(int itemID, int slot, int amount) {
		if (getFreeSlots() <= 0) {
			return false;//TODO: Print a message indicating full bank
		}
		Item item = getPlayer().getInventory().getItemAtSlot(slot);
		if (item == null || item.getId() != itemID) {
			return false;
		}
		item = new Item(item);
		item.setAmount(amount);
		int removed = getPlayer().getInventory().remove(item);
		item.setAmount(removed);
		depositItem(item);
		refresh();
		return true;
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
		if (items.get(slot) == null) {
			items.shift();
			refresh();
		} else {
			refresh(slot);
		}
	}
	
	/**
	 * Refreshes the bank.
	 * @param slots	The slots to update (if no slots are given, all will be updated)
	 */
	public void refresh(int... slots) {
		getPlayer().getPacketDispatcher().dispatchItems(95, items, slots);
	}

	@Override
	public void handleActionButton(int component, final int slotID, final int itemID, ActionButton button) {
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
				getPlayer().setInputEvent(new InputEnteredEvent () {
					@Override
					public void onInputEntered(Object input) {
						int amount = (Integer) input;
						if (amount == 0) {
							return;
						}
						lastDepositAmount = amount;
						getPlayer().getPacketDispatcher().dispatchVar(new VarMessage(111, lastDepositAmount));
						withdrawItem(itemID, slotID, amount);
					}
					@Override
					public void onCancel() { }					
				});	
				requestNumber("Enter amount: ");			
				break;
			case SIX://Withdraw all
				amount = items.getNumberOf(itemID);
				break;
			case SEVEN://Withdraw all-but-one
				amount = items.getNumberOf(itemID) - 1;
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
				getPlayer().setInputEvent(new InputEnteredEvent () {
					@Override
					public void onInputEntered(Object input) {
						int amount = (Integer) input;
						if (amount == 0) {
							return;
						}
						lastDepositAmount = amount;
						getPlayer().getPacketDispatcher().dispatchVar(new VarMessage(111, lastDepositAmount));
						depositInventoryItem(itemID, slotID, amount);
					}
					@Override
					public void onCancel() { }					
				});
				requestNumber("Enter amount: ");
				break;
			case SIX://Deposit all
				amount = getPlayer().getInventory().getItems().getNumberOf(itemID);
				break;
			case EIGHT://Item action (eat, wield, etc)
				handleInventoryItemAction(itemID, slotID);
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
			//System.out.println("Attempt to deposit "+amount+" of item "+itemID+" at slot "+slotID+" (button="+button.getID()+")");
			break;
		case 50:
			close();
			return;
		case 11://Deposit inventory
			depositInventory();
			return;
		case 15://Deposit equipment
			depositEquipment();
			return;
		case 17://Deposit BoB
			
		case 13://Deposit coin pouch
			
		case 4://Swap between insert and swap mode
		
		case 8://Swap between note mode
			
		case 139://Bank pin
			
		default:
			System.out.println("Bank button pressed: component="+component+", slot="+slotID+", item="+itemID+", button="+button.getID());
			break;
		}	
	}
	
	public void requestNumber (String message) {
		getPlayer().getPacketDispatcher().dispatchInterface(RSInterface.DIALOG_BOX);
		getPlayer().getPacketDispatcher().dispatchInterface(RSInterface.INPUT_DIALOG);
		getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(RSInterface.INT_INPUT_SCRIPT, message));
	}
	
	public void handleInventoryItemAction (int itemID, int slotID) {
		Item item = player.getInventory().getItemAtSlot(slotID);
		if (item == null || item.getId() != itemID) {
			return;
		}
		player.getInventory().handleEquip(item, slotID, null);		
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
	
	public void depositInventory () {
		for (int slot = 0;slot<Inventory.INVENTORY_SIZE;slot++) {
			Item item = getPlayer().getInventory().getItemAtSlot(slot);
			if (item == null) {
				continue;
			}
			if (!depositInventoryItem(item.getId(), slot, item.getAmount())) {
				getPlayer().getPacketDispatcher().dispatchMessage("Not enough space in your bank.");
				break;
			}
		}
	}
	
	public void depositEquipment () {
		for (EquipSlot slot : EquipSlot.values()) {
			if (getFreeSlots() <= 0) {
				getPlayer().getPacketDispatcher().dispatchMessage("Not enough space in your bank.");
				break;//TODO: Print a message indicating full bank
			}
			Item item = player.getEquipment().swapItem(slot, null);
			if (item == null) {
				continue;
			}			
			depositItem(item);
		}
		getPlayer().getUpdateArchive().getAppearance().packBlock();
		refresh();
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
	
	public static boolean isBankNpc (NPC npc) {
		return npc.getDefinition().getName().equalsIgnoreCase("Banker");
	}
	
	public static boolean isBankBooth (RS3Object object) {
		int id = object.getId();
		return id == 2015 || id == 2012 || id == 2019 || id == 782;
	}
}
