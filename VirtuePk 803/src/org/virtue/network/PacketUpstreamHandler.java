package org.virtue.network;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.virtue.network.protocol.codec.handshake.HandshakeType;
import org.virtue.network.session.Session;
import org.virtue.network.session.impl.AccountCreationSession;
import org.virtue.network.session.impl.JS5Session;
import org.virtue.network.session.impl.LoginSession;

/**
 * @author Taylor
 * @since Sep 14, 2013
 */
public class PacketUpstreamHandler extends SimpleChannelUpstreamHandler {
	
	/**
	 * (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelUpstreamHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		if (ctx.getAttachment() == null) {
			HandshakeType handshakeType = (HandshakeType) e.getMessage();
			switch (handshakeType.getType()) {
			case HANDSHAKE_LOGIN:
				ctx.setAttachment(new LoginSession(ctx));
				break;
			case HANSHAKE_CREATION:
				ctx.setAttachment(new AccountCreationSession(ctx));
				break;
			case HANDSHAKE_ONDEMAND:
				ctx.setAttachment(new JS5Session(ctx));
				break;
			}
		} else {
			((Session) ctx.getAttachment()).decode(e.getMessage());
		}
	}

	/**
	 * (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelUpstreamHandler#exceptionCaught(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ExceptionEvent)
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
	}

	/**
	 * (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelUpstreamHandler#channelDisconnected(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		((Session) ctx.getAttachment()).disconnect();
	}
}
