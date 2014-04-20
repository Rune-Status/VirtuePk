package org.virtue.network.protocol.packet.encoder.impl.chat;

import org.virtue.Launcher;
import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.game.logic.social.messages.PublicMessage;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

public class PublicMessageEncoder implements PacketEncoder<PublicMessage> {

	@Override
	public RS3PacketBuilder buildPacket(PublicMessage node) {
		RS3PacketBuilder buffer = new RS3PacketBuilder(260);
		buffer.putPacketVarByte(OutgoingOpcodes.PUBLIC_MESSAGE_PACKET);
		buffer.putShort(node.getPlayerIndex());
		buffer.putShort(node.getEffects());
		buffer.put(node.getRank().getID());
		Launcher.getHuffman().huffmanEncrypt(buffer, node.getMessage());
		buffer.endPacketVarByte();
		return buffer;
	}

}
