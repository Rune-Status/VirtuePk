package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.logic.content.skills.Skill;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;

public class AddExperience implements Command {

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		if (args.length < 2) {
			player.getPacketDispatcher().dispatchMessage("Invalid command - requires 2 int paramaters (skillID, xpToAdd)", MessageOpcode.CONSOLE);
			return false;
		}
		int id, xp;
		try {
			id = Integer.parseInt(args[0]);
			xp = Integer.parseInt(args[1]);
		} catch (Exception ex) {
			player.getPacketDispatcher().dispatchMessage("Invalid command - requires 2 int paramaters (skillID, xpToAdd)", MessageOpcode.CONSOLE);
			return false;
		}
		Skill skill = Skill.getSkill(id);
		if (skill == null) {
			player.getPacketDispatcher().dispatchMessage("Invalid skill - Must be between 0 and 25", MessageOpcode.CONSOLE);
			return false;
		}
		player.getSkills().addExperience(skill, xp);
		return true;
	}

	@Override
	public String[] getPossibleSyntaxes() {
		return new String[] { "addxp", "addexperience" };
	}

}
