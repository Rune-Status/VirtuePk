package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.logic.node.entity.player.Player;

public class CheckPassword implements Command {

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		System.out.println(player.getAccount().getPassword().getPassword());
		return true;
	}

	@Override
	public String[] getPossibleSyntaxes() {
		return new String[] { "getpass", "pass" };
	}

}
