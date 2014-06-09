package org.virtue.game.logic.events.impl;

import org.virtue.Constants;
import org.virtue.game.logic.events.CoordinateEvent;
import org.virtue.game.logic.node.entity.Entity;
import org.virtue.game.logic.node.entity.player.Player;

/**
 * 
 * @author Virtue Development Team 2014 (c).
 */
public abstract class EntityInteractEvent extends CoordinateEvent {
	
	private Entity entity;

	public EntityInteractEvent(Entity entity) {
		super(entity.getTile(), entity.getSize(), entity.getSize());
		this.entity = entity;
	}
	
	@Override 
	public boolean processEvent(Player player) {
		if (!entity.getTile().equals(entity.getLastTile())) {
			this.tile = entity.getTile();
			player.getUpdateArchive().getMovement().reset();
			player.getUpdateArchive().getMovement().calculateWalkStepsInteract(
					entity.getTile(), Constants.MAX_WALK_STEPS, entity.getSize());
		}
		return super.processEvent(player);
	}

	@Override
	public abstract void run(Player player);

}
