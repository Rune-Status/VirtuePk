package org.virtue.network.protocol.handlers.impl;

import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.session.impl.WorldSession;

public class MessageHandler extends PacketHandler<WorldSession> {

	@Override
	public void handle(WorldSession session) {
		String message = getFlag("message", "");
		int colourEffect = getFlag("colourEffect", 0);
		int moveEffect = getFlag("moveEffect", 0);
		
		session.getPlayer().getChatManager().handlePublicMessage(message, colourEffect, moveEffect);
	}

}
