package org.virtue.game.logic.node.entity.player;

import org.virtue.game.config.IncommingOpcodes;

public enum PlayerOption {
	/**
	 * The first player option. Usually "Attack"
	 */
	ONE(1, IncommingOpcodes.PLAYER_OPTION_1_PACKET), 
	/**
	 * The second player option. Usually "Follow"
	 */
	TWO(2, IncommingOpcodes.PLAYER_OPTION_2_PACKET), 
	THREE(3, IncommingOpcodes.PLAYER_OPTION_3_PACKET),
	/**
	 * The fourth player option. Usually "Trade"
	 */
	FOUR(4, IncommingOpcodes.PLAYER_OPTION_4_PACKET),
	/**
	 * The fifth player option. Usually "Req Assist"
	 */
	FIVE(5, IncommingOpcodes.PLAYER_OPTION_5_PACKET), 
	SIX(6, IncommingOpcodes.PLAYER_OPTION_6_PACKET), 
	SEVEN(7, IncommingOpcodes.PLAYER_OPTION_7_PACKET), 
	EIGHT(8, IncommingOpcodes.PLAYER_OPTION_8_PACKET), 
	NINE(9, IncommingOpcodes.PLAYER_OPTION_9_PACKET), 
	TEN(10, IncommingOpcodes.PLAYER_OPTION_10_PACKET);
	
	private final int id, opcode;
	
	PlayerOption (int id, int opcode) {
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
	 * Gets the Player option associated with the specified opcode
	 * @param opcode	The opcode.
	 * @return			The NPC Option
	 */
	public static PlayerOption fromOpcode (int opcode) {
		for (PlayerOption button : PlayerOption.values()) {
			if (button.opcode == opcode) {
				return button;
			}
		}
		return null;
	}
}
