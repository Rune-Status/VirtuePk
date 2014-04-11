package org.virtue.network.protocol.handlers.impl;

import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.session.WorldSession;

/**
 * @author Taylor Moon
 * @since Jan 27, 2014
 */
public class InputHandler extends PacketHandler<WorldSession> {
	
	@Override
	public void handle(WorldSession session) {
		session.getPlayer().getAccount().putFlag("recent_" + getFlag("type", "null") + "_input", true);
		session.getPlayer().getAccount().putFlag("input", getFlag("input", null));
	}
}
