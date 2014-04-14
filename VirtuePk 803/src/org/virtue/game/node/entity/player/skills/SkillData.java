package org.virtue.game.node.entity.player.skills;

public class SkillData {

	private int experience = 0;
	private int currentLevel = 0;
	private final Skill skill;
	
	public SkillData (Skill skill) {
		this(skill, 0, 1);
	}
	
	public SkillData (Skill skill, int xp, int level) {
		this.skill = skill;
		this.experience = xp;
		this.currentLevel = level;
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
	
}
