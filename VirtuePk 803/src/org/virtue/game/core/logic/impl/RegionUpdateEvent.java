package org.virtue.game.core.logic.impl;

import org.virtue.game.core.logic.LogicEvent;
import org.virtue.game.logic.World;
import org.virtue.utility.GameClock;

public class RegionUpdateEvent extends LogicEvent {

	@Override
	public void run() {
		World.getWorld().getRegionManager().runRegionTick();//TODO: Find out where this is supposed to be handled
	}

	@Override
	public long getIntervalDelay() {
		return GameClock.ONE_TICK;
	}

	@Override
	public long getInitialDelay() {
		return 200;
	}

	@Override
	public boolean singleShotEvent() {
		return false;
	}

}
