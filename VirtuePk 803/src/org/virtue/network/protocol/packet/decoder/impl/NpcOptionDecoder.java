package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.game.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.impl.NpcOptionHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

public class NpcOptionDecoder implements PacketDecoder<NpcOptionHandler> {

	@Override
	public NpcOptionHandler decodePacket(RS3PacketReader packet, Session session, int opcode) {
		NpcOptionHandler handler = new NpcOptionHandler();
		handler.putFlag("forceRun", (packet.getByteS() == 1));
		handler.putFlag("npcIndex", packet.getShortA());
		handler.putFlag("opcode", opcode);
		return handler;
	}

	@Override
	public int[] getPossiblePackets() {
		return new int[] { IncommingOpcodes.NPC_OPTION_1_PACKET, IncommingOpcodes.NPC_OPTION_2_PACKET,
				IncommingOpcodes.NPC_OPTION_3_PACKET, IncommingOpcodes.NPC_OPTION_4_PACKET,
				IncommingOpcodes.NPC_OPTION_5_PACKET, IncommingOpcodes.NPC_EXAMINE_PACKET };
	}

}
