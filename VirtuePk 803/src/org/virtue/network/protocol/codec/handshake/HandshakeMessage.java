package org.virtue.network.protocol.codec.handshake;

import org.virtue.network.protocol.packet.RS3PacketBuilder;


/**
 * @author Taylor
 * @version 1.0
 */
public class HandshakeMessage {
	
	/**
	 * The byte buffer of the message
	 */
	private RS3PacketBuilder buffer;
	
	/**
	 * Constructs a new {@code JS5Message} instance.
	 * @param buffer The {@code PsycOutBuffer} instance to use.
	 */
	public HandshakeMessage(RS3PacketBuilder buffer) {
		this.buffer = buffer;
	}

	/**
	 * @return the buffer
	 */
	public RS3PacketBuilder getBuffer() {
		return buffer;
	}

}
