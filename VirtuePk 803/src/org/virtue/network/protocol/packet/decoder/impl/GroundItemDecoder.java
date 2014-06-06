package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.game.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.impl.GroundItemHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

/**
 * 
 * @author Virtue Development Team 2014 (c).
 */
public class GroundItemDecoder implements PacketDecoder<GroundItemHandler> {

	@Override
	public GroundItemHandler decodePacket(RS3PacketReader packet, Session session, int opcode) {
		GroundItemHandler handler = new GroundItemHandler();
		handler.putFlag("baseY", packet.getShort());
		handler.putFlag("forceRun", (packet.get() == 1));
		handler.putFlag("itemID", packet.getShort());
		handler.putFlag("baseX", packet.getLEShortA());
		handler.putFlag("opcode", opcode);
		return handler;
	}

	@Override
	public int[] getPossiblePackets() {
		return new int[] { IncommingOpcodes.GROUND_ITEM_OPTION_1_PACKET, IncommingOpcodes.GROUND_ITEM_OPTION_2_PACKET,
				IncommingOpcodes.GROUND_ITEM_OPTION_3_PACKET, IncommingOpcodes.GROUND_ITEM_OPTION_4_PACKET,
				IncommingOpcodes.GROUND_ITEM_OPTION_5_PACKET, IncommingOpcodes.GROUND_ITEM_EXAMINE_PACKET };
	}

}
