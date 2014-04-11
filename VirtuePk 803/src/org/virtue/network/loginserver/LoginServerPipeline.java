package org.virtue.network.loginserver;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.DefaultChannelPipeline;

/**
 * @author Taylor
 * @version 1.0
 */
public class LoginServerPipeline implements ChannelPipelineFactory {

	/**
	 * (non-Javadoc)
	 * @see org.jboss.netty.channel.ChannelPipelineFactory#getPipeline()
	 */
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = new DefaultChannelPipeline();
		pipeline.addLast("decoder", new LoginServerPacketFilter());
		pipeline.addLast("multiplexer", new LoginServerPacketProcessor());
		return pipeline;
	}
}
