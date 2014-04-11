package org.virtue.network.session;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.virtue.Constants;
import org.virtue.Launcher;
import org.virtue.game.Lobby;
import org.virtue.game.node.entity.player.Player;
import org.virtue.game.node.entity.player.identity.Account;
import org.virtue.network.RS2PacketFilter;
import org.virtue.network.loginserver.LoginServer;
import org.virtue.network.loginserver.LoginSessions;
import org.virtue.network.loginserver.output.LoginRequestEncoder;
import org.virtue.network.messages.LoginResponse;
import org.virtue.network.protocol.packet.RS3PacketBuilder;

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
			getContext().getChannel().write(buffer);
			return;
		}
		if (!(message instanceof Account)) {
			return;
		}
		System.out.println("Processing account...");
		Account account = (Account) message;
		getContext().getChannel().getPipeline().addFirst("packetDecoder", new RS2PacketFilter());
		disconnect();
		WorldSession gameSession = new WorldSession(getContext());
		getContext().setAttachment(gameSession);
		account.setSession(gameSession);
		LoginSessions.getPendingRequests().add(account);
		Player player;
		if (Lobby.getPlayers().contains(account.getUsername().getAccountName())) {
			player = Lobby.getPlayer(account.getUsername().getAccountName());
		} else {
			player = new Player(account);
		}
		gameSession.setPlayer(player);
		if (Constants.LOGIN_SERVER) {
			LoginServer.getConnection().send(LoginRequestEncoder.class, player);
		} else {
			Launcher.getEngine().getLoginFilter().getPendingLogins().add(account);
		}
	}

	@Override
	public void disconnect() {
		
	}
}
