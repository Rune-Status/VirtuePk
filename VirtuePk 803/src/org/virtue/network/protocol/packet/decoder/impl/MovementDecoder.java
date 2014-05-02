package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.game.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.impl.MovementHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;
import org.virtue.network.session.impl.WorldSession;

public class MovementDecoder implements PacketDecoder<MovementHandler> {

	@Override
	public MovementHandler decodePacket(RS3PacketReader packet, Session session, int opcode) {
		MovementHandler handler = new MovementHandler();
		handler.putFlag("baseX", packet.getShortA());
		handler.putFlag("baseY", packet.getLEShortA());
		handler.putFlag("forceRun", (packet.getByteA() == 1));
		//TODO: Minimap packet has more information; include this somewhere
		return handler;
	}

	@Override
	public int[] getPossiblePackets() {
		return new int[] { IncommingOpcodes.MINI_WALKING_PACKET, IncommingOpcodes.WALKING_PACKET };
	}

}
