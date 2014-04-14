package org.virtue.network.protocol.codec.creation;

import java.util.GregorianCalendar;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.virtue.utility.Base37Utils;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 9, 2014
 */
public class AccountCreationDecoder extends FrameDecoder implements ChannelHandler {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (buffer.readableBytes() > 6) 
			return null;
		int stage = buffer.readByte();
		switch (stage) {
		case 21:
			int day = buffer.readByte();
			int month = buffer.readByte();
			int year = buffer.readByte();
			int country = buffer.readByte();
			return new PersonalDetails(new GregorianCalendar(year, month, day), country);
		case 22:
			if (buffer.readableBytes() < 8)
				return null;
			String username = Base37Utils.decodeBase37(buffer.readLong());
			return new UsernameRequest(username);
		}
		return null;
	}

}
