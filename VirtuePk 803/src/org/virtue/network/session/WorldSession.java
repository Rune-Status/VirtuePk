package org.virtue.network.session;

import java.net.ProtocolException;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.virtue.Launcher;
import org.virtue.game.World;
import org.virtue.game.node.entity.player.Player;
import org.virtue.network.RS2Network;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.protocol.packet.RS2Packet;
import org.virtue.network.protocol.packet.RS2PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;

/**
 * @author Taylor
 * @date Jan 14, 2014
 */
public class WorldSession extends Session {

	/**
	 * Represents the player.
	 */
	private Player player;
	
	/**
	 * Constructs a new {@code WorldSession.java}.
	 * @param context
	 */
	public WorldSession(ChannelHandlerContext context) {
		super(context);
	}

	@Override
	public void decode(Object message) {
		if (!(message instanceof RS2Packet)) {
			try {
				throw new ProtocolException(message.toString());
			} catch (ProtocolException e) {
				Launcher.getEngine().handleException(e);
			}
		}
		PacketDecoder<? super PacketHandler<? super Session>> decoder = RS2Network.getDecoders().get(((RS2Packet) message).getOpcode());
		if (decoder == null) {
			System.err.println("Unhandled packet: " + ((RS2Packet) message).getOpcode());
			return;
		}
		@SuppressWarnings("unchecked")
		PacketHandler<? super Session> handler = (PacketHandler<? super Session>) decoder.decodePacket(new RS2PacketReader(((RS2Packet) message).getBuffer().buffer()), this, ((RS2Packet) message).getOpcode());
		if (handler != null) {
			handler.handle(this);
		}
	}

	@Override
	public void disconnect() {
		//World.getWorld().removePlayer(player);
		//player.destroy();
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
}