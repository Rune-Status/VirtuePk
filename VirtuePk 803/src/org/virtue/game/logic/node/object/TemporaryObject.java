package org.virtue.game.logic.node.object;

import org.virtue.Launcher;
import org.virtue.game.logic.World;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.region.Tile;

public class TemporaryObject extends RS3Object {
	
	private final int replacementID;
	private final int originalID;
	private final int respawnDelay;
	private int respawnTime = 0;
	private boolean defaultDepleted = false;

	public TemporaryObject(int id, int rotation, int type, Tile tile, int replacement, int respawnDelay) {
		super(id, rotation, type, tile);
		this.originalID = id;
		this.replacementID = replacement;
		this.respawnDelay = respawnDelay;
	}

	public void deplete () {
		super.setId(replacementID);
		super.setType(10);
		World.getWorld().getRegionManager().getRegionByID(getTile().getRegionID()).updateObject(this, defaultDepleted);
	}
	
	public void respawn () {
		super.setId(originalID);
		respawnTime = 0;
		World.getWorld().getRegionManager().getRegionByID(getTile().getRegionID()).updateObject(this, !defaultDepleted);
	}
	
	public boolean checkRespawn () {
		respawnTime++;
		return respawnTime >= respawnDelay;
	}
	
	public boolean isDepleted () {
		return super.getId() != originalID;
	}
	
	@Override
	public boolean isDistanceOption (ObjectOption option) {
		return super.isDistanceOption(option);
	}
	
	@Override
	public void interact (Player player, ObjectOption option) {
		super.interact(player, option);
	}
}
