package org.virtue.game.logic.content.skills;

public class SkillData {

	private int experience = 0;
	private int currentLevel = 0;
	private int baseLevel = 1;
	private final Skill skill;
	
	public static int[] levelXpNeeded = new int[120];
	
	static {
		int xp = 0;
		for (int level = 1; level <= 120; level++) {
		    int difference = (int) ((double) level + 300.0 * Math.pow(2.0, (double) level / 7.0));
		    xp += difference;
		    levelXpNeeded[level-1] = xp / 4;
		    //System.out.println("Level "+level+" has "+(xp/4)+" experience");
		}
	}
	
	public SkillData (Skill skill) {
		this(skill, 0, 1);
	}
	
	public SkillData (Skill skill, int xp, int level) {
		this.skill = skill;
		this.experience = xp;
		this.currentLevel = level;
		calculateBaseLevel();
	}
	
	/**
	 * Gets the id of the skill (the id should be between 1 and the total number of skills)
	 * @return	The id.
	 */
	public Skill getSkill () {
		return skill;
	}
	
	/**
	 * Gets the experience the player has in the skill
	 * @return	The experience.
	 */
	public int getExperience () {
		return experience;
	}
	
	/**
	 * Gets the current boosted/reduced skill level for the player.
	 * @return	The level.
	 */
	public int getCurrentLevel () {
		return currentLevel;
	}
	
	/**
	 * Gets the player's current base level
	 * @return	The base level
	 */
	public int getBaseLevel () {
		return baseLevel;
	}
	
	/**
	 * Increments the current experience by the specified amount in the format "float". 
	 * As experience is stored as an integer, this will multiply the amount by ten and remove any decimals
	 * @param xpToAdd	The amount of xp to add, represented as a float.
	 */
	public void addExperienceFloat (float xpToAdd) {
		addExperience((int) xpToAdd*10);
	}
	
	/**
	 * Increments the current experience by the specified amount
	 * @param xpToAdd	The amount of xp to add
	 */
	public void addExperience (int xpToAdd) {
		experience += xpToAdd;
		if (experience < 0) {
			experience = 0;
		}	
		calculateBaseLevel();
	}
	
	private void calculateBaseLevel () {		
		for (int i =0;i<skill.getMaxLevel();i++) {
			if ((experience/10) >= levelXpNeeded[i]) {
				continue;
			} else {
				baseLevel = i;
				return;
			}
		}
		baseLevel = skill.getMaxLevel();
	}
	
	public void setCurrentLevel (int level) {
		if (level > Byte.MAX_VALUE || level < 0) {
			throw new IllegalArgumentException("Level must be between 0 and "+Byte.MAX_VALUE);
		}
		currentLevel = level;
	}
	
}
