package org.virtue.network.protocol.codec.creation;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 9, 2014
 */
public class AccountCreationEncoder extends OneToOneEncoder {

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
		if (!channel.isWritable())
			return null;
		if (msg instanceof AccountCreationResponse) {
			AccountCreationResponse response = (AccountCreationResponse) msg;
			channel.write(response.getStatus());
			channel.write(response.getPayload());
		}
		return null;
	}

}
