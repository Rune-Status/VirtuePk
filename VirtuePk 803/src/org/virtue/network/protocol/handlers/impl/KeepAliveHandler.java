package org.virtue.network.protocol.handlers.impl;

import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.protocol.packet.encoder.impl.KeepAliveEncoder;
import org.virtue.network.session.Session;

/**
 * @author Taylor Moon
 * @since Jan 25, 2014
 */
public class KeepAliveHandler extends PacketHandler<Session> {

	@Override
	public void handle(Session session) {
		session.getTransmitter().send(KeepAliveEncoder.class);
	}
}
