package org.virtue.game.logic.item;

import org.virtue.game.logic.content.skills.firemaking.FiremakingAction;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.ActionButton;

public class Logs extends Item {
	
	public static enum LogType {
		NORMAL(1511),
		OAK(1521),
		WILLOW(1519),
		TEAK(6333),
		MAPLE(1517),
		MAHOGANY(6332),
		YEW(1515),
		MAGIC(1513),
		ELDER(29556);
		
		private final int itemID;
		
		LogType (int itemID) {
			this.itemID = itemID;
		}
		
		/**
		 * Gets the item ID of the log
		 * @return	The item ID
		 */
		public int getID () {
			return itemID;
		}
		
		public static LogType forID (int id) {
			for (LogType type : LogType.values()) {
				if (type.itemID == id) {
					return type;
				}
			}
			return null;
		}
	}
	
	private final LogType type;

	public Logs(LogType type, int amount) {
		super(type.getID(), amount);
		this.type = type;
	}
	
	public LogType getType () {
		return type;
	}
	
	@Override
	public void handleItemOnItem (Player player, int invSlot, Item item2, int item2Slot) {
		if (item2.getId() == 590) {//Tinderbox
			player.setActionEvent(new FiremakingAction(this, invSlot));
		}
	}
	
	@Override
	public void handleInventoryOption (Player player, String optionName, ActionButton option, int slotID) {
		switch (option) {
		case TWO://Light
			player.setActionEvent(new FiremakingAction(this, slotID));
			break;
		case ONE://Craft
			
		default:
			System.out.println("Logs clicked: slot="+slotID+", type="+type+", option="+optionName+" ("+option.getID()+")");
			break;
		}
	}

}
