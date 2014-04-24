package org.virtue.game.core.filter;

import java.net.ProtocolException;
import java.util.ArrayDeque;
import java.util.Queue;

import org.virtue.game.config.ClientVarps;
import org.virtue.game.core.logic.LogicEvent;
import org.virtue.game.logic.Lobby;
import org.virtue.game.logic.World;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.identity.Account;
import org.virtue.game.logic.node.entity.player.screen.ClientScreen;
import org.virtue.network.protocol.codec.login.LoginType;
import org.virtue.network.protocol.messages.VarpMessage;
import org.virtue.network.protocol.packet.encoder.impl.LoginEncoder;
import org.virtue.network.protocol.packet.encoder.impl.MapSceneEncoder;
import org.virtue.network.protocol.packet.encoder.impl.ScreenConfigEncoder;
import org.virtue.network.protocol.packet.encoder.impl.VarpEncoder;
import org.virtue.network.session.Session;
import org.virtue.network.session.impl.WorldSession;

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
		LoginType type = account.getFlag("login_type", LoginType.WORLD_PART_2);
		System.out.println("Processing login request for: "+account.getUsername().getAccountName()+" of type: "+type.toString().replace("_", " "));

		Player player;
		switch (type) {
		case LOBBY:
			try {
				player = new Player(account);
			} catch (Exception ex) {
				ex.printStackTrace();
				return;
			}
			System.out.println("Created player.");
			((WorldSession) session).setPlayer(player);
			account.getSession().getTransmitter().send(LoginEncoder.class, account);
			System.out.println("Adding player "+account.getUsername().getAccountName()+" to the lobby...");
			Lobby.addPlayer(player);
			int[] varps = ClientVarps.getLobbyVarps();
			for (int i = 0; i < varps.length; i++) {
				int val = varps[i];
				if (val != 0) {
					session.getTransmitter().send(VarpEncoder.class, new VarpMessage(i, val));
				}
			}
			System.out.println("Initialising lobby for player "+account.getUsername().getAccountName());
			player.startLobby();
			break;
		case WORLD_PART_1:
			player = null;
			account.getSession().getTransmitter().send(ScreenConfigEncoder.class, new ClientScreen());
			return;
		case WORLD_PART_2:
			System.out.println("Sending login response data...");
			if (Lobby.contains(account.getUsername().getAccountNameAsProtocol())) {
				player = Lobby.getPlayer(account.getUsername().getAccountNameAsProtocol());
				player.destroy();
				Lobby.removePlayer(account.getUsername().getAccountNameAsProtocol());
			}
			if (World.getWorld().contains(account.getUsername().getAccountNameAsProtocol())) {
				player = World.getWorld().getPlayer(account.getUsername().getAccountNameAsProtocol());
				System.out.println("Found world player.");
			} else {
				System.out.println("Creating new player.");
				player = new Player(account);
				World.getWorld().addPlayer(player);
			}
			account.putFlag("playerIndex", player.getIndex());
			account.getSession().getTransmitter().send(LoginEncoder.class, account);
			player.getViewport().loadViewport();
			session.getTransmitter().send(MapSceneEncoder.class, player.getViewport());
			player.start();
			break;
		default:
			player = null;
			try {
				throw new ProtocolException("Unexpected type: " + type);
			} catch (ProtocolException e) {
				e.printStackTrace();
			}
		}
		if (!type.equals(LoginType.WORLD_PART_1)) {
			((WorldSession) session).setPlayer(player);
			System.out.println(account.getUsername().getName() + " has logged into the " + type.toString().split("_")[0].toLowerCase() + " (index="+player.getIndex()+").");
		}
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
