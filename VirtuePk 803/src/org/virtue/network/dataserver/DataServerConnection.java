package org.virtue.network.dataserver;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.virtue.network.dataserver.output.WorldSubmissionEncoder;

/**
 * @author Taylor
 * @version 1.0
 */
public class DataServerConnection {

	/**
	 * Represents the context.
	 */
	private final ChannelHandlerContext context;
	
	/**
	 * Constructs a new {@code LSConnection.java}.
	 * @param context The context.
	 */
	public DataServerConnection(ChannelHandlerContext context) {
		this.context = context;
		send(WorldSubmissionEncoder.class);
	}

	/**
	 * @return The context
	 */
	public ChannelHandlerContext getContext() {
		return context;
	}
	
	/**
	 * Encodes a packet to the login server.
	 * @param packet The packet to encode.
	 * @param params The params.
	 */
	public void send(final Class<?> packet, final Object... params) {
		synchronized (context.getChannel()) {
			if (context.getChannel().isConnected()) {
				if (params.length > 0) {
					context.getChannel().write(ChannelBuffers.copiedBuffer(DataServer.getEncoders().get(packet).buildPacket(params[0]).buffer()));
				} else {
					context.getChannel().write(ChannelBuffers.copiedBuffer(DataServer.getEncoders().get(packet).buildPacket(null).buffer()));
				}
			}
		}
	}
}
