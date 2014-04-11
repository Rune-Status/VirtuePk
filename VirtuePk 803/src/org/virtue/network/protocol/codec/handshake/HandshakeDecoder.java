package org.virtue.network.protocol.codec.handshake;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.virtue.network.protocol.codec.login.RS2LoginDecoder;
import org.virtue.network.protocol.codec.login.RS3LoginDecoder;
import org.virtue.network.protocol.codec.ondemand.JS5Decoder;
import org.virtue.network.protocol.codec.ondemand.JS5Encoder;
import org.virtue.network.protocol.codec.ondemand.XorEncoder;

/**
 * @author Taylor Moon
 *
 * @since v1.0
 */
public class HandshakeDecoder extends FrameDecoder {

	/**
	 * Constructs a {@code HandshakeDecoder} instance.
	 */
	public HandshakeDecoder() {
		super(true);
	}

	/**
	 * (non-Javadoc)
	 * @see org.jboss.netty.handler.codec.frame.FrameDecoder#decode(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel, org.jboss.netty.buffer.ChannelBuffer)
	 */
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		HandshakeType handshakeType = new HandshakeType(0xFF & buffer.readByte());
		switch (handshakeType.getType()) {
		case HANDSHAKE_LOGIN:
			channel.write(validateResponse());
			channel.getPipeline().addBefore("upHandler", "loginDecoder", new RS3LoginDecoder());
			break;
		case HANDSHAKE_ONDEMAND:
			channel.getPipeline().addFirst("encoder", new JS5Encoder());
			channel.getPipeline().addFirst("xor-encoder", new XorEncoder());
			channel.getPipeline().addBefore("upHandler", "updateDecoder", new JS5Decoder());
			break;
		default:
			break;
		}
		channel.getPipeline().remove(HandshakeDecoder.class);
		return buffer.readable() ? new Object[] { handshakeType, buffer.readBytes(buffer.readableBytes()) } : handshakeType;
	}

	/**
	 * Validates the login response.
	 */
	private ChannelBuffer validateResponse() {
		ChannelBuffer channelBuffer = ChannelBuffers.buffer(1);
		channelBuffer.writeByte(0);
		return channelBuffer;
	}
}
