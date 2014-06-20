package org.virtue.network;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.session.Session;

/**
 * @author Taylor Moon
 * @since Jan 25, 2014
 */
public class RS3PacketTransmitter {
	
	
	/**
	 * Represents the {@link Session} used to encode data.
	 */
	private Session session;
	
	/**
	 * Constructs a new {@code RS3PacketTransmitter.java}.
	 */
	public RS3PacketTransmitter(Session session) {
		this.session = session;
	}
	
	/**
	 * Transmits a message to the client
	 * @param buffer The {@link RS3PacketBuilder} containing the message.
	 */
	public ChannelFuture processPacket(final RS3PacketBuilder buffer) {
		ChannelFuture response = null;
		if (session.getContext().getChannel().isConnected()) {
			ChannelBuffer b = ChannelBuffers.copiedBuffer(buffer.buffer(), 0, buffer.getPosition());
			synchronized (session.getContext().getChannel()) {
				response = session.getContext().getChannel().write(b);
			}
		}
		return response;
	}
	
	/**
	 * Transmits a message to the client
	 * @param buffer The {@link RS3PacketBuilder} containing the message.
	 */
	public static ChannelFuture processPacket(final RS3PacketBuilder buffer, Channel channel) {
		ChannelFuture response = null;
		if (channel.isConnected()) {
			ChannelBuffer b = ChannelBuffers.copiedBuffer(buffer.buffer(), 0, buffer.getPosition());
			synchronized (channel) {
				response = channel.write(b);
			}
		}
		return response;
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
