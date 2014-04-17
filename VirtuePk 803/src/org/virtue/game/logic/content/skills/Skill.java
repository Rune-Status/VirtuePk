package org.virtue.game.logic.content.skills;

public enum Skill {
	ATTACK(0, "Attack"),
	DEFENCE(1, "Defence"),
	STRENGTH(2, "Strength"),
	CONSTITUTION(3, "Constitution"),
	RANGED(4, "Ranged"),
	PRAYER(5, "Prayer"),
	MAGIC(6, "Magic"),
	COOKING(7, "Cooking"),
	WOODCUTTING(8, "Woodcutting"),
	FLETCHING(9, "Fletching"),
	FISHING(10, "Fishing"),
	FIREMAKING(11, "Firemaking"),
	CRAFTING(12, "Crafting"),
	SMITHING(13, "Smithing"),
	MINING(14, "Mining"),
	HERBLORE(15, "Herblore"),
	AGILITY(16, "Agility"),
	THIEVING(17, "Thieving"),
	SLAYER(18, "Slayer"),
	FARMING(19, "Farming"),
	RUNECRAFTING(20, "Runecrafting"),
	HUNTER(21, "Hunter"),
	CONSTRUCTION(22, "Construction"),
	SUMMONING(23, "Summoning"),
	DUNGEONEERING(24, "Dungeoneering", 120),
	DIVINATION(25, "Divination");
	
	private final String name;
	private final int skillID;
	private final int maxLevel;
	
	Skill (int id, String name) {
		this(id, name, 99);
	}
	
	Skill (int id, String name, int maxLevel) {
		this.skillID = id;
		this.name = name;
		this.maxLevel = maxLevel;
	}
	
	/**
	 * Gets the id of the skill
	 * @return	The id.
	 */
	public int getID () {
		return skillID;
	}
	
	/**
	 * Gets the name of the skill
	 * @return	The name.
	 */
	public String getName () {
		return name;
	}
	
	/**
	 * Gets the maximum level for the skill
	 * @return	The maximum level.
	 */
	public int getMaxLevel () {
		return maxLevel;
	}
	
	public static Skill getSkill (int id) {
		Skill skill = Skill.values()[id];
		if (skill.skillID == id) {
			return skill;
		} else {
			for (Skill s : Skill.values()) {
				if (s.skillID == id) {
					return s;
				}
			}
			return null;
		}
	}
}
