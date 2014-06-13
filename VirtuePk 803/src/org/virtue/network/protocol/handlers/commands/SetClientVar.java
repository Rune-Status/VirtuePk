package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.network.protocol.messages.VarMessage;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;

public class SetClientVar implements Command {

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		int key, value;
		if (args.length < 2) {
			player.getPacketDispatcher().dispatchMessage("You must specifiy a var ID and a value.", MessageOpcode.CONSOLE);
			return false;
		}
		try {
			key = Integer.parseInt(args[0]);
			value = Integer.parseInt(args[1]);
		} catch (Exception ex) {
			return false;
		}
		if (syntax.equalsIgnoreCase("varc")) {
			player.getPacketDispatcher().dispatchVar(VarMessage.varc(key, value));
		} else if (syntax.equalsIgnoreCase("varbit")) {
			player.getPacketDispatcher().dispatchVar(VarMessage.varbit(key, value));
		} else {
			player.getPacketDispatcher().dispatchVar(VarMessage.varp(key, value));
		}		
		System.out.println("Sending varp to client: key="+key+", value="+value);
		return true;
	}

	@Override
	public String[] getPossibleSyntaxes() {
		return new String[] { "setclientvarp", "setvarp", "varp", "varc", "varbit" };
	}

}
