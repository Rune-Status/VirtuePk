package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.network.protocol.handlers.impl.InputHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

/**
 * @author Taylor Moon
 * @since Jan 27, 2014
 */
public class InputDecoder implements PacketDecoder<InputHandler> {

	@Override
	public InputHandler decodePacket(RS3PacketReader packet, Session session, int opcode) {
		InputHandler handler = new InputHandler();
		/*
		 * The incoming input from the client that the player has entered.
		 */
		handler.putFlag("input", opcode == 29 ? packet.getString() : packet.getInt());
		handler.putFlag("type", opcode == 29 ? "string" : "integer");
		return handler;
	}

	@Override
	public int[] getPossiblePackets() {
		return new int[] { 29, 81 };
	}
}
