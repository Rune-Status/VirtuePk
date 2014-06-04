package org.virtue.game.logic.content.skills.woodcutting;

import org.virtue.game.logic.item.Logs.LogType;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public enum TreeDefinition {
	//65256 - Dark tree stump (magic?)
	//58131, 58132, 58134 - Swamp tree stumps?
	//43493, 31296 - Blood tree stump
	//11059 - Large black tree stump
	//4328, 4329 - Large tree stumps
	NORMAL(1, 25, LogType.NORMAL, 20, 4, 8, 0, 40350),//Also 40350-40357
	OAK(15, 37.5, LogType.OAK, 30, 4, 15, 15, 78118),//Also includes 78119
	WILLOW(30, 67.5, LogType.WILLOW, 60, 4, 51, 15, 11855),
	TEAK(35, 85, LogType.TEAK, 60, 4, 51, 10, 9037),//TODO: Find respawn delays and chop times
	MAPLE(45, 100, LogType.MAPLE, 83, 16, 72, 10, 31057),
	//Mahogany - 9035? Teak - 9037?
	MAHOGANY(50, 125, LogType.MAHOGANY, 83, 16, 72, 10, 40350),//TODO: Find respawn delays, proper stump ID and chop times
	YEW(60, 175, LogType.YEW, 120, 17, 94, 10, 4329),//TODO: Find stump ID
	MAGIC(75, 250, LogType.MAGIC, 150, 21, 121, 10, 63179),//37824
	ELDER(90, 325, LogType.ELDER, 150, 21, 121, 0, -1);//TODO: Find respawn delays and chop times
	
	private final int levelRequired;
	private final LogType logs;
	private final double experience;
	private final int baseTime, randomTime, respawnDelay;
	private final int randomLifeProbability;
	private final int stumpID;
	
	TreeDefinition (int level, double xp, LogType logs, int logBaseTime, int logRandomTime, int respawnDelay, int randomLifeProbability, int stumpID) {
		this.levelRequired = level;
		this.experience = xp;
		this.logs = logs;
		this.baseTime = logBaseTime;
		this.randomTime = logRandomTime;
		this.respawnDelay = respawnDelay;
		this.randomLifeProbability = randomLifeProbability;
		this.stumpID = stumpID;
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
	public LogType getLogType () {
		return logs;
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
	 * Gets the object ID of the default stump for the tree associated with this log.
	 * @return	The stump ID
	 */
	public int getStumpID () {
		return stumpID;
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
