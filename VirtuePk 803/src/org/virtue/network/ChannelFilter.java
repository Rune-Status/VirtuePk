package org.virtue.network;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.virtue.engine.GameEngine;
import org.virtue.engine.threads.ChannelThreadFactory;
import org.virtue.engine.threads.NetworkThreadFactory;

/**
 * @author Taylor
 * @version 1.0
 */
public class ChannelFilter {
	
	/**
	 * Represents the port to connect to.
	 */
	private final int PORT = 43594;
	
	/**
	 * Represents the {@link ExecutorService} responsible for providing threads used for our network filter.
	 */
	private static final ExecutorService NETWORK_EXECUTOR = Executors.newSingleThreadExecutor(new NetworkThreadFactory());
	
	/**
	 * Represents the {@link ExecutorService} responsible for providing threads used for our bound channel.
	 */
	private static final ExecutorService CHANNEL_EXECUTOR = Executors.newFixedThreadPool(7, new ChannelThreadFactory());
	
	/**
	 * The {@code ServerBootstrap} to use.
	 */
	private final ServerBootstrap BOOT_STRAP;
	
	/**
	 * Constructs a new {@code ChannelFilter.java}
	 */
	public ChannelFilter() {
		BOOT_STRAP = new ServerBootstrap(new NioServerSocketChannelFactory(NETWORK_EXECUTOR, CHANNEL_EXECUTOR, GameEngine.getAvailableWorkerCount()));
	}
	
	/**
	 * Starts this <tt>channel filter</tt>
	 */
	public void start() {
		BOOT_STRAP.setOption("reuseAddress", true);
		BOOT_STRAP.setOption("child.tcpNoDelay", true);
		BOOT_STRAP.setPipelineFactory(new ChannelPipelineFilter());
		BOOT_STRAP.bind(new InetSocketAddress(PORT));
	}	
}
