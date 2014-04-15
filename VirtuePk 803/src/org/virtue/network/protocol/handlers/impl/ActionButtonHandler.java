package org.virtue.network.protocol.handlers.impl;

import org.virtue.game.node.entity.player.screen.InterfaceManager;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.session.impl.WorldSession;

public class ActionButtonHandler extends PacketHandler<WorldSession> {


	
	/**
	 * Represents the interface manager.
	 */
	private InterfaceManager interfaceManager;
	
	@Override
	public void handle(WorldSession session) {
		int interfaceHash = getFlag("interface", -1);
		int buttonID = getFlag("button1", -1);
		int buttonID2 = getFlag("button2", -1);
		int interfaceID = interfaceHash >> 16;
		int component = interfaceHash & 0xffff;
		
		switch (interfaceID) {
		case 1433:
			if (component == 65) {//Logout button
				session.getPlayer().sendLogout(false);
				return;
			} else if (component == 57) {//Lobby logout button
				session.getPlayer().sendLogout(true);
				return;
			}
			break;
		case 1431:
			if (component == 7) {
				interfaceManager.sendMeleePowersTab();
			}
			break;
		}
		System.out.println("InterfaceID: " + interfaceID + " ComponentID: "+ component + " ButtonId: " + buttonID + " ButtonId2: " + buttonID2);
	}

}
