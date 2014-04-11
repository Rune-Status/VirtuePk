package org.virtue.network;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.session.Session;

/**
 * @author Taylor Moon
 * @since Jan 25, 2014
 */
public class RS2PacketTransmitter {
	
	
	/**
	 * Represents the {@link Session} used to encode data.
	 */
	private Session session;
	
	/**
	 * Constructs a new {@code RS2PacketTransmitter.java}.
	 */
	public RS2PacketTransmitter(Session session) {
		this.session = session;
	}
	
	/**
	 * Transmits a message to the client
	 * @param buffer The {@link RS2PacketBuilder} containing the message.
	 */
	public ChannelFuture processPacket(final RS3PacketBuilder buffer) {
		if (session.getContext().getChannel().isConnected()) {
			ChannelBuffer b = ChannelBuffers.copiedBuffer(buffer.buffer(), 0, buffer.getPosition());
			synchronized (session.getContext().getChannel()) {
				return session.getContext().getChannel().write(b);
			}
		}
		return null;
	}
	
	/**
	 * Encodes a packet to the client.
	 * @param encoder The encoder {@link Class} object corresponding to the packet encoder to encode.
	 * @param context The context data, if any.
	 */ 
	public void send(Class<?> encoder, Object... context) {
		if (context == null || context.length < 1) {
			processPacket(RS2Network.getEncoders().get(encoder).buildPacket(-1));
		} else {
			processPacket(RS2Network.getEncoders().get(encoder).buildPacket(context[0]));
		}
	}
	
	/**
	 * Encodes a packet to the client.
	 * @param packet The direct byte buffer to encode.
	 */
	public void send(RS3PacketBuilder packet) {
		processPacket(packet);
	}
}
