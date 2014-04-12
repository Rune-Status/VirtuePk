package org.virtue.network.dataserver.handlers;

import java.net.ProtocolException;
import java.util.List;

import org.virtue.Launcher;
import org.virtue.game.Lobby;
import org.virtue.game.WorldHub;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.session.Session;

/**
 * @author Taylor Moon
 * @since Jan 26, 2014
 */
public class LobbyHandler extends PacketHandler<Session> {

	@Override
	public void handle(Session session) {
		List<WorldHub> worlds = getFlag("worlds", null);
		if (worlds == null || worlds.size() < 1) {
			try {
				throw new ProtocolException("Invalid lobby header recieved.");
			} catch (ProtocolException e) {
				Launcher.getEngine().handleException(e);
			}
		}
		Lobby.recieveLoginServerUpdate(worlds);
	}
}
