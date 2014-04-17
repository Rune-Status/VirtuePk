package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.game.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.impl.ObjectOptionHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

public class ObjectOptionDecoder implements PacketDecoder<ObjectOptionHandler> {

	@Override
	public ObjectOptionHandler decodePacket(RS3PacketReader packet, Session session, int opcode) {
		ObjectOptionHandler handler = new ObjectOptionHandler();
		handler.putFlag("objectID", packet.getLEInt());
		handler.putFlag("baseY", packet.getLEShortA());
		handler.putFlag("forceRun", (packet.getByteA() == 1));
		handler.putFlag("baseX", packet.getLEShortA());
		handler.putFlag("opcode", opcode);
		return handler;
	}

	@Override
	public int[] getPossiblePackets() {
		return new int[] { IncommingOpcodes.OBJECT_OPTION_1_PACKET, IncommingOpcodes.OBJECT_OPTION_2_PACKET,
				IncommingOpcodes.OBJECT_OPTION_3_PACKET, IncommingOpcodes.OBJECT_OPTION_4_PACKET,
				IncommingOpcodes.OBJECT_OPTION_5_PACKET, IncommingOpcodes.OBJECT_EXAMINE_PACKET };
	}

}
