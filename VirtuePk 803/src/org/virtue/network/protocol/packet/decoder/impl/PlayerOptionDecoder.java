package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.game.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.impl.PlayerOptionHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

/**
 * 
 * @author Virtue Development Team 2014 (c).
 */
public class PlayerOptionDecoder implements PacketDecoder<PlayerOptionHandler> {

	@Override
	public PlayerOptionHandler decodePacket(RS3PacketReader packet, Session session, int opcode) {
		PlayerOptionHandler handler = new PlayerOptionHandler();
		handler.putFlag("playerIndex", packet.getShortA());
		handler.putFlag("forceRun", (packet.get() == 1));
		handler.putFlag("opcode", opcode);
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
