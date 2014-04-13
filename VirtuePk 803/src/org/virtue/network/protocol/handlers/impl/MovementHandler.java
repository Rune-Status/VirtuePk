package org.virtue.network.protocol.handlers.impl;

import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.session.WorldSession;

public class MovementHandler extends PacketHandler<WorldSession> {

	@Override
	public void handle(WorldSession session) {
		int baseX = getFlag("baseX", -1);
		int baseY = getFlag("baseY", -1);
		boolean forceRun = getFlag("forceRun", false);
		
		System.out.println("Movement request: x="+baseX+", y="+baseY+", forceRun="+forceRun);
	}

}
