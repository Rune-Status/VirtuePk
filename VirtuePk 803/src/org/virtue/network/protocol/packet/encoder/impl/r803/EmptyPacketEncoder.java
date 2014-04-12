package org.virtue.network.protocol.packet.encoder.impl.r803;

import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

public class EmptyPacketEncoder implements PacketEncoder<Integer> {

	@Override
	public RS3PacketBuilder buildPacket(Integer opcode) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacket(opcode);
		return buffer;
	}

}
