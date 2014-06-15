package org.virtue.network.protocol.messages;

import org.virtue.game.logic.vars.VarDomainType;

/**
 * @author Taylor
 * @version 1.0
 */
public class VarMessage {

	/**
	 * The key for the var
	 */
	private final int key;
	
	/**
	 * The value of the var
	 */
	private final Object value;
	
	/**
	 * The type of the var
	 */
	private final VarDomainType type;
	
	/**
	 * Whether the message represents a varBit update
	 */
	private final boolean varBit;
	
	public VarMessage(VarDomainType type, int varID, Object value) {
		this(type, varID, value, false);
	}
	
	/**
	 * Constructs a new {@code VarpContext.java}
	 * @param type		The variable type
	 * @param varID 	The variable ID (aka key)
	 * @param value		The variable value
	 * @param varBit	Whether the message represents a varBit update
	 */
	public VarMessage(VarDomainType type, int varID, Object value, boolean varBit) {
		this.key = varID;
		this.value = value;
		this.type = type;
		this.varBit = varBit;
	}

	/**
	 * @return The varpId
	 */
	public int getVarID() {
		return key;
	}

	/**
	 * @return The domain type of the var
	 */
	public VarDomainType getType() {
		return type;
	}
	
	/**
	 * Returns whether this message is a varBit update
	 * @return	True if the message is a varBit update, false otherwise
	 */
	public boolean isVarBit () {
		return varBit;
	}
	
	/**
	 * Returns whether the value is a string
	 * @return	True if the value is a string, false if it is an integer
	 */
	public boolean isStringValue () {
		return value instanceof String;
	}

	/**
	 * @return The value as an integer
	 */
	public int getIntValue() {
		return (Integer) value;
	}
	
	/**
	 * @return The value as a string
	 */
	public String getStrValue () {
		return (String) value;
	}
}
