package org.virtue.engine.logic.impl;

import org.virtue.engine.logic.LogicEvent;
import org.virtue.utility.GameClock;

/**
 * Handles the update of multiple entities in the game world, which includes
 * building the update packet and refreshing other details.
 * 
 * @author Taylor Moon
 * @since Jan 24, 2014
 */
public class CharacterLogicUpdateEvent extends LogicEvent {

	@Override
	public void run() {
//		try {
//			for (Player player : World.getWorld().getPlayers()) {
//				player.onCycle();
//				player.update();
//				player.refreshOnDemand();
//			}
//			for (NPC npc : World.getWorld().getNpcs()) {
//				npc.onCycle();
//				npc.update();
//				npc.refreshOnDemand();
//			}
//		} catch (Exception e) {
//			Launcher.getEngine().handleException(e);
//		}
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
