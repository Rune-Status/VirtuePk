package org.virtue.game.logic.content.skills.runecrafting;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 13, 2014
 */
public enum AlterDefinition {
	AIR(1, 5, 556, false, 2478, 11, 22, 33, 44, 55, 66, 77, 88, 99, 110), 
	MIND(2, 5.5, 558, false, 2479, 14, 28, 42, 56, 70, 84, 98, 112), 
	WATER(5, 6, 555, false, 2480, 19, 38, 57, 76, 95), 
	EARTH(9, 6.5, 557, false, 2481, 26, 52, 78, 104), 
	FIRE(14, 7, 554, false, 2482, 35, 70, 105), 
	BODY(20, 7.5, 559, false, 2483, 46, 92), 
	COSMIC(27, 8, 564, true, 2484, 59), 
	CHAOS(35, 8.5, 562, true, 2487, 74), 
	ASTRAL(40, 8.7, 9075, true, 17010, 82), 
	NATURE(44, 9, 561, true, 2486, 91), 
	LAW(54, 9.5, 563, true, 2485, 110), 
	DEATH(65, 10, 560, true, 2488), 
	BLOOD(77, 10.5, 565, true, 30624);

	/**
	 * Represents the required level in order to craft runes.
	 */
	private final int level;

	/**
	 * Represents the experience gained from this altar.
	 */
	private final double experience;

	/**
	 * Represents the reward id of the product.
	 */
	private final int itemID;

	/**
	 * Represents the object id of the altar.
	 */
	private final int alterID;
	
	/**
	 * Represents whether only pure essence can be used to craft this rune
	 */
	private final boolean pureEssenceOnly;
	
	/**
	 * Represents the levels at which multiple runes can be crafted
	 */
	private final int[] multiples;

	/**
	 * Constructs a new {@code AlterDefinition} {@code Object}.
	 * 
	 * @param level
	 *            the level required.
	 * @param experience
	 *            the experience.
	 * @param reward
	 *            the reward id.
	 * @param object
	 *            the object id.
	 */
	AlterDefinition(int level, double experience, int itemID, boolean pureEssence, int alterID, int... multiples) {
		this.level = level;
		this.experience = experience;
		this.itemID = itemID;
		this.pureEssenceOnly = pureEssence;
		this.alterID = alterID;
		this.multiples = multiples;
	}

	/**
	 * Gets the level required.
	 * 
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Gets the experience gained.
	 * 
	 * @return the experience
	 */
	public double getExperience() {
		return experience;
	}

	/**
	 * Gets the reward id.
	 * 
	 * @return the reward
	 */
	public int getRewardId() {
		return itemID;
	}

	/**
	 * Gets the object id.
	 * 
	 * @return the object of the altar.
	 */
	public int getObject() {
		return alterID;
	}
	
	/**
	 * Returns whether or not pure essence is required
	 * @return	True if pure essence is required, false if normal can be used
	 */
	public boolean requiresPureEssence () {
		return pureEssenceOnly;
	}
	
	public int getHighestMultiple (int level) {
		int multiple = 1;
		for (int lvl : multiples) {
			if (lvl <= level) {
				multiple++;
			}
		}
		return multiple;
	}
	
	public static AlterDefinition forAlterID (int alterID) {
		for (AlterDefinition r : AlterDefinition.values()) {
			if (r.alterID == alterID) {
				return r;
			}
		}
		return null;
	}
	
	public static AlterDefinition forItemID (int itemID) {
		for (AlterDefinition r : AlterDefinition.values()) {
			if (r.itemID == itemID) {
				return r;
			}
		}
		return null;
	}
}
