package org.virtue.network.session;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.virtue.network.RS3PacketTransmitter;

/**
 * @author Taylor
 * @date Jan 14, 2014
 */
public abstract class Session {
	
	/**
	 * Represents the {@link ChannelHandlerContext} instance for this {@code Session}.
	 */
	private final ChannelHandlerContext context;
	
	/**
	 * Represents the {@link RS3PacketTransmitter} responsible for processing outgoing packets.
	 */
	private final RS3PacketTransmitter TRASMITTER = new RS3PacketTransmitter(this);
	
	/**
	 * Constructs a new {@code Session.java}.
	 * @param context The {@link ChannelHandlerContext} instance.
	 */
	public Session(ChannelHandlerContext context) {
		this.context = context;
	}
	
	/**
	 * Called when a message is recieved to this bound session.
	 * @param message The message being recieved.
	 */
	public abstract void decode(Object message);
	
	/**
	 * Called when this session disconnects from the server.
	 */
	public abstract void disconnect();
	
	/**
	 * @return The context
	 */
	public ChannelHandlerContext getContext() {
		return context;
	}
	
	/**
	 * @return The transmitter
	 */
	public RS3PacketTransmitter getTransmitter() {
		return TRASMITTER;
	}
}
