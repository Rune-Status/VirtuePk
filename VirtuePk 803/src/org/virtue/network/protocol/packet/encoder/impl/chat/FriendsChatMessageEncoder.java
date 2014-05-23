package org.virtue.network.protocol.packet.encoder.impl.chat;

import org.virtue.Launcher;
import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.game.logic.social.messages.FriendsChatMessage;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

public class FriendsChatMessageEncoder implements PacketEncoder<FriendsChatMessage> {

	@Override
	public RS3PacketBuilder buildPacket(FriendsChatMessage node) {
		RS3PacketBuilder buffer = new RS3PacketBuilder(260);
		buffer.putPacketVarByte(OutgoingOpcodes.FRIENDS_CHAT_MESSAGE_PACKET);
		buffer.put(node.hasFilteredName() ? 1 : 0);
		buffer.putString(node.getSenderName());
		if (node.hasFilteredName()) {
			buffer.putString(node.getNameUnfiltered());
		}
		buffer.putString(node.getPrefix());
		byte[] hashCode = node.getMessageHash();
		for (int i=0;i<5;i++) {
			buffer.put(hashCode[i]);
		}
		buffer.put(node.getRank().getID());
		Launcher.getHuffman().huffmanEncrypt(buffer, node.getMessage());
		buffer.endPacketVarByte();
		return buffer;
	}

}
