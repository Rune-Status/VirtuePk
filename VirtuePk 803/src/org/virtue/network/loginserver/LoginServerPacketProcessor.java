package org.virtue.network.loginserver;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.protocol.packet.RS2Packet;
import org.virtue.network.protocol.packet.RS2PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

/**
 * @author Taylor
 * @version 1.0
 */
public class LoginServerPacketProcessor extends SimpleChannelUpstreamHandler {

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.netty.channel.SimpleChannelUpstreamHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext,
	 *      org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		if (!(e.getMessage() instanceof RS2Packet)) {
			return;
		}
		RS2Packet packet = (RS2Packet) e.getMessage();
		PacketDecoder<? super PacketHandler<? super Session>> decoder = LoginServer.getDecoders().get(packet.getOpcode());
		if (decoder == null) {
			System.err.println("Unhandled login server packet: " + packet.getOpcode());
			return;
		}
		@SuppressWarnings("unchecked")
		PacketHandler<? super Session> handler = (PacketHandler<? super Session>) decoder.decodePacket(new RS2PacketReader(packet.getBuffer().buffer()), (Session) ctx.getAttachment(), packet.getOpcode());
		if (handler != null) {
			handler.handle((Session) ctx.getAttachment());
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.netty.channel.SimpleChannelUpstreamHandler#exceptionCaught(org.jboss.netty.channel.ChannelHandlerContext,
	 *      org.jboss.netty.channel.ExceptionEvent)
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.netty.channel.SimpleChannelUpstreamHandler#channelDisconnected(org.jboss.netty.channel.ChannelHandlerContext,
	 *      org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
	
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.netty.channel.SimpleChannelUpstreamHandler#channelConnected(org.jboss.netty.channel.ChannelHandlerContext,
	 *      org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		LoginServer.setConnection(new LoginServerConnection(ctx));
	}
}
