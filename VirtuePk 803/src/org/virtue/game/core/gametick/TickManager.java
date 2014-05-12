package org.virtue.game.core.gametick;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Queue;

import org.virtue.Launcher;

/**
 * @author Taylor Moon
 * @since Jan 23, 2014
 */
public class TickManager {
	
	/**
	 * Represents the ticks to be added.
	 */
	private final Queue<Tick> awaitingTicks = new ArrayDeque<>();
	
	/**
	 * Represents a list of ticks in the repository to be updated and processed.
	 */
	private final Collection<Tick> ticks = Collections.synchronizedList(new ArrayList<Tick>());
	
	/**
	 * Loads the default ticks.
	 */
	public void loadDefaults() {
		ticks.add(Launcher.getEngine().getLogicProcessor());
	}
	
	/**
	 * Called when the ticks in the collection should be executed and updated.
	 * Any ticks that should be destroyed or repressed are moved to separate
	 * lists that are flagged as such category.
	 */
	public void processAllTicks() {
		//synchronized (ticks) { (Do not attempt to synchronise an already synchronised list...
			try {
				if (!awaitingTicks.isEmpty()) {
					ticks.addAll(awaitingTicks);
				}
				//System.out.println(ticks.size()+" total tasks.");
				for (Tick tick : ticks) {
					//System.out.println("Task: "+tick.getClass().getName());
					if (tick == null) {
						continue;
					}
					TickState state = tick.onTick();
					if (tick.hasGenericTickState()) {
						state = tick.getGenericTickState();
					}
					if (state == null) {
						continue;
					}
					switch (state) {
					case ALIVE:
						continue;
					case DESTROYED:
						ticks.remove(tick);
						break;
					default:

					}
				}
			} catch (Exception e) {
				Launcher.getEngine().handleException(e);
			}
		//}
	}
	
	/**
	 * Registers a tick into the collection.
	 * @param tick The tick to be registered.
	 * @return The succession result.
	 */
	public boolean register(Tick tick) {
		if (ticks.contains(tick) || awaitingTicks.contains(tick)) {
			return false;
		}
		awaitingTicks.add(tick);
		return true;
	}
}
