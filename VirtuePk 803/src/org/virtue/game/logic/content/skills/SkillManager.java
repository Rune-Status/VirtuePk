package org.virtue.game.logic.content.skills;

import java.util.EnumMap;

import org.virtue.Constants;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.update.masks.Graphics;
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.game.logic.node.interfaces.RS3Interface;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SkillManager extends AbstractInterface {

	private final Player player;
	private final EnumMap<Skill, SkillData> skills = new EnumMap<Skill, SkillData>(Skill.class);
	//private SkillData[] skills = new SkillData[Skill.values().length];
	
	public SkillManager (Player player) {
		super(RS3Interface.SKILLS, player);
		this.player = player;
		init();
	}

	@Override
	public void postSend() {
		sendInterfaceSettings(10, 0, 26, 30);//IfaceSettings: 96075786, 26, 0, 30		
	}
	
	/*public SkillData getSkill (Skills s) {
		return skills[s.getID()];
	}*/
	
	public void addExperience (Skill s, double totalXpToAdd) {
		addExperience(s, totalXpToAdd, 0, false);
	}
	
	public void addExperience (Skill s, double totalXpToAdd, int bonusExperience) {
		addExperience(s, totalXpToAdd, bonusExperience, false);
	}
	
	/**
	 * Adds experience to the specified skill
	 * @param skill				The skill to add experience to
	 * @param totalXpToAdd		The the amount of experience to add
	 * @param bonusExperience	The bonus experience component
	 * @param canHaveBonus		Whether global (or skill-specific) bonuses can be applied
	 */
	public void addExperience (Skill skill, double totalXpToAdd, int bonusExperience, boolean canHaveBonus) {
		totalXpToAdd = totalXpToAdd * Constants.GLOBAL_XP_MODIFIER;//Runs the xp through the global modifier
		SkillData skillData = skills.get(skill);
		int levelBefore = skillData.getBaseLevel();
		skillData.addExperienceFloat(totalXpToAdd);//TODO: Handle bonus experience
		int levelAfter = skillData.getBaseLevel();
		//System.out.println("Level before: "+levelBefore+", level after: "+levelAfter);
		if (levelAfter > levelBefore) {//Player has advanced in level
			handleAdvancement(skillData, (levelAfter - levelBefore));
		}
		player.getPacketDispatcher().dispatchSkill(skillData);
	}
	
	public int getCurrentLevel (Skill skill) {
		return skills.get(skill).getCurrentLevel();
	}
	
	public int getBaseLevel (Skill skill) {
		return skills.get(skill).getBaseLevel();
	}
	
	private void handleAdvancement (SkillData skill, int advancement) {
		boolean multiple = advancement != 1;
		String levelPart = (multiple ? advancement : skill.getSkill().startsWithVowel() ? "an" : "a")+" "+skill.getSkill().getName()+" level"+(multiple ? "s" : "");
		skill.setCurrentLevel(skill.getCurrentLevel()+advancement);
		player.getPacketDispatcher().dispatchMessage("You have advanced "+levelPart+"! You have reached level "+skill.getBaseLevel()+".");
		player.getUpdateArchive().queueGraphic(new Graphics(199));
		//You have advanced [a, an, x] [skill] level[s]! You have reached level [y].
		//Show advancement notification
		//Flash skill icon
		//Fireworks
	}
	
	private void init () {
		for (Skill s : Skill.values()) {
			if (s.equals(Skill.CONSTITUTION)) {
				skills.put(s, new SkillData(s, 11840, 10));
			} else {
				skills.put(s, new SkillData(s));
			}
		}
	}
	
	public void sendAllSkills () {
		for (SkillData skill : skills.values()) {
			player.getPacketDispatcher().dispatchSkill(skill);
		}
		//player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(6504, 1, 0, 0, 0, 4420));
	}
	
	/**
	 * Serialises the current skill data into a {@link com.google.gson.JsonArray}
	 * @return	A JsonArray containing the skill data
	 */
	public JsonArray serialise () {
		JsonArray skillsData = new JsonArray();
		for (SkillData s : skills.values()) {
			JsonObject skill = new JsonObject();
			skill.addProperty("id", s.getSkill().getID());
			skill.addProperty("xp", s.getExperience());
			skill.addProperty("level", s.getCurrentLevel());
			skillsData.add(skill);
		}
		return skillsData;
	}
	
	/**
	 * Deserialises the skill data from the specified JSON array
	 * @param skillsData	The {@link com.google.gson.JsonArray} containing the skill data
	 */
	public void deserialise (JsonArray skillsData) {
		for (JsonElement skillData : skillsData) {
			JsonObject data = (JsonObject) skillData;
			Skill s = Skill.getSkill(data.get("id").getAsInt());
			int xp = data.get("xp").getAsInt();
			int level = data.get("level").getAsInt();
			skills.put(s, new SkillData(s, xp, level));
		}
	}

	@Override
	public void handleActionButton(int component, int slot1, int slot2, ActionButton button) {
		System.out.println("Clicked skill: component="+component+", slot1="+slot1+", slot2="+slot2+", button="+button.getID());
	}

	@Override
	public int getTabID() {
		return 0;
	}
}
