package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.impl.PlayerOptionHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

public class PlayerOptionDecoder implements PacketDecoder<PlayerOptionHandler> {

	@Override
	public PlayerOptionHandler decodePacket(RS3PacketReader packet, Session session, int opcode) {
		PlayerOptionHandler handler = new PlayerOptionHandler();
		handler.putFlag("index", packet.getShortA());
		handler.putFlag("forceRun", (packet.get() == 1));
		int option = 0;
		switch (opcode) {
		case IncommingOpcodes.PLAYER_OPTION_1_PACKET:
			option = 1;
			break;
		case IncommingOpcodes.PLAYER_OPTION_2_PACKET:
			option = 2;
			break;
		case IncommingOpcodes.PLAYER_OPTION_3_PACKET:
			option = 3;
			break;
		case IncommingOpcodes.PLAYER_OPTION_4_PACKET:
			option = 4;
			break;
		case IncommingOpcodes.PLAYER_OPTION_5_PACKET:
			option = 5;
			break;
		case IncommingOpcodes.PLAYER_OPTION_6_PACKET:
			option = 6;
			break;
		case IncommingOpcodes.PLAYER_OPTION_7_PACKET:
			option = 7;
			break;
		case IncommingOpcodes.PLAYER_OPTION_8_PACKET:
			option = 8;
			break;
		case IncommingOpcodes.PLAYER_OPTION_9_PACKET:
			option = 9;
			break;
		case IncommingOpcodes.PLAYER_OPTION_10_PACKET:
			option = 10;
			break;
		}
		handler.putFlag("option", option);
		return handler;
	}

	@Override
	public int[] getPossiblePackets() {
		return new int[] { IncommingOpcodes.PLAYER_OPTION_1_PACKET, IncommingOpcodes.PLAYER_OPTION_2_PACKET,
				IncommingOpcodes.PLAYER_OPTION_3_PACKET, IncommingOpcodes.PLAYER_OPTION_4_PACKET,
				IncommingOpcodes.PLAYER_OPTION_5_PACKET, IncommingOpcodes.PLAYER_OPTION_6_PACKET,
				IncommingOpcodes.PLAYER_OPTION_7_PACKET, IncommingOpcodes.PLAYER_OPTION_8_PACKET,
				IncommingOpcodes.PLAYER_OPTION_9_PACKET, IncommingOpcodes.PLAYER_OPTION_10_PACKET };
	}

}
