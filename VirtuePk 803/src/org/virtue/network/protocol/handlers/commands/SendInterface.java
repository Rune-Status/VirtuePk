package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.RSInterface;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;

public class SendInterface implements Command {

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		int id = 0;
		int component = 236;
		if (args.length < 1) {
			return false;
		}
		try {
			id = Integer.parseInt(args[0]);
			if (args.length >= 2) {
				component = Integer.parseInt(args[1]);
			}
		} catch (Exception ex) {
			player.getPacketDispatcher().dispatchMessage("Invalid interface command: requires 1 int paramater", MessageOpcode.CONSOLE);
			return false;
		}
		player.getInterfaces().sendInterface(false, RSInterface.GAME_SCREEN, component, id);
		return false;
	}

	@Override
	public String[] getPossibleSyntaxes() {
		return new String[] { "interface", "iface" };
	}

}
