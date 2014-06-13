package org.virtue.network.protocol.codec.ondemand;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.virtue.utility.BufferUtils;

/**
 * @author Belthazar
 * @since Aprin 5th 2014
 */
public class JS5Decoder extends FrameDecoder {

	/**
	 * @author Graham
	 */
	private enum State {

		/**
		 * Read version state
		 */
		READ_VERSION,

		/**
		 * Read request
		 */
		READ_REQUEST
	}

	/**
	 * The update state
	 */
	private State state = State.READ_VERSION;

	/**
	 * (non-Javadoc)
	 * @see org.jboss.netty.handler.codec.frame.FrameDecoder#decode(org.jboss.netty
	 * .channel.ChannelHandlerContext, org.jboss.netty.channel.Channel,
	 * org.jboss.netty.buffer.ChannelBuffer)
	 */
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if(buffer.readableBytes() < 6)
			return null;
		
		if (state == State.READ_VERSION) {
			state = State.READ_REQUEST;
			int length = buffer.readUnsignedByte();
			if(buffer.readableBytes() >= length) {
				int version = buffer.readInt();
				int subVersion = buffer.readInt();
				String key = BufferUtils.readString(buffer);				
				return new UpdateVersionMessage(version, subVersion, key);
			}
		} else {
			int opcode = buffer.readUnsignedByte();
			if (opcode == 0 || opcode == 1) {
				int type = buffer.readUnsignedByte();
				int file = buffer.readInt();
				return new ArchiveRequest(opcode == 1, type, file);
			} else if (opcode == 4) {
				int key = buffer.readUnsignedByte();
				buffer.readerIndex(buffer.readerIndex() + 2);
				return new UpdateEncryptionMessage(key);
			} else {
				buffer.readerIndex(buffer.readerIndex() + 5);
				return null;
			}
		}
		return null;
	}
}
