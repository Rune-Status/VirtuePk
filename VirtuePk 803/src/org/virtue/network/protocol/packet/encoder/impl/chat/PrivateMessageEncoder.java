package org.virtue.network.protocol.packet.encoder.impl.chat;

import org.virtue.Launcher;
import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.game.logic.social.messages.PrivateMessage;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

public class PrivateMessageEncoder implements PacketEncoder<PrivateMessage> {

	@Override
	public RS3PacketBuilder buildPacket(PrivateMessage node) {
		RS3PacketBuilder buffer = new RS3PacketBuilder(260);
		if (node.isIncomming()) {
			buffer.putPacketVarShort(OutgoingOpcodes.INCOMMING_PRIVATE_MESSAGE_PACKET);
			buffer.put(node.hasDifferentNames() ? 1 : 0);
			buffer.putString(node.getSenderRespond());
			if (node.hasDifferentNames()) {
				buffer.putString(node.getSenderDisplay());
			}
			byte[] hashCode = node.getMessageHash();
			for (int i=0;i<5;i++) {
				buffer.put(hashCode[i]);
			}
			buffer.put(node.getRank().getID());
		} else {
			buffer.putPacketVarShort(OutgoingOpcodes.OUTGOING_PRIVATE_MESSAGE_PACKET);
			buffer.putString(node.getSenderDisplay());
		}
		Launcher.getHuffman().huffmanEncrypt(buffer, node.getMessage());		
		buffer.endPacketVarShort();
		return buffer;
	}

}
