package org.virtue.network.protocol.packet.encoder.impl;

import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

/**
 * @author Taylor
 * @version 1.0
 */
public class LogoutEncoder implements PacketEncoder<Boolean> {

	/**
	 * (non-Javadoc)
	 * @see com.psyc.net.codec.encoder.PacketEncoder#encode(java.lang.Object)
	 */
	@Override
	public RS3PacketBuilder buildPacket(Boolean lobby) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacket(lobby ? OutgoingOpcodes.LOBBY_LOGOUT_PACKET : OutgoingOpcodes.FULL_LOGOUT_PACKET);
		return buffer;
	}
}
