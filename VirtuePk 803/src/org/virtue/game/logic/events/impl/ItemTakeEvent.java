package org.virtue.game.logic.events.impl;

import org.virtue.game.logic.World;
import org.virtue.game.logic.events.CoordinateEvent;
import org.virtue.game.logic.item.GroundItem;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.region.Region;

public class ItemTakeEvent extends CoordinateEvent {
	
	private GroundItem item;	

	public ItemTakeEvent(GroundItem item) {
		super(item.getTile(), 1, 1);
		this.item = item;
	}
	
	@Override
	public boolean processEvent(Player player) {
		//System.out.println("Processing item take event...");
		Region region = World.getWorld().getRegionManager().getRegionByID(tile.getRegionID());
		if (region == null || !region.containsItem(item)) {
			player.getUpdateArchive().getMovement().resetWalkSteps();
			return true;
		}
		return super.processEvent(player);
	}

	@Override
	public void run(Player player) {
		Region region = World.getWorld().getRegionManager().getRegionByID(tile.getRegionID());
		region.removeItem(item);
		player.getInventory().add(item);
	}

}
