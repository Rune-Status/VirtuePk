package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.game.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.impl.SocialActionHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

/**
 * 
 * @author Virtue Development Team 2014 (c).
 */
public class SocialActionDecoder implements PacketDecoder<SocialActionHandler> {

	@Override
	public SocialActionHandler decodePacket(RS3PacketReader packet, Session session, int opcode) {
		SocialActionHandler handler = new SocialActionHandler();
		if (opcode == IncommingOpcodes.FRIEND_NOTE_PACKET) {
			handler.putFlag("note", packet.getString());			
		}
		handler.putFlag("name", packet.getString());
		if (opcode == IncommingOpcodes.ADD_IGNORE_PACKET) {
			handler.putFlag("tillLogout", (packet.get() == 1));
		} else if (opcode == IncommingOpcodes.IGNORE_NOTE_PACKET) {
			handler.putFlag("note", packet.getString());
		} else if (opcode == IncommingOpcodes.CHANGE_FRIEND_RANK_PACKET) {
			handler.putFlag("rank", packet.getByteC());
		}
		handler.putFlag("opcode", opcode);
		return handler;
	}

	@Override
	public int[] getPossiblePackets() {
		return new int[] { IncommingOpcodes.ADD_FRIEND_PACKET, IncommingOpcodes.REMOVE_FRIEND_PACKET,
				IncommingOpcodes.ADD_IGNORE_PACKET, IncommingOpcodes.REMOVE_IGNORE_PACKET,
				IncommingOpcodes.JOIN_FRIEND_CHAT_PACKET, IncommingOpcodes.IGNORE_NOTE_PACKET,
				IncommingOpcodes.FRIEND_NOTE_PACKET, IncommingOpcodes.FRIENDS_CHAT_KICK_PACKET,
				IncommingOpcodes.CHANGE_FRIEND_RANK_PACKET };
	}

}
