package org.virtue.game.logic.node.object;

import org.virtue.cache.def.ObjectDefinition;
import org.virtue.cache.def.ObjectDefinitionLoader;
import org.virtue.game.logic.node.Node;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.identity.Rank;
import org.virtue.game.logic.node.interfaces.impl.Bank;
import org.virtue.game.logic.region.Tile;

/**
 * @author Taylor
 * @date Jan 21, 2014
 */
public class RS3Object extends Node {
	
	/**
	 * Represents the ID of this object.
	 */
	private int id;
	
	/**
	 * Represents the rotation of this object.
	 */
	private int rotation;
	
	/**
	 * Represents the type of this object.
	 */
	private int type;
	
	/**
	 * Represents the tile.
	 */
	private Tile tile;
	
	private ObjectDefinition definition;
	
	/**
	 * Constructs a new {@code RS3Object.java}.
	 * @param id The id.
	 * @param rotation The rotation.
	 * @param type The type.
	 */
	public RS3Object(int id, int rotation, int type, Tile tile) {
		this.id = id;
		this.rotation = rotation;
		this.type = type;
		this.tile = tile;
		this.definition = ObjectDefinitionLoader.forId(id);
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the rotation
	 */
	public int getRotation() {
		return rotation;
	}

	/**
	 * @param rotation the rotation to set
	 */
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the tile
	 */
	public Tile getTile() {
		return tile;
	}

	/**
	 * @param tile the tile to set
	 */
	public void setTile(Tile tile) {
		this.tile = tile;
	}
	
	public int getSizeX() {
		return (rotation != 1 && rotation != 3) ? definition.getSize()[0] : definition.getSize()[1];
	}
	
	public int getSizeY() {
		return (rotation != 1 && rotation != 3) ? definition.getSize()[1] : definition.getSize()[0];
	}
	
	/**
	 * @return	The cache definition for the object
	 */
	public ObjectDefinition getDefinition () {
		return definition;
	}
	
	/**
	 * Returns whether the given option can be handled at a distance
	 * @param option	The option to check
	 * @return			True if the option can be handled at a distance, false if the player must be adjacent first
	 */
	public boolean isDistanceOption (ObjectOption option) {
		return option.equals(ObjectOption.EXAMINE);
	}
	
	/**
	 * Handles the player interaction with this object
	 * @param player	The player interacting with the object
	 * @param option	The selected interaction option
	 */
	public void interact (Player player, ObjectOption option) {
		if (Bank.isBankBooth(this) && option.equals(ObjectOption.TWO)) {
			player.getInterfaces().openBank();
			return;
		}
		String message = "Clicked object: objectID="+id+", xCoord="+getTile().getX()+", yCoord="+getTile().getX()+", optionID="+option.getID();
		System.out.println(message);
		if (player.getAccount().getRank().equals(Rank.ADMINISTRATOR)) {
			player.getPacketDispatcher().dispatchMessage(message);
		}
	}

}
