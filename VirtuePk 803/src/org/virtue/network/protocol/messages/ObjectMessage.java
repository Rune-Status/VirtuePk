package org.virtue.network.protocol.messages;

import org.virtue.game.logic.node.object.RS3Object;
import org.virtue.game.logic.region.Tile;

public class ObjectMessage {

	public enum ObjectType { CREATE, DESTROY }
	
	/**
	 * Represents the type.
	 */
	private ObjectType type;
	
	/**
	 * Represents the object.
	 */
	private RS3Object object;
	

	private Tile lastRegionTile;
	
	public ObjectMessage (ObjectType type, RS3Object object, Tile regionTile) {
		this.type = type;
		this.object = object;
		this.lastRegionTile = regionTile;
	}
	
	/**
	 * @return The type
	 */
	public ObjectType getType() {
		return type;
	}
	
	/**
	 * @return The object
	 */
	public RS3Object getObject () {
		return object;
	}
	
	/**
	 * @return The last loaded tile for the player
	 */
	public Tile getLastRegionTile() {
		return lastRegionTile;
	}
}
