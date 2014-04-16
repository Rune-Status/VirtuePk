package org.virtue.game.node.entity.region;

import java.util.ArrayDeque;
import java.util.Queue;

import org.virtue.Launcher;
import org.virtue.engine.logic.LogicEvent;

/**
 * @author Taylor Moon
 * @since Jan 25, 2014
 */
public class RegionUpdateManager extends LogicEvent {
	
	/**
	 * Represents a queue of pending region updates.
	 */
	private final Queue<RegionUpdateEvent> pending_updates = new ArrayDeque<>();

	@Override
	public void run() {
		if (pending_updates.isEmpty()) {
			return;
		}
		RegionUpdateEvent event = getPendingRegions().poll();
		if (event == null) {
			return;
		}
		try {
			// Initialize the update process.
			event.getRegion().preUpdate();

			// Update the actual region.
			event.getRegion().update();

			// End and configure the update process.
			event.getRegion().postUpdate();
		} catch (Exception e) {
			Launcher.getEngine().handleException(e);
		}
		Launcher.getEngine().getAsycnhronousPool().execute(event.getEvent());
	}

	@Override
	public long getIntervalDelay() {
		return 1;
	}

	@Override
	public long getInitialDelay() {
		return 0;
	}

	@Override
	public boolean singleShotEvent() {
		return false;
	}

	/**
	 * @return the pending_updates
	 */
	public Queue<RegionUpdateEvent> getPendingRegions() {
		return pending_updates;
	}

}
