package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.node.entity.player.Player;
import org.virtue.game.node.entity.player.update.ref.Appearance;
import org.virtue.game.node.entity.player.update.ref.Appearance.Gender;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 16, 2014
 */
public class ReloadAppearance implements Command {

	public Appearance appearance;

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		appearance.setGender(Gender.MALE);
		return false;
	}

	@Override
	public String[] getPossibleSyntaxes() {
		return new String[] { "male", "female" };
	}

}
