package org.virtue.game.node.entity;

import org.virtue.game.node.Node;
import org.virtue.game.region.Tile;

/**
 * @author Taylor
 * @date Jan 13, 2014
 */
public abstract class Entity extends Node {
	
	/**
	 * Represents the last tile.
	 */
	protected Tile lastTile;

	/**
	 * Called when this entity is created.
	 */
	public abstract void start();
	
	/**
	 * Called when this entity is destroyed.
	 */
	public abstract void destroy();
	
	/**
	 * Called evey game tick.
	 */
	public abstract void onCycle();
	
	/**
	 * Called every game tick.
	 */
	public abstract void refreshOnDemand();
	
	/**
	 * Called to check for updating.
	 * @return If this entity exitsts.
	 */
	public abstract boolean exists();
	
	/**
	 * Called to update the entity's render sequence.
	 */
	public abstract void update();

	/**
	 * @return the lastTile
	 */
	public Tile getLastTile() {
		return lastTile;
	}

	/**
	 * @param lastTile the lastTile to set
	 */
	public void setLastTile(Tile lastTile) {
		this.lastTile = lastTile;
	}
}
