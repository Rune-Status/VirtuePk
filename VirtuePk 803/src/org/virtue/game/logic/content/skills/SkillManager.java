package org.virtue.game.logic.content.skills;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.update.masks.Graphics;
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.game.logic.node.interfaces.RSInterface;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SkillManager extends AbstractInterface {

	private Player player;
	
	private SkillData[] skills = new SkillData[Skill.values().length];

	//Varp IDs used:
	private static final int XP_COUNTER_1_VALUE = 91;
	private static final int XP_COUNTER_2_VALUE = 92;
	private static final int XP_COUNTER_3_VALUE = 93;
	
	public SkillManager (Player player) {
		super(RSInterface.SKILLS, player);
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
	
	public void addExperience (Skill s, float amountToAdd) {
		SkillData skill = skills[s.getID()];
		int levelBefore = skill.getBaseLevel();
		skill.addExperienceFloat(amountToAdd);
		int levelAfter = skill.getBaseLevel();
		//System.out.println("Level before: "+levelBefore+", level after: "+levelAfter);
		if (levelAfter > levelBefore) {//Player has advanced in level
			handleAdvancement(skill, (levelAfter - levelBefore));
		}
		player.getPacketDispatcher().dispatchSkill(skill);
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
				skills[s.getID()] = new SkillData(s, 11840, 10);
			} else {
				skills[s.getID()] = new SkillData(s);
			}
		}
	}
	
	public void sendAllSkills () {
		for (SkillData skill : skills) {
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
		for (SkillData s : skills) {
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
	public void deserialise (Player player) {
		JsonArray skillsData = player.getAccount().getCharFile().get("skills").getAsJsonArray();
		for (JsonElement skillData : skillsData) {
			JsonObject data = (JsonObject) skillData;
			Skill s = Skill.getSkill(data.get("id").getAsInt());
			int xp = data.get("xp").getAsInt();
			int level = data.get("level").getAsInt();
			skills[s.getID()] = new SkillData(s, xp, level);
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
