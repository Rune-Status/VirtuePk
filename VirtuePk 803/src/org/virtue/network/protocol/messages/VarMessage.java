package org.virtue.network.protocol.messages;

/**
 * @author Taylor
 * @version 1.0
 */
public class VarMessage {
	
	public static enum Type {PLAYER, NPC, CLIENT, BIT};

	/**
	 * The ID of the varp
	 */
	private final int varpId;
	
	/**
	 * The ID of the varp
	 */
	private final int value;
	
	/**
	 * Whether the var is varclient (varc) or varplayer (varp)
	 */
	private final boolean varClient;
	
	/**
	 * Constructs a new {@code VarpContext.java}
	 * @param varpId The varp ID
	 * @param cs2 If this is a cs2 varp
	 */
	public VarMessage(int varpId, int value, boolean varClient) {
		this.varpId = varpId;
		this.value = value;
		this.varClient = varClient;
	}
	
	/**
	 * Constructs a new {@code VarpContext.java}
	 * @param varpId The varp ID
	 */
	public VarMessage(int varpId, int value) {
		this(varpId, value, false);
	}

	/**
	 * @return The varpId
	 */
	public int getVarID() {
		return varpId;
	}

	/**
	 * @return The cs2
	 */
	public boolean isVarClient() {
		return varClient;
	}

	/**
	 * @return The value
	 */
	public int getValue() {
		return value;
	}
}
