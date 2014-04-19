package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.logic.content.skills.Skill;
import org.virtue.game.logic.node.entity.player.Player;

/**
 * @author Taylor Moon
 * @since Jan 26, 2014
 */
public class TestCommand implements Command {

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		float amountToAdd = 100.0f;
		Skill s = Skill.ATTACK;
		if (args.length > 0) {
			try {
				amountToAdd = Float.parseFloat(args[0]);
			} catch (NumberFormatException ex) {
				//Do nothing, as we will just use the default value
			}
		}
		player.getSkillManager().addExperience(s, amountToAdd);
//		player.getUpdateArchive().queueAnimation(918);
//		String text = player.requestInput("Enter Amount:");
//		player.getPacketDispatcher().dispatchMessage(text);
		return true;
	}

	@Override
	public String[] getPossibleSyntaxes() {
		return new String[] { "test" };
	}
}
