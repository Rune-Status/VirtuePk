package org.virtue.game.core.logic.impl;

import org.virtue.Launcher;
import org.virtue.game.core.logic.LogicEvent;
import org.virtue.game.logic.World;
import org.virtue.game.logic.node.entity.npc.NPC;
import org.virtue.game.logic.node.entity.player.Player;
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
		try {
			for (Player player : World.getWorld().getPlayers()) {
				player.onCycle();
				player.update();
				player.refreshOnDemand();
			}
			for (NPC npc : World.getWorld().getNpcs()) {
				npc.onCycle();
				npc.update();
				npc.refreshOnDemand();
			}
			World.getWorld().getRegionManager().runRegionTick();//TODO: Find out where this is supposed to be handled
		} catch (Exception e) {
			Launcher.getEngine().handleException(e);
		}
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
