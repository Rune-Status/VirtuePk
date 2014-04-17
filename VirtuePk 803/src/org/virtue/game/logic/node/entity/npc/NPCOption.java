package org.virtue.game.logic.node.entity.npc;

import org.virtue.game.config.IncommingOpcodes;

public enum NPCOption {
	/**
	 * The first NPC option. Usually "Talk"
	 */
	ONE(1, IncommingOpcodes.NPC_OPTION_1_PACKET),
	/**
	 * The second NPC option. Usually "Attack"
	 */
	TWO(2, IncommingOpcodes.NPC_OPTION_2_PACKET), 
	/**
	 * The third NPC option. This includes most other options, such as "pickpocket" or "trade"
	 */
	THREE(3, IncommingOpcodes.NPC_OPTION_3_PACKET), 
	/**
	 * The fourth NPC option
	 */
	FOUR(4, IncommingOpcodes.NPC_OPTION_4_PACKET), 
	/**
	 * The fifth NPC option
	 */
	FIVE(5, IncommingOpcodes.NPC_OPTION_5_PACKET), 
	/**
	 * The NPC "Examine" option. This option will not cause the player to move towards the NPC
	 */
	EXAMINE(-1, IncommingOpcodes.NPC_EXAMINE_PACKET);
	
	private final int id, opcode;
	
	NPCOption (int id, int opcode) {
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
	public static NPCOption fromOpcode (int opcode) {
		for (NPCOption button : NPCOption.values()) {
			if (button.opcode == opcode) {
				return button;
			}
		}
		return null;
	}
}
