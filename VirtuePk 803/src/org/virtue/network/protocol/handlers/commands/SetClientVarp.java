package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.node.entity.player.Player;
import org.virtue.network.protocol.messages.VarpMessage;

public class SetClientVarp implements Command {

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		int key, value;
		try {
			key = Integer.parseInt(args[0]);
			value = Integer.parseInt(args[1]);
		} catch (Exception ex) {
			return false;
		}
		player.getPacketDispatcher().dispatchVarp(new VarpMessage(key, value));
		System.out.println("Sending varp to client: key="+key+", value="+value);
		return true;
	}

	@Override
	public String[] getPossibleSyntaxes() {
		return new String[] { "setclientvarp", "setvarp" };
	}

}
