package org.virtue.game.core.logic.impl;

import org.virtue.game.core.logic.LogicEvent;
import org.virtue.game.logic.World;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.utility.GameClock;

public class SystemUpdateEvent extends LogicEvent {
	
	private int delay;
	
	private boolean hasRun = false;
	
	public SystemUpdateEvent (int delay) {
		this.delay = delay;
	}

	@Override
	public void run() {
		if (hasRun) {
			return;
		}
		int sysUpdateDelay = World.getWorld().getSystemUpdateDelay();
		if (sysUpdateDelay != -1 && sysUpdateDelay <= 0) {
			for (Player p : World.getWorld().getPlayers()) {
				p.sendLogout(false);
			}
			World.getWorld().setOnline(false);
			if (future != null) {
				future.cancel(false);
			}
			hasRun = true;
		}
	}

	@Override
	public long getIntervalDelay() {
		return GameClock.ONE_TICK;
	}

	@Override
	public long getInitialDelay() {
		return delay;
	}

	@Override
	public boolean singleShotEvent() {
		return hasRun;
	}

}
