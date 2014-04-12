package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.impl.ClientScreenHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

public class ClientScreenDecoder implements PacketDecoder<ClientScreenHandler> {

	@Override
	public ClientScreenHandler decodePacket(RS3PacketReader packet, Session session, int opcode) {
		ClientScreenHandler handler = new ClientScreenHandler();
		
		handler.putFlag("display_mode", packet.getUnsignedByte());
		handler.putFlag("width", packet.getUnsignedShort());
		handler.putFlag("height", packet.getUnsignedShort());
		packet.getUnsignedByte();//Not sure what this is...
		return handler;
	}

	@Override
	public int[] getPossiblePackets() {
		return new int[] { IncommingOpcodes.SCREEN_PACKET };
	}

}
