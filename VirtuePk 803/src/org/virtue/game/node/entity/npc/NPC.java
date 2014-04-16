package org.virtue.game.node.entity.npc;

import org.virtue.game.node.entity.Entity;
import org.virtue.game.node.entity.region.Tile;

/**
 * @author Taylor
 * @date Jan 13, 2014
 */
public class NPC extends Entity {
	
	/**
	 * Represents the tile.
	 */
	private Tile tile;
	
	/**
	 * Represents the id.
	 */
	private int id;

	/**
	 * Constructs a new {@code NPC.java}.
	 * @param id The id.
	 * @param tile The tile.
	 */
	public NPC(int id, Tile tile) {
		this.id = id;
		this.tile = tile;
	}
	
	@Override
	public void start() {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void onCycle() {
	}

	/**
	 * @return the tile.
	 */
	public Tile getTile() {
		return tile;
	}
	
	/**
	 * @return the id.
	 */
	public int getId() {
		return id;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public void refreshOnDemand() {
	}

	@Override
	public void update() {
	}
}
