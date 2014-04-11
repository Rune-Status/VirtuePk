package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.impl.KeepAliveHandler;
import org.virtue.network.protocol.packet.RS2PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

/**
 * @author Taylor Moon
 * @since Jan 25, 2014
 */
public class KeepAliveDecoder implements PacketDecoder<KeepAliveHandler> {

	@Override
	public KeepAliveHandler decodePacket(RS2PacketReader packet, Session session, int opcode) {
		/*
		 * Simply used to keep the client and server connected, as a
		 * verification that connection was not lost.
		 */
		return new KeepAliveHandler();
	}

	@Override
	public int[] getPossiblePackets() {
		return new int[] { IncommingOpcodes.PING_PACKET };
	}
}
