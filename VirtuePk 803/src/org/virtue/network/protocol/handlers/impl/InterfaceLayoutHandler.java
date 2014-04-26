package org.virtue.network.protocol.handlers.impl;

import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.session.impl.WorldSession;

public class InterfaceLayoutHandler extends PacketHandler<WorldSession> {

	@Override
	public void handle(WorldSession session) {
		if (getFlag("isFinished", true)) {
			RS3PacketBuilder buffer = new RS3PacketBuilder();
			buffer.putPacket(OutgoingOpcodes.RESET_LAYOUT_INFORMATION);
			session.getTransmitter().send(buffer);
		}
	}

}
