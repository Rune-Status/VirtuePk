package org.virtue.network.protocol.handlers.commands;

import java.io.IOException;
import org.virtue.Launcher;
import org.virtue.cache.def.CacheIndex;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.RS3Interface;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;

public class SendInterface implements Command {
	
	public static int INTERFACE_COUNT;
	
	static {
		try {
			INTERFACE_COUNT = Launcher.getCache().getFileCount(CacheIndex.INTERFACES);
		} catch (IOException ex) {
			INTERFACE_COUNT = -1;
		}
	}

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		int id;
		int component = 236;
		if (args.length < 1) {
			return false;
		}
		try {
			id = Integer.parseInt(args[0]);
			if (args.length >= 2) {
				component = Integer.parseInt(args[1]);
			}
		} catch (NumberFormatException ex) {
			player.getPacketDispatcher().dispatchMessage("Invalid interface command: requires 1 int paramater", MessageOpcode.CONSOLE);
			return false;
		}
		if (id == RS3Interface.GAME_SCREEN) {
			player.getPacketDispatcher().dispatchMessage("You cannot send the game screen using this method.", MessageOpcode.CONSOLE);
			return false;
		}
		if (id < 0 || id >= INTERFACE_COUNT) {
			player.getPacketDispatcher().dispatchMessage("Invalid interface ID: must be between 0 and "+INTERFACE_COUNT, MessageOpcode.CONSOLE);
			return false;
		}
		player.getInterfaces().sendInterface(false, RS3Interface.GAME_SCREEN, component, id);
		return false;
	}

	@Override
	public String[] getPossibleSyntaxes() {
		return new String[] { "interface", "iface" };
	}

}
