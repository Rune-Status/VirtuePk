package org.virtue.network;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.virtue.network.protocol.packet.RS2Packet;
import org.virtue.network.protocol.packet.RS2PacketReader;

/**
 * @author Taylor
 * @since Sep 15, 2013
 */
public class RS2PacketFilter extends FrameDecoder {

	/**
	 * (non-Javadoc)
	 * @see org.jboss.netty.handler.codec.frame.FrameDecoder#decode(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel, org.jboss.netty.buffer.ChannelBuffer)
	 */
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (buffer.readable()) {
			int opcode = buffer.readByte() & 0xFF;
			if (opcode < 0) {
				buffer.discardReadBytes();
				return null;
			}
			int length = RS2Network.getPacketVars()[opcode];
			if (length < 0) {
				switch (length) {
				case -1:
					if (buffer.readable()) {
						length = buffer.readByte() & 0xff;
					}
					break;
				case -2:
					if (buffer.readableBytes() >= 2) {
						length = buffer.readShort() & 0xffff;
					}
					break;
				default:
					length = buffer.readableBytes();
					break;
				}
			}
			if (buffer.readableBytes() >= length) {
				if (length < 0) {
					return null;
				}
				byte[] payload = new byte[length];
				buffer.readBytes(payload, 0, length);
				return new RS2Packet(opcode, new RS2PacketReader(payload));
			}
		}
		return null;
	}
}