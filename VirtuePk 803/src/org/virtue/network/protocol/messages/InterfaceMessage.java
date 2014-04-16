package org.virtue.network.protocol.messages;

/**
 * @author Taylor
 * @version 1.0
 */
public class InterfaceMessage {

	private final int id;
	private final int windowLocation;
	private final int windowId;
	private final boolean walkable;
	
	/**
	 * Constructs a new {@code InterfaceContext.java}
	 * @param id The interface ID
	 * @param windowLocation The opcode
	 * @param walkable If the interface is walkable
	 */
	public InterfaceMessage(int id, int windowLocation, int windowId, boolean walkable) {
		this.id = id;
		this.windowLocation = windowLocation;
		this.walkable = walkable;
		this.windowId = windowId;
	}

	/**
	 * @return The id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return The opcode
	 */
	public int getOpcode() {
		return getWindowLocation();
	}

	/**
	 * @return The walkable
	 */
	public boolean isClipped() {
		return walkable;
	}

	/**
	 * @return The windowId
	 */
	public int getWindowId() {
		return windowId;
	}

	/**
	 * @return The windowLocation
	 */
	public int getWindowLocation() {
		return windowLocation;
	}
	
}
