package org.virtue.network.session.impl;

import java.net.ProtocolException;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.virtue.Launcher;
import org.virtue.game.World;
import org.virtue.game.node.entity.player.Player;
import org.virtue.network.RS2Network;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.protocol.packet.RS3Packet;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

/**
 * @author Taylor
 * @date Jan 14, 2014
 */
public class WorldSession extends Session {

	/**
	 * Represents the player.
	 */
	private Player player;
	
	private boolean isLobby;
	
	/**
	 * Constructs a new {@code WorldSession.java}.
	 * @param context
	 */
	public WorldSession(ChannelHandlerContext context, boolean isLobby) {
		super(context);
		this.isLobby = isLobby;
	}

	@Override
	public void decode(Object message) {
		if (!(message instanceof RS3Packet)) {
			try {
				throw new ProtocolException(message.toString());
			} catch (ProtocolException e) {
				Launcher.getEngine().handleException(e);
			}
		}
		PacketDecoder<? super PacketHandler<? super Session>> decoder = RS2Network.getDecoders().get(((RS3Packet) message).getOpcode());
		if (decoder == null) {
			System.err.println("Unhandled packet: " + ((RS3Packet) message).getOpcode());
			return;
		}
		@SuppressWarnings("unchecked")
		PacketHandler<? super Session> handler = (PacketHandler<? super Session>) decoder.decodePacket(new RS3PacketReader(((RS3Packet) message).getBuffer().buffer()), this, ((RS3Packet) message).getOpcode());
		if (handler != null) {
			handler.handle(this);
		}
	}

	@Override
	public void disconnect() {
		if (!isLobby) {			
			System.out.println("Removing "+player.getAccount().getUsername().getName()+" from the game.");
			player.destroy();
		}
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
