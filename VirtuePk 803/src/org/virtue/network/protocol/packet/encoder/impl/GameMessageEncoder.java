package org.virtue.network.protocol.packet.encoder.impl;

import org.virtue.config.OutgoingOpcodes;
import org.virtue.network.protocol.messages.GameMessage;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

/**
 * @author Sundays211
 * @version 0.1
 */
public class GameMessageEncoder implements PacketEncoder<GameMessage> {

	@Override
	public RS3PacketBuilder buildPacket(GameMessage message) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacketVarByte(OutgoingOpcodes.MESSAGE_PACKET);
		buffer.putSmart(message.getOpcode().getOpcode());
		buffer.putInt(message.getPlayer().getTile().getTileHash());
		int bitMask = 0;
		if (message.getPlayer().getAccount().getUsername().getName() != null) {
			bitMask |= 0x1;
		}
		if (message.getPlayer().getAccount().getUsername().hasTitle()) {
			bitMask |= 0x2;
		}
		buffer.put(bitMask);
		if (message.getPlayer().getAccount().getUsername().getName() != null) {
			buffer.putString(message.getPlayer().getAccount().getUsername().getNameWithTitles());
		}
		if (message.getPlayer().getAccount().getUsername().hasTitle()) {
			buffer.putString(message.getPlayer().getAccount().getUsername().getNameAsProtocol().replace('_', ' '));
		}
		buffer.putString(message.getMessage());
		buffer.endPacketVarByte();
		return buffer;
	}

}
