package org.virtue.game.logic.region;

import org.virtue.game.core.gametick.Tick;
import org.virtue.game.core.gametick.TickState;
import org.virtue.game.logic.World;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class TempObjectUpdate extends Tick {
	
	private final int regionID;
	
	public TempObjectUpdate (int regionID) {
		this.regionID = regionID;
	}

	@Override
	public TickState onTick() {
		System.out.println("Checking temporary objects");
		Region region = World.getWorld().getRegionManager().getRegionByID(regionID);
		if (region == null) {
			return TickState.DESTROYED;
		}
		region.checkTempObjects();
		return TickState.ALIVE;
	}
	
}
