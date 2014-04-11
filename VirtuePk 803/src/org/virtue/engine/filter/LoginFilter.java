package org.virtue.engine.filter;

import java.net.ProtocolException;
import java.util.ArrayDeque;
import java.util.Queue;

import org.virtue.config.ClientVarps;
import org.virtue.engine.logic.LogicEvent;
import org.virtue.game.Lobby;
import org.virtue.game.World;
import org.virtue.game.node.entity.player.Player;
import org.virtue.game.node.entity.player.identity.Account;
import org.virtue.network.RS2Network;
import org.virtue.network.messages.VarpMessage;
import org.virtue.network.protocol.codec.login.LoginType;
import org.virtue.network.protocol.packet.encoder.impl.r803.MapSceneEncoder;
import org.virtue.network.protocol.packet.encoder.impl.r803.LoginEncoder;
import org.virtue.network.protocol.packet.encoder.impl.r803.VarpEncoder;
import org.virtue.network.session.Session;

/**
 * @author Taylor Moon
 * @since Jan 23, 2014
 */
public class LoginFilter extends LogicEvent {

	/**
	 * Represents a queue of pending login requests.
	 */
	private Queue<Account> pending_logins = new ArrayDeque<>();

	@Override
	public void run() {
		if (getPendingLogins().isEmpty()) {
			return;
		}
		Account account = getPendingLogins().poll();
		if (account == null) {
			return;
		}
		final Session session = account.getSession();
		LoginType type = account.getFlag("login_type", LoginType.WORLD);
		System.out.println("Processing login request for: "+account.getUsername().getAccountName());
		account.getSession().getTransmitter().send(LoginEncoder.class, account);
		switch (type) {
		case LOBBY:
			final Player player1 = new Player(account);
			Lobby.getPlayers().add(player1);
			int[] varps = ClientVarps.getLobbyVarps();
			for (int i = 0; i < varps.length; i++) {
				int val = varps[i];
				if (val != 0) {
					session.getTransmitter().send(VarpEncoder.class, new VarpMessage(i, val));
				}
			}
			player1.startLobby();
			break;
		case WORLD:
			final Player player;
			if (Lobby.getPlayers().contains(account.getUsername().getAccountName())) {
				player = Lobby.getPlayer(account.getUsername().getAccountName());
				Lobby.removePlayer(account.getUsername().getAccountName());
			} else {
				player = new Player(account);
			}
			World.getWorld().addPlayer(player);
			player.getViewport().loadViewport();
			session.getTransmitter().send(MapSceneEncoder.class, player.getViewport());
			player.start();
			break;
		default:
			try {
				throw new ProtocolException("Unexpected type: " + type);
			} catch (ProtocolException e) {
				e.printStackTrace();
			}
		}
		System.out.println(account.getUsername().getName() + " has logged into the " + type.toString().toLowerCase() + ".");
	}

	@Override
	public long getIntervalDelay() {
		return 5;
	}

	@Override
	public long getInitialDelay() {
		return 0;
	}

	/**
	 * @return the pending_logins
	 */
	public Queue<Account> getPendingLogins() {
		return pending_logins;
	}

	/**
	 * @param pending_logins
	 *            the pending_logins to set
	 */
	public void setPendingLogins(Queue<Account> pending_logins) {
		this.pending_logins = pending_logins;
	}

	@Override
	public boolean singleShotEvent() {
		return false;
	}
}
