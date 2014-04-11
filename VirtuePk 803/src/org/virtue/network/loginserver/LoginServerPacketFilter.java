package org.virtue.network.loginserver;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.virtue.network.protocol.packet.RS2Packet;
import org.virtue.network.protocol.packet.RS3PacketBuilder;

/**
 * @author Taylor
 * @version 1.0
 */
public class LoginServerPacketFilter extends FrameDecoder implements ChannelHandler {

	/**
	 * (non-Javadoc)
	 * @see org.jboss.netty.handler.codec.frame.FrameDecoder#decode(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel, org.jboss.netty.buffer.ChannelBuffer)
	 */
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (!buffer.readable() || buffer.readableBytes() < 1)
			return null;
		int opcode = buffer.readByte() & 0xFF;
		byte[] payload = new byte[buffer.array().length];
		buffer.readBytes(payload, 0, payload.length - 1);
		return new RS2Packet(opcode, new RS3PacketBuilder(payload));
	}
}
