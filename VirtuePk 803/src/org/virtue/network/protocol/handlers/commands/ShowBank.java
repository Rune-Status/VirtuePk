package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.logic.node.entity.player.Player;

public class ShowBank implements Command {

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		player.getInterfaces().setInterface(player.getBank(), 13, false);
		return true;
	}

	@Override
	public String[] getPossibleSyntaxes() {
		return new String[] { "bank" };
	}

}
