package org.virtue.network;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.DefaultChannelPipeline;
import org.virtue.network.protocol.codec.handshake.HandshakeDecoder;

/**
 * @author Taylor
 * @version 1.0
 */
public class ChannelPipelineFilter implements ChannelPipelineFactory {
	
	/**
	 * (non-Javadoc)
	 * @see org.jboss.netty.channel.ChannelPipelineFactory#getPipeline()
	 */
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = new DefaultChannelPipeline();
		pipeline.addLast("decoder", new HandshakeDecoder());
		pipeline.addLast("upHandler", new PacketUpstreamHandler());
		return pipeline;
	}
}
