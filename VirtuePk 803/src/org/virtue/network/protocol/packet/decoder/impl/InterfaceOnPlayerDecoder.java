package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.game.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.impl.InterfaceOnPlayerHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

/**
 * 
 * @author Virtue Development Team 2014 (c).
 */
public class InterfaceOnPlayerDecoder implements PacketDecoder<InterfaceOnPlayerHandler> {

	@Override
	public InterfaceOnPlayerHandler decodePacket(RS3PacketReader packet, Session session, int opcode) {
		InterfaceOnPlayerHandler handler = new InterfaceOnPlayerHandler();
		int slot1 = packet.getLEShortA() & 0xFFFF;
		if(slot1 == 65535) {
			slot1 = -1;
		}
		handler.putFlag("slot1", slot1);
		handler.putFlag("playerIndex", packet.getLEShortA());
		handler.putFlag("forceRun", (packet.getByteC() == 1));
		int slot2 = packet.getLEShortA() & 0xFFFF;
		if(slot2 == 65535) {
			slot2 = -1;
		}
		handler.putFlag("slot2", slot2);
		handler.putFlag("compHash", packet.getIntV1());
		return handler;
	}

	@Override
	public int[] getPossiblePackets() {
		return new int[] { IncommingOpcodes.INTERFACE_ON_PLAYER_PACKET};
	}

}
