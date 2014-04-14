package org.virtue.network.protocol.handlers.impl;

import org.virtue.game.World;
import org.virtue.game.node.entity.player.Player;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.session.WorldSession;

public class PlayerOptionHandler extends PacketHandler<WorldSession> {

	@Override
	public void handle(WorldSession session) {
		int playerIndex = getFlag("index", 0);
		boolean forceRun = getFlag("forceRun", false);
		int option = getFlag("option", 0);
		
		if (option < 1 || option > 10 || playerIndex < 1 || playerIndex > 2048) {//TODO: Replace with max player constant
			throw new RuntimeException("Invalid paramaters: option="+option+", playerIndex="+playerIndex);
		}
		
		Player player = World.getWorld().getPlayer(playerIndex);
		if (player == null) {
			//Player does not exist; ignore request
			return;
		}
		System.out.println("Received player action: option="+option+", playerIndex="+playerIndex+", player="+player.getAccount().getUsername().getName()+", forceRun="+forceRun);
	}

}
