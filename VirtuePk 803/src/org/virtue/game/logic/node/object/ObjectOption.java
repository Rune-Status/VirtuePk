package org.virtue.game.logic.node.object;

import org.virtue.game.config.IncommingOpcodes;

public enum ObjectOption {
	ONE(1, IncommingOpcodes.OBJECT_OPTION_1_PACKET), 
	TWO(2, IncommingOpcodes.OBJECT_OPTION_2_PACKET), 
	THREE(3, IncommingOpcodes.OBJECT_OPTION_3_PACKET), 
	FOUR(4, IncommingOpcodes.OBJECT_OPTION_4_PACKET), 
	FIVE(5, IncommingOpcodes.OBJECT_OPTION_5_PACKET), 
	EXAMINE(-1, IncommingOpcodes.OBJECT_EXAMINE_PACKET);
	
	private final int id, opcode;
	
	ObjectOption (int id, int opcode) {
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
	 * Gets the NPC option associated with the specified opcode
	 * @param opcode	The opcode.
	 * @return			The NPC Option
	 */
	public static ObjectOption getFromOpcode (int opcode) {
		for (ObjectOption button : ObjectOption.values()) {
			if (button.opcode == opcode) {
				return button;
			}
		}
		return null;
	}
}
