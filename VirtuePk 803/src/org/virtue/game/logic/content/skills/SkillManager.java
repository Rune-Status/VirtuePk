package org.virtue.game.logic.content.skills;

import org.virtue.game.logic.node.entity.player.Player;

public class SkillManager {

	private Player player;
	
	private SkillData[] skills = new SkillData[Skill.values().length];

	//Varp IDs used:
	private static final int XP_COUNTER_1_VALUE = 91;
	private static final int XP_COUNTER_2_VALUE = 92;
	private static final int XP_COUNTER_3_VALUE = 93;
	
	public SkillManager (Player player) {
		this.player = player;
		init();
	}
	
	private void init () {
		for (Skill s : Skill.values()) {
			if (s.equals(Skill.CONSTITUTION)) {
				skills[s.getID()] = new SkillData(s, 1184, 10);
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
}
