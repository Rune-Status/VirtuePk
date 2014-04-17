package org.virtue.game.logic.node.entity.npc;

import org.virtue.game.logic.node.entity.Entity;
import org.virtue.game.logic.node.entity.region.Tile;

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

	private boolean exists = true;

	/**
	 * Constructs a new {@code NPC.java}.
	 * @param id The id.
	 * @param tile The tile.
	 */
	public NPC(int id, Tile tile) {
		super();
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
		getUpdateArchive().getMovement().process();
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
		return exists;
	}

	@Override
	public void refreshOnDemand() {
		getUpdateArchive().reset();//Refresh update flags
	}

	@Override
	public void update() {
	}
}
