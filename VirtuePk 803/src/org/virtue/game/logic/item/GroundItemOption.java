package org.virtue.game.logic.item;

import org.virtue.game.config.IncommingOpcodes;

public enum GroundItemOption {
	ONE(1, IncommingOpcodes.GROUND_ITEM_OPTION_1_PACKET),
	TWO(2, IncommingOpcodes.GROUND_ITEM_OPTION_2_PACKET),
	THREE(3, IncommingOpcodes.GROUND_ITEM_OPTION_3_PACKET),
	FOUR(4, IncommingOpcodes.GROUND_ITEM_OPTION_4_PACKET),
	FIVE(5, IncommingOpcodes.GROUND_ITEM_OPTION_5_PACKET),
	EXAMINE(-1, IncommingOpcodes.GROUND_ITEM_EXAMINE_PACKET);
	
	private final int id, opcode;
	
	GroundItemOption(int id, int opcode) {
		this.id = id;
		this.opcode = opcode;
	}
	
	/**
	 * Gets the ID for this option
	 * @return	The ID
	 */
	public int getID () {
		return id;
	}
	
	/**
	 * Gets the opcode used for this option
	 * @return	The opcode
	 */
	public int getOpcode () {
		return opcode;
	}
	
	/**
	 * Gets the ground item option associated with the specified opcode
	 * @param opcode	The opcode.
	 * @return			The ground item option
	 */
	public static GroundItemOption getFromOpcode (int opcode) {
		for (GroundItemOption button : GroundItemOption.values()) {
			if (button.opcode == opcode) {
				return button;
			}
		}
		return null;
	}

}
