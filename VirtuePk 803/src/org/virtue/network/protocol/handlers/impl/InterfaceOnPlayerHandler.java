package org.virtue.network.protocol.handlers.impl;

import org.virtue.game.logic.World;
import org.virtue.game.logic.events.impl.InterfaceOnPlayerEvent;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.network.session.impl.WorldSession;

/**
 * 
 * @author Virtue Development Team 2014 (c).
 */
public class InterfaceOnPlayerHandler extends MovementHandler {

	@Override
	public void handle(WorldSession session) {
		int interfaceHash = getFlag("compHash", -1);
		int slot1 = getFlag("slot1", -1);
		int slot2 = getFlag("slot2", -1);
		int interfaceID = interfaceHash >> 16;
		int component = interfaceHash & 0xffff;
		AbstractInterface iFace = session.getPlayer().getInterfaces().getInterface(interfaceID);
		if (iFace == null) {
			return;
		}
		Player player = World.getWorld().getPlayer(getFlag("playerIndex", -1));
		if (player == null || !player.exists()) {
			return;//Player does not exist
		}
		session.getPlayer().getUpdateArchive().queueFaceEntity(player);
		putFlag("facing", true);
		putFlag("baseX", player.getTile().getX());
		putFlag("baseY", player.getTile().getY());
		putFlag("sizeX", player.getSize());
		putFlag("sizeY", player.getSize());
		super.handle(session);//Starts the movement towards the player
		session.getPlayer().setCoordinateEvent(new InterfaceOnPlayerEvent(player, iFace, component, slot1, slot2));//Process the interaction once the movement is complete
	}
}
