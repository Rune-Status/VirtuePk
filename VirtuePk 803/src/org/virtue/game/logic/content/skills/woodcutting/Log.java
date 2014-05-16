package org.virtue.game.logic.content.skills.woodcutting;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public enum Log {
	NORMAL(1, 25, 1511, 20, 4, 8, 0),
	OAK(15, 37.5, 1521, 30, 4, 15, 15),
	WILLOW(30, 67.5, 1519, 60, 4, 51, 15),
	TEAK(35, 85, 1519, 60, 4, 51, 10),//TODO: Find respawn delays, item IDs and chop times
	MAPLE(45, 100, 1517, 83, 16, 72, 10),
	MAHOGANY(50, 125, 1519, 60, 4, 51, 10),//TODO: Find respawn delays, item IDs and chop times
	YEW(60, 175, 1515, 120, 17, 94, 10),
	MAGIC(75, 250, 1513, 150, 21, 121, 10),
	ELDER(90, 325, 1513, 150, 21, 121, 0);//TODO: Find respawn delays, item IDs and chop times
	
	private final int levelRequired;
	private final int itemID;
	private final double experience;
	private final int baseTime, randomTime, respawnDelay;
	private final int randomLifeProbability;
	
	Log (int level, double xp, int logID, int logBaseTime, int logRandomTime, int respawnDelay, int randomLifeProbability) {
		this.levelRequired = level;
		this.experience = xp;
		this.itemID = logID;
		this.baseTime = logBaseTime;
		this.randomTime = logRandomTime;
		this.respawnDelay = respawnDelay;
		this.randomLifeProbability = randomLifeProbability;
	}
	
	/**
	 * Gets the level needed to chop this log from a tree
	 * @return	The level needed
	 */
	public int getLevel () {
		return levelRequired;
	}
	
	/**
	 * Gets the item ID of the log
	 * @return	The item ID
	 */
	public int getLogID () {
		return itemID;
	}
	
	/**
	 * Gets the amount of experience gained when a log is successfully chopped
	 * @return	The experience gained
	 */
	public double getExperience () {
		return experience;
	}
	
	/**
	 * Gets the number of ticks before the tree can respawn
	 * @return	The number of ticks before the tree can respawn
	 */
	public int getRespawnTime () {
		return respawnDelay;
	}
	
	/**
	 * Gets the maximum number of ticks needed in order to chop a log from this tree
	 * @return	The maximum number of ticks
	 */
	public int getChopMaxTime () {
		return baseTime;
	}
	
	public int getRandomTime () {
		return randomTime;
	}

	public int getRandomLifeProbability() {
		return randomLifeProbability;
	}
}
