package org.virtue.network.protocol.handlers.impl;

import org.virtue.game.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.session.impl.WorldSession;

/**
 * @author Taylor Moon
 * @since Jan 27, 2014
 */
public class InputHandler extends PacketHandler<WorldSession> {
	
	@Override
	public void handle(WorldSession session) {
		switch (getFlag("opcode", -1)) {
		case IncommingOpcodes.TEXT_INPUT_PACKET:
			if (session.getPlayer().getInputEvent() != null) {
				session.getPlayer().getInputEvent().onInputEntered(getFlag("input", ""));
			}
			break;
		case IncommingOpcodes.INT_INPUT_PACKET:
			if (session.getPlayer().getInputEvent() != null) {
				session.getPlayer().getInputEvent().onInputEntered(new Integer(getFlag("input", -1)));
			}
			break;
		}
	}
}
