package org.virtue.network.protocol.handlers.impl;

import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.session.WorldSession;

public class ActionButtonHandler extends PacketHandler<WorldSession> {

	@Override
	public void handle(WorldSession session) {
		int interfaceHash = getFlag("interface", -1);
		int buttonID = getFlag("button1", -1);
		int buttonID2 = getFlag("button2", -1);
		int interfaceID = interfaceHash >> 16;
		int component = interfaceHash & 0xffff;
		System.out.println("InterfaceID: " + interfaceID + " ComponentID: "+ component + " ButtonId: " + buttonID + " ButtonId2: " + buttonID2);
	}

}
