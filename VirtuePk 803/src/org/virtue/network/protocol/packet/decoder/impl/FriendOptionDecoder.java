package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.game.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.impl.FriendOptionHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

/**
 * 
 * @author Virtue Development Team 2014 (c).
 */
public class FriendOptionDecoder implements PacketDecoder<FriendOptionHandler> {

	@Override
	public FriendOptionHandler decodePacket(RS3PacketReader packet, Session session, int opcode) {
		FriendOptionHandler handler = new FriendOptionHandler();
		handler.putFlag("slot1", packet.getIntV2());
		handler.putFlag("friend", packet.getString());
		handler.putFlag("slot2", packet.getLEShort() & 0xFFFF);
		handler.putFlag("button", packet.get());
		return handler;
	}

	@Override
	public int[] getPossiblePackets() {
		return new int[] { IncommingOpcodes.FRIEND_OPTION_PACKET };
	}

}
