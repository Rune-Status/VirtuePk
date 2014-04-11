package org.virtue.network.protocol.packet.encoder.impl;

import org.virtue.config.OutgoingOpcodes;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 8, 2014
 */
public class KeepAliveEncoder implements PacketEncoder<Object> {

	@Override
	public RS3PacketBuilder buildPacket(Object node) {
		return new RS3PacketBuilder().putPacket(OutgoingOpcodes.KEEP_ALIVE_PACKET);
	}
}
