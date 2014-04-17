package org.virtue.network.protocol.packet.encoder.impl;

import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.network.protocol.messages.InterfaceMessage;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

/**
 * @author Taylor
 * @version 1.0
 */
public class InterfaceEncoder implements PacketEncoder<InterfaceMessage> {

	/**
	 * (non-Javadoc)
	 * @see com.psyc.net.codec.encoder.PacketEncoder#encode(java.lang.Object)
	 */
	@Override
	public RS3PacketBuilder buildPacket(InterfaceMessage context) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacket(OutgoingOpcodes.INTERFACE_PACKET);
		/*
		 * The ID of the interface to open.
		 */
		buffer.putLEShortA(context.getId());
		/*
		 * Part of an XTEA keyset.
		 */
		buffer.putLEInt(0);
		/*
		 * Depicts if a player can walk while the requested interface is open.
		 */
		buffer.putByteA(context.isClipped() ? 1 : 0);
		/*
		 * Part of an XTEA keyset.
		 */
		buffer.putIntV2(0);
		buffer.putIntV1(0);
		buffer.putInt(0);
		/*
		 * The widget hash data.
		 */
		buffer.putInt(context.getWindowId() << 16 | context.getWindowLocation());
		return buffer;
	}

}
