package org.virtue.game.logic.events;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.region.Tile;

public abstract class CoordinateEvent {
	
	protected final Tile tile;
	
	private int sizeX;
	
	private int sizeY;
	
	/**
	 * Constructs an event which runs when the player reaches the specified location
	 * @param tile		The target tile
	 * @param event		The event to run
	 * @param sizeX		The x-size of the object located at the target tile
	 * @param sizeY		The y-size of the object located at the target tile
	 */
	public CoordinateEvent (Tile tile, int sizeX, int sizeY) {
		this.tile = tile;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}
	
	public boolean processEvent(Player player) {
		if (player.getTile().getPlane() != tile.getPlane()) {
			return true;
		}
		int distanceX = player.getTile().getX() - tile.getX();
		int distanceY = player.getTile().getY() - tile.getY();
		if (distanceX > sizeX || distanceX < -1 || distanceY > sizeY
				|| distanceY < -1) {
			return cantReach(player);
		}
		if (player.getUpdateArchive().getMovement().hasWalkSteps()) {
			player.getUpdateArchive().getMovement().reset();
		}
		run(player);
		return true;
	}
	
	public abstract void run(Player player);

	public boolean cantReach(Player player) {
		if (!player.getUpdateArchive().getMovement().hasWalkSteps() && player.getUpdateArchive().getMovement().getNextWalkDirection() == -1) {
			player.getPacketDispatcher().dispatchMessage("You can't reach that.");
			return true;
		}
		return false;
	}

}
