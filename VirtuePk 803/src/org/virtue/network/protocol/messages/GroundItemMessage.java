package org.virtue.network.protocol.messages;

import org.virtue.game.logic.item.GroundItem;
import org.virtue.game.logic.region.Tile;

public class GroundItemMessage {
	
	/**
	 * @author Taylor
	 * @version 1.0
	 */
	public enum GroundItemType { CREATE, DESTROY }
	
	/**
	 * Represents the type.
	 */
	private GroundItemType type;
	
	/**
	 * Represents the item.
	 */
	private GroundItem item;
	
	private Tile lastRegionTile;
	
	/**
	 * Constructs a new {@code GroundItemMessage.java}.
	 * @param type The type.
	 * @param item The item.
	 */
	public GroundItemMessage(GroundItemType type, GroundItem item, Tile lastRegionTile) {
		this.type = type;
		this.item = item;
		this.lastRegionTile = lastRegionTile;
	}

	/**
	 * @return The type
	 */
	public GroundItemType getType() {
		return type;
	}

	/**
	 * @return The item
	 */
	public GroundItem getItem() {
		return item;
	}

	/**
	 * @return The last loaded tile for the player
	 */
	public Tile getLastRegionTile() {
		return lastRegionTile;
	}

}
