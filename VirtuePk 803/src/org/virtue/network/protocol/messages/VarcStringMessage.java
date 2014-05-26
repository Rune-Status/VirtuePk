package org.virtue.network.protocol.messages;

public class VarcStringMessage {
	
	/**
	 * The ID of the varc string
	 */
	private final int varcID;
	
	/**
	 * The ID of the varc string
	 */
	private final String value;
	
	public VarcStringMessage (int varcID, String value) {
		this.varcID = varcID;
		this.value = value;
		
	}

	/**
	 * @return The varcID
	 */
	public int getVarcID() {
		return varcID;
	}

	/**
	 * @return The value
	 */
	public String getValue() {
		return value;
	}

}
