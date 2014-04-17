package org.virtue.game.logic.item;

import org.virtue.game.logic.node.entity.region.Tile;

/**
 * @author Taylor
 * @date Jan 21, 2014
 */
public class GroundItem extends Item {

	/**
	 * Represents the tile.
	 */
	private Tile tile;
	
	/**
	 * Constructs a new {@code GroundItem.java}.
	 * @param tile The tile.
	 */
	public GroundItem(int id, int amount, Tile tile) {
		super(id, amount);
		this.tile = tile;
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
}
