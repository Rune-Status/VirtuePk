package org.virtue.game.logic.social.clans;

import org.virtue.game.core.logic.LogicEvent;
import org.virtue.utility.GameClock;

public class ClanUpdateEvent extends LogicEvent {
	
	private final ClanManager clanManager;
	
	public ClanUpdateEvent (ClanManager clanManager) {
		this.clanManager = clanManager;
	}

	@Override
	public void run() {
		//Sends clan data updates to the players
		clanManager.runUpdateTasks();
		//Saves clan data to the permanent storage areas
		clanManager.runSaveTasks();//TODO: This should be run less frequently
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
