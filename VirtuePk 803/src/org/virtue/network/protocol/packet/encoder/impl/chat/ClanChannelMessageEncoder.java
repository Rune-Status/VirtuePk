package org.virtue.network.protocol.packet.encoder.impl.chat;

import org.virtue.Launcher;
import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.game.logic.social.messages.ClanChannelMessage;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

public class ClanChannelMessageEncoder implements PacketEncoder<ClanChannelMessage> {

	@Override
	public RS3PacketBuilder buildPacket(ClanChannelMessage node) {
		RS3PacketBuilder buffer = new RS3PacketBuilder(260);
		buffer.putPacketVarByte(OutgoingOpcodes.CLAN_CHANNEL_MESSAGE_PACKET);
		buffer.put(node.isGuestCc() ? 0 : 1);
		buffer.putString(node.getSenderName());
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
