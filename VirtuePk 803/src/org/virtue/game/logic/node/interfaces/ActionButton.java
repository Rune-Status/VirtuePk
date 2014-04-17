package org.virtue.game.logic.node.interfaces;

import org.virtue.game.config.IncommingOpcodes;

public enum ActionButton {
	ONE(1, IncommingOpcodes.ACTION_BUTTON_1_PACKET), TWO(2, IncommingOpcodes.ACTION_BUTTON_2_PACKET), 
	THREE(3, IncommingOpcodes.ACTION_BUTTON_3_PACKET), FOUR(4, IncommingOpcodes.ACTION_BUTTON_4_PACKET), 
	FIVE(5, IncommingOpcodes.ACTION_BUTTON_5_PACKET), SIX(6, IncommingOpcodes.ACTION_BUTTON_6_PACKET), 
	SEVEN(7, IncommingOpcodes.ACTION_BUTTON_7_PACKET), EIGHT(8, IncommingOpcodes.ACTION_BUTTON_8_PACKET), 
	NINE(9, IncommingOpcodes.ACTION_BUTTON_9_PACKET), TEN(10, IncommingOpcodes.ACTION_BUTTON_10_PACKET);
	
	private final int id, opcode;
	
	ActionButton (int id, int opcode) {
		this.id = id;
		this.opcode = opcode;
	}
	
	public int getID () {
		return id;
	}
	
	public int getOpcode () {
		return opcode;
	}
	
	public static ActionButton getFromOpcode (int opcode) {
		for (ActionButton button : ActionButton.values()) {
			if (button.opcode == opcode) {
				return button;
			}
		}
		return null;
	}
}
