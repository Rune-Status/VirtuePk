package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.impl.WorldlistHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

/**
 * @author Taylor
 * @date Jan 20, 2014
 */
public class WorlListDecoder implements PacketDecoder<WorldlistHandler> {

	@Override
	public WorldlistHandler decodePacket(RS3PacketReader buffer, Session session, int opcode) {
		WorldlistHandler handler = new WorldlistHandler();
		System.out.println("Received request for world list...");
		/*
		 * Represents the opcode that the client requested as the lobby loading
		 * procedure. The opcodes consist of a full update and a fixed update,
		 * each performing seperate properties.
		 */
		handler.putFlag("request_opcode", buffer.getInt());
		return handler;
	}

	@Override
	public int[] getPossiblePackets() {
		return new int[] { IncommingOpcodes.WORLD_LIST_UPDATE };
	}
}
