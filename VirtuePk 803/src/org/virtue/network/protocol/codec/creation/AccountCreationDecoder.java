package org.virtue.network.protocol.codec.creation;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 9, 2014
 */
public class AccountCreationDecoder extends FrameDecoder implements ChannelHandler {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (!buffer.readable()) 
			return null;
		buffer.readByte();
		return null;
	}

}
