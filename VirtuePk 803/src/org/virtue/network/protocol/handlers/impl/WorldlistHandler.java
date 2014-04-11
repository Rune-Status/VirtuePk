package org.virtue.network.protocol.handlers.impl;

import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.protocol.packet.encoder.impl.WorldListEncoder;
import org.virtue.network.session.Session;

/**
 * @author Taylor
 * @date Jan 20, 2014
 */
public class WorldlistHandler extends PacketHandler<Session> {

	@Override
	public void handle(Session session) {
		session.getTransmitter().send(WorldListEncoder.class, getFlag("request_opcode", -1));
	}
}
