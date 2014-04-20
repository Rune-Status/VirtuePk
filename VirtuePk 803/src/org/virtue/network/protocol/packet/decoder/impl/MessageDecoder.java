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
		MessageHandler handler = new MessageHandler();
		handler.putFlag("opcode", opcode);
		int maxSize = 200;
		if (opcode == IncommingOpcodes.CHAT_TYPE_PACKET) {
			((WorldSession) session).getPlayer().getChatManager().setChatType(packet.get());
			return null;
		} else if (opcode == IncommingOpcodes.PRIVATE_MESSAGE_PACKET) {
			handler.putFlag("recipient", packet.getString());
			maxSize = 300;
		} else {
			handler.putFlag("colourEffect", packet.get());
			handler.putFlag("moveEffect", packet.get());
		}
		handler.putFlag("message", Launcher.getHuffman().huffmanDecrypt(packet, maxSize));
		return handler;
	}

	@Override
	public int[] getPossiblePackets() {
		return new int[] { IncommingOpcodes.CHAT_TYPE_PACKET, IncommingOpcodes.MESSAGE_PACKET,
				IncommingOpcodes.PRIVATE_MESSAGE_PACKET };
	}

}
