package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.logic.World;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.identity.Rank;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;

public class SystemUpdate implements Command {

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		if (!player.getAccount().getRank().equals(Rank.ADMINISTRATOR)) {
			return false;
		}
		int delay;
		if (args.length < 1) {
			return false;
		}
		try {
			delay = Integer.parseInt(args[0]);
		} catch (Exception ex) {
			return false;
		}
		World.getWorld().queueSystemUpdate(delay);
		player.getPacketDispatcher().dispatchMessage("System update queued for "+delay+" seconds", MessageOpcode.CONSOLE);
		return true;
	}

	@Override
	public String[] getPossibleSyntaxes() {
		return new String[] { "sysupdate" };
	}

}
