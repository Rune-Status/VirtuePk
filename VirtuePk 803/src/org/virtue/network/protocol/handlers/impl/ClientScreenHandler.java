package org.virtue.network.protocol.handlers.impl;

import org.virtue.game.node.entity.player.screen.ClientScreen;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.session.WorldSession;
import org.virtue.utility.DisplayMode;

public class ClientScreenHandler extends PacketHandler<WorldSession> {

	@Override
	public void handle(WorldSession session) {
		ClientScreen screen = session.getPlayer().getInterfaces().getScreen();
		DisplayMode displayMode = DisplayMode.forId(getFlag("width", 0));
		int width = getFlag("width", 0);
		int height = getFlag("height", 0);
		
		screen.setScreenInfo(height, width, displayMode);
	}

}
