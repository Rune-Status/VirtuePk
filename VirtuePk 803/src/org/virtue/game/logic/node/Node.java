package org.virtue.game.logic.node;

import org.virtue.game.logic.node.entity.region.Tile;

/**
 * @author Taylor
 * @date Jan 13, 2014
 */
public class Node {

	/**
	 * Represents the node index.
	 */
	private int index;
	
	/**
	 * Represents the tile.
	 */
	protected Tile tile;

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		//System.out.println("Setting entity index to: "+index);
		this.index = index;
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
