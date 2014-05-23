package org.virtue.game.logic.content.skills.fishing;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public enum Fish {
	SHRIMP(317, 1, 10, 1),
	ANCHOVIES(321, 15, 40, 1),
	SARDINES(327, 5, 20, 1),
	HERRING(345, 10, 30, 1);
	
	private final int id, level;
	private final double xp;
	
	/**
	 * Represents a type of fish
	 * @param id	The item ID for the raw fish
	 * @param level	The level required to catch the fish
	 * @param xp	The amount of xp gained if the fish is caught
	 * @param ratio	The ratio of the fish of this type caught compared to fish of other types at the same spot
	 */
	Fish(int id, int level, double xp, int ratio) {
		this.id = id;
		this.level = level;
		this.xp = xp;
	}
	
	public int getID () {
		return id;
	}
}
