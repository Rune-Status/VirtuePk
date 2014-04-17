package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.Launcher;
import org.virtue.game.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.impl.MessageHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;
import org.virtue.network.session.impl.WorldSession;

public class MessageDecoder implements PacketDecoder<MessageHandler> {

	@Override
	public MessageHandler decodePacket(RS3PacketReader packet, Session session, int opcode) {
		if (opcode == IncommingOpcodes.CHAT_TYPE_PACKET) {
			((WorldSession) session).getPlayer().setChatType(packet.get());
		} else {
			MessageHandler handler = new MessageHandler();
			handler.putFlag("colourEffect", packet.get());
			handler.putFlag("moveEffect", packet.get());
			handler.putFlag("message", Launcher.getHuffman().huffmanDecrypt(packet, 200));
			return handler;
		}
		return null;
	}

	@Override
	public int[] getPossiblePackets() {
		return new int[] { IncommingOpcodes.CHAT_TYPE_PACKET, IncommingOpcodes.MESSAGE_PACKET };
	}

}
