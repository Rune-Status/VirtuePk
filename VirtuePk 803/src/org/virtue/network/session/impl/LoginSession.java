package org.virtue.network.session.impl;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.virtue.Constants;
import org.virtue.Launcher;
import org.virtue.game.Lobby;
import org.virtue.game.node.entity.player.Player;
import org.virtue.game.node.entity.player.identity.Account;
import org.virtue.network.RS3PacketFilter;
import org.virtue.network.loginserver.DataServer;
import org.virtue.network.loginserver.LoginSessions;
import org.virtue.network.loginserver.output.LoginRequestEncoder;
import org.virtue.network.messages.LoginResponse;
import org.virtue.network.protocol.codec.login.LoginType;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.session.Session;

/**
 * @author Taylor
 * @date Jan 14, 2014
 */
public class LoginSession extends Session {

	/**
	 * Constructs a new {@code LoginSession.java}.
	 * @param context
	 */
	public LoginSession(ChannelHandlerContext context) {
		super(context);
	}

	@Override
	public void decode(Object message) {
		if (message instanceof LoginResponse) {
			LoginResponse response = ((LoginResponse) message);
			System.out.println("Sending login response: "+response.getReturnCode());
			RS3PacketBuilder buffer = new RS3PacketBuilder();
			buffer.put(response.getReturnCode());
			//getContext().getChannel().write(buffer);
                        getTransmitter().send(buffer);
			return;
		}
		if (!(message instanceof Account)) {
			return;
		}
		System.out.println("Processing account...");
		Account account = (Account) message;
		LoginType type = account.getFlag("login_type", LoginType.WORLD_PART_2);
		LoginSessions.getPendingRequests().add(account);
		Player player;
		if (Lobby.getPlayers().contains(account.getUsername().getAccountName())) {
			player = Lobby.getPlayer(account.getUsername().getAccountName());
		} else {
			player = new Player(account);
		}
		if (Constants.LOGIN_SERVER) {
			DataServer.getConnection().send(LoginRequestEncoder.class, player);
		} else {
			Launcher.getEngine().getLoginFilter().getPendingLogins().add(account);
		}
		if (type.equals(LoginType.WORLD_PART_1)) {
			account.setSession(this);
			return;//Don't change the session type yet if the login type is world part 1
		}
		getContext().getChannel().getPipeline().addFirst("packetDecoder", new RS3PacketFilter());
		disconnect();
		WorldSession gameSession = new WorldSession(getContext(), type.equals(LoginType.LOBBY));
		getContext().setAttachment(gameSession);
		account.setSession(gameSession);
		gameSession.setPlayer(player);
	}

	@Override
	public void disconnect() {
		
	}
}
