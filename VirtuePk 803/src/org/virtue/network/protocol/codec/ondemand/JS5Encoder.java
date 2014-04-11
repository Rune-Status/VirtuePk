package org.virtue.network.protocol.codec.ondemand;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.virtue.cache.Cache;

/**
 * @author Belthazar
 * @date April, 5th 2014
 */
public class JS5Encoder extends OneToOneEncoder implements ChannelHandler {

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws IOException {
		if (msg instanceof FileResponse) {
			FileResponse response = (FileResponse) msg;
			ChannelBuffer container = response.getContainer();
			int index = response.getIndex();
			int file = response.getFile();
			ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
			int compression = container.readUnsignedByte();
			int size = ((container.readByte() & 0xff) << 24) + ((container.readByte() & 0xff) << 16) + ((container.readByte() & 0xff) << 8) + (container.readByte() & 0xff);
			if (!response.isPriority()) {
				file |= 0x80000000;
			}

			buffer.writeByte(index);
			buffer.writeInt(file);
			buffer.writeByte(compression);
			buffer.writeInt(size);

			int bytes = container.readableBytes();
			if (bytes > 502) {
				bytes = 502;
			}

			buffer.writeBytes(container.readBytes(bytes));

			for (;;) {
				bytes = container.readableBytes();
				if (bytes == 0) {
					break;
				} else if (bytes > 507) {
					bytes = 507;
				}
				buffer.writeByte(index);
				buffer.writeInt(file);
				buffer.writeBytes(container.readBytes(bytes));
			}
			return buffer;
		} else if (msg instanceof UpdateStatusMessage) {
			UpdateStatusMessage status = (UpdateStatusMessage) msg;
			ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
			buffer.writeByte(status.getStatus());
			for (int i = 0; i < Cache.UPDATE_INTEGERS.length; i++) {
				buffer.writeInt(Cache.UPDATE_INTEGERS[i]);
			}
			return buffer;
		}
		return msg;
	}
}
