package org.virtue.network.messages;

/**
 * @author Taylor
 * @version 1.0
 */
public class VarpMessage {

	/**
	 * The ID of the varp
	 */
	private final int varpId;
	
	/**
	 * The ID of the varp
	 */
	private final int value;
	
	/**
	 * If the varp 
	 */
	private final boolean cs2;
	
	/**
	 * Constructs a new {@code VarpContext.java}
	 * @param varpId The varp ID
	 * @param cs2 If this is a cs2 varp
	 */
	public VarpMessage(int varpId, int value, boolean cs2) {
		this.varpId = varpId;
		this.value = value;
		this.cs2 = cs2;
	}
	
	/**
	 * Constructs a new {@code VarpContext.java}
	 * @param varpId The varp ID
	 */
	public VarpMessage(int varpId, int value) {
		this(varpId, value, false);
	}

	/**
	 * @return The varpId
	 */
	public int getVarpId() {
		return varpId;
	}

	/**
	 * @return The cs2
	 */
	public boolean isCs2() {
		return cs2;
	}

	/**
	 * @return The value
	 */
	public int getValue() {
		return value;
	}
}
