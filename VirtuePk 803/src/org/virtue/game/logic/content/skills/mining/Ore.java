package org.virtue.game.logic.content.skills.mining;

public enum Ore {
	CLAY(1, 5, 434, 10, 1, 5), 
	COPPER(1, 17.5, 436, 10, 1, 5), 
	TIN(1, 17.5, 438, 10, 1, 5), 
	IRON(15, 35, 440, 15, 1, 10), 
	SILVER(20, 40, 442, 25, 1, 20), 
	COAL(30, 50, 453, 50, 10, 30), 
	GOLD(40, 60, 444, 80, 20, 40), 
	MITHRIL(55, 80, 447, 100, 20, 60), 
	ADAMANTITE(70, 95, 449, 130, 25, 180), 
	RUNITE(85, 125, 451, 150, 30, 360);
	
	private final int levelRequired;
	private final int itemID;
	private final double experience;
	private final int baseTime, randomTime, respawnDelay;
	
	Ore (int level, double xp, int oreID, int oreBaseTime, int oreRandomTime, int respawnDelay) {
		this.levelRequired = level;
		this.experience = xp;
		this.itemID = oreID;
		this.baseTime = oreBaseTime;
		this.randomTime = oreRandomTime;
		this.respawnDelay = respawnDelay;
	}
	
	/**
	 * Gets the level needed to mine this ore
	 * @return	The level needed
	 */
	public int getLevel () {
		return levelRequired;
	}
	
	/**
	 * Gets the item ID of the ore
	 * @return	The item ID
	 */
	public int getOreID () {
		return itemID;
	}
	
	/**
	 * Gets the amount of experience gained when the ore is successfully mined
	 * @return	The experience gained
	 */
	public double getExperience () {
		return experience;
	}
	
	/**
	 * Gets the number of ticks before the rock can respawn
	 * @return	The number of ticks before the rock can respawn
	 */
	public int getRespawnTime () {
		return respawnDelay;
	}
	
	/**
	 * Gets the maximum number of ticks needed in order to mine the ore
	 * @return	The maximum number of ticks
	 */
	public int getMineMaxTime () {
		return baseTime;
	}
	
	public int getRandomTime () {
		return randomTime;
	}

}
