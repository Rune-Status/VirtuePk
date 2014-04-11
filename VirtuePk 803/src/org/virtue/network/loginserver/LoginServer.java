package org.virtue.network.loginserver;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;
import org.virtue.network.session.Session;
import org.virtue.utility.FileUtils;

/**
 * @author Taylor
 * @version 1.0
 */
public class LoginServer {
	
	/**
	 * Represents the {@link ClientBootstrap}.
	 */
	private static ClientBootstrap strap;
	
	/**
	 * Represents the connection.
	 */
	private static LoginServerConnection connection;
	
	/**
	 * Represents a {@link Map} of packet decoders that will be referred to when a packet
	 * is recieved from the client
	 */
	private static final Map<Integer, PacketDecoder<? super PacketHandler<? super Session>>> decoders = new HashMap<>();
	
	/**
	 * Represents a {@link List} of packet encoders that will be referred to when
	 * a packet is sent to the client
	 */
	private static final Map<Class<?>, PacketEncoder<? super Object>> encoders = new HashMap<>();
	
	/**
	 * Loads the this login server.
	 */
	public static void load() {
		for (Class<?> c : FileUtils.getClasses("org.virtue.network.loginserver.input")) {
			try {
				Object object = c.newInstance();
				if (!(object instanceof PacketDecoder<?>)) {
					System.err.println("Invalid packet: " + object.getClass().getSimpleName());
					continue;
				}
				@SuppressWarnings("unchecked")
				PacketDecoder<? super PacketHandler<? super Session>> decoder = (PacketDecoder<? super PacketHandler<? super Session>>) c.newInstance();
				for (int opcode : decoder.getPossiblePackets())
					getDecoders().put(opcode, decoder);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		for (Class<?> c : FileUtils.getClasses("org.virtue.network.loginserver.output")) {
			try {
				@SuppressWarnings("unchecked")
				PacketEncoder<? super Object> encoder = (PacketEncoder<? super Object>) c.newInstance();
				getEncoders().put(encoder.getClass(), encoder);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		connect();
	}
	
	/**
	 * Starts this <tt>session factory</tt>
	 */
	private static void connect() {
		try {
			strap = new ClientBootstrap();
			strap.setOption("remoteAddress", new InetSocketAddress("127.0.0.1", 5555));
			strap.setFactory(new NioClientSocketChannelFactory());
			strap.setOption("reuseAddress", true);
			strap.setOption("child.tcpNoDelay", true);
			strap.setPipelineFactory(new LoginServerPipeline());
			strap.connect((SocketAddress) strap.getOption("remoteAddress"));
		} catch (Exception e) {
			System.err.println("Failed initializing a connection to login server.");
			System.exit(-1);
		}
	}

	/**
	 * @return the connection
	 */
	public static LoginServerConnection getConnection() {
		return connection;
	}

	/**
	 * @param connection the connection to set
	 */
	public static void setConnection(LoginServerConnection connection) {
		LoginServer.connection = connection;
	}

	/**
	 * @return the encoders
	 */
	public static Map<Class<?>, PacketEncoder<? super Object>> getEncoders() {
		return encoders;
	}

	/**
	 * @return the decoders
	 */
	public static Map<Integer, PacketDecoder<? super PacketHandler<? super Session>>> getDecoders() {
		return decoders;
	}
}
