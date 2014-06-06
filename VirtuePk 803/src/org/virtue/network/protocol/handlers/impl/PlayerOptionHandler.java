package org.virtue.network.protocol.handlers.impl;

import org.virtue.game.logic.World;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.PlayerOption;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.session.impl.WorldSession;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 20, 2014
 */
public class PlayerOptionHandler extends PacketHandler<WorldSession> {

	@Override
	public void handle(WorldSession session) {
		int playerIndex = getFlag("playerIndex", -1);
		boolean forceRun = getFlag("forceRun", false);
		PlayerOption option = PlayerOption.fromOpcode(getFlag("opcode", -1));
		
		if (option == null || playerIndex < 1 || playerIndex > World.getWorld().getPlayers().capacity) {
			throw new RuntimeException("Invalid paramaters: opcode="+getFlag("opcode", -1)+", playerIndex="+playerIndex);
		}
		
		Player player = World.getWorld().getPlayer(playerIndex);
		if (player == null) {			
			return;//Player does not exist; ignore request
		}
		if (player.isInteractOption(option)) {
			
		} else {
			player.handleDistanceOption(session.getPlayer(), option);
			session.getPlayer().getPacketDispatcher().dispatchMinimapFlag(null);
		}
		System.out.println("Received player action: option="+option.getID()+", playerIndex="+playerIndex+", player="+player.getAccount().getUsername().getName()+", forceRun="+forceRun);
	}

}
