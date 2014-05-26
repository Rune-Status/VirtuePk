package org.virtue.network.protocol.packet.encoder.impl;

import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.network.protocol.messages.VarcStringMessage;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

public class VarcStringEncoder implements PacketEncoder<VarcStringMessage> {

	@Override
	public RS3PacketBuilder buildPacket(VarcStringMessage node) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		if (node.getValue().length() >= Byte.MAX_VALUE) {//TODO: Fix this to correctly check length
			buffer.putPacketVarShort(OutgoingOpcodes.LARGE_VARC_STRING_PACKET);
			buffer.putString(node.getValue());
			buffer.putLEShortA(node.getVarcID());
			buffer.endPacketVarShort();
		} else {
			buffer.putPacketVarByte(OutgoingOpcodes.SMALL_VARC_STRING_PACKET);
			buffer.putLEShortA(node.getVarcID());
			buffer.putString(node.getValue());
			buffer.endPacketVarByte();
		}
		return buffer;
	}

}
