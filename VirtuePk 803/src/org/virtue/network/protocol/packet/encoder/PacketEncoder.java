package org.virtue.network.protocol.packet.encoder;

import org.virtue.network.protocol.packet.RS3PacketBuilder;

/**
 * @author Taylor Moon
 * @since Jan 23, 2014
 */
public interface PacketEncoder<T extends Object> {

	/**
	 * Called when a packet is requested to be dispatched, before dispatching a
	 * blank packet the server must build the byte structure.
	 * @param node The node reference.
	 * @return The packet that was recently built.
	 */
	RS3PacketBuilder buildPacket(T node);
}
