package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.game.config.IncommingOpcodes;
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
		if (opcode == IncommingOpcodes.TEXT_INPUT_PACKET) {
			handler.putFlag("input", packet.getString());
		} else if (opcode == IncommingOpcodes.INT_INPUT_PACKET) {
			handler.putFlag("input", packet.getInt());
		}
		handler.putFlag("opcode", opcode);
		return handler;
	}

	@Override
	public int[] getPossiblePackets() {
		return new int[] { IncommingOpcodes.TEXT_INPUT_PACKET, IncommingOpcodes.INT_INPUT_PACKET };
	}
}
