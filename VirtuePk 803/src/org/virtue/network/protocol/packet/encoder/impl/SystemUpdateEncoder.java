package org.virtue.network.protocol.packet.encoder.impl;

import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

public class SystemUpdateEncoder implements PacketEncoder<Integer> {

	@Override
	public RS3PacketBuilder buildPacket(Integer node) {
		RS3PacketBuilder builder = new RS3PacketBuilder();
		builder.putPacket(OutgoingOpcodes.SYSTEM_UPDATE_PACKET);
		builder.putShort(node);
		return builder;
	}

}
