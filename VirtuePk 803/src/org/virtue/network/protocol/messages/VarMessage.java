package org.virtue.network.protocol.messages;

/**
 * @author Taylor
 * @version 1.0
 */
public class VarMessage {
	
	public static VarMessage varp (int key, int value) {
		return new VarMessage(DomainType.PLAYER, key, value);
	}
	
	public static VarMessage varc (int key, int value) {
		return new VarMessage(DomainType.CLIENT, key, value);
	}
	
	public static VarMessage varcStr (int key, String value) {
		return new VarMessage(DomainType.CLIENT_STR, key, value);
	}
	
	public static VarMessage varbit (int key, int value) {
		return new VarMessage(DomainType.BIT, key, value);
	}
	
	public static enum DomainType {PLAYER, NPC, CLIENT, CLIENT_STR, BIT};

	/**
	 * The key for the var
	 */
	private final int key;
	
	/**
	 * The value of the var
	 */
	private final Object value;
	
	/**
	 * Whether the var is varclient (varc) or varplayer (varp)
	 */
	private final DomainType type;
	
	/**
	 * Constructs a new {@code VarpContext.java}
	 * @param varpId The varp ID
	 * @param cs2 If this is a cs2 varp
	 */
	public VarMessage(DomainType type, int varpId, Object value) {
		this.key = varpId;
		this.value = value;
		this.type = type;
	}
	
	/**
	 * Constructs a new {@code VarpContext.java}
	 * @param varpId The varp ID
	 */
	public VarMessage(int varpId, int value) {
		this(DomainType.PLAYER, varpId, value);
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
	public DomainType getType() {
		return type;
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
