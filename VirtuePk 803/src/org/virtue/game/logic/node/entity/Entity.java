package org.virtue.game.logic.node.entity;

import java.util.List;

import org.virtue.game.logic.node.Node;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.update.UpdateBlockArchive;
import org.virtue.game.logic.node.entity.player.update.masks.Bar;
import org.virtue.game.logic.node.entity.region.LandscapeRepository;
import org.virtue.game.logic.node.entity.region.Tile;


/**
 * @author Taylor
 * @date Jan 13, 2014
 */
public abstract class Entity extends Node {
	

	private int hitpoints;
	
	private int maxHitpoints;	

	private int direction;
	
	/**
	 * Properties class
	 */
	public Properties properties;
	
	/**
	 * The last loaded map region
	 */
	protected Tile lastLoadedRegion;
	
	/**
	 * Represents the last tile.
	 */
	protected Tile lastTile;
	
	/**
	 * Represents the update archive.
	 */
	private UpdateBlockArchive updateArchive;

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
	 * Bar
	 */
	private transient List<Bar> cachedBars;
	
	public Entity () {
		updateArchive = new UpdateBlockArchive(this);
	}
	
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

	/**
	 * @return the updateArchive
	 */
	public UpdateBlockArchive getUpdateArchive() {
		return updateArchive;
	}

	/**
	 * @param updateArchive the updateArchive to set
	 */
	public void setUpdateArchive(UpdateBlockArchive updateArchive) {
		this.updateArchive = updateArchive;
	}

	/**
	 * @return	The direction this entity is facing
	 */
	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public void appendBar(Bar... bars) {
		for (Bar bar : bars) {
			if (bar == null) {
				continue;
			}
			this.cachedBars.add(bar);
		}
	}
	
	public void loadMapRegion () {
		lastLoadedRegion = new Tile(getTile());
	}
	
	/**
	 * Represents if a map update is required.
	 * @return True if so; false otherwise.
	 */
	public boolean needsMapUpdate () {
		int lastRegionX = lastLoadedRegion.getChunkX(); 
		int lastRegionY = lastLoadedRegion.getChunkY();
		int regionX = getTile().getChunkX();
		int regionY = getTile().getChunkY();
		int size = ((LandscapeRepository.REGION_SIZES[0] >> 3) / 2) - 1;		
		return Math.abs(lastRegionX - regionX) >= size || Math.abs(lastRegionY - regionY) >= size;
	}
	
	public Tile getLastLoadedRegion () {
		return lastLoadedRegion;
	}
	
	/**
	 * @return
	 */
	public Properties getProperties() {
		return properties;
	}
	public int getHitpoints() {
		return hitpoints;
	}

	public void setHitpoints(int hitpoints) {
		this.hitpoints = hitpoints;
	}

	public void removeHitpoints(int hitpoints) {
		this.hitpoints -= hitpoints;
	}

	public int getMaxHitpoints() {
		return maxHitpoints;
	}
}
