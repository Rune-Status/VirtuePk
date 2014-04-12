package org.virtue.network.dataserver.handlers;

import java.util.ArrayList;

import org.virtue.Constants;
import org.virtue.game.Lobby;
import org.virtue.game.WorldHub;
import org.virtue.network.dataserver.DataServer;
import org.virtue.network.dataserver.message.WorldSubmissionResponse;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.session.Session;

/**
 * @author Taylor
 * @date Jan 14, 2014
 */
public class WorldSubmissionHandler extends PacketHandler<Session> {

	@Override
	public void handle(Session session) {
		WorldSubmissionResponse response = getFlag("response", WorldSubmissionResponse.DENIED_ACCESS);
		switch(response) {
		case DENIED_ACCESS:
			System.err.println("Login server denied submission access to remote address.");
			break;
		case EXISTING_WORLD:
			System.err.println("Login server contains world " + Constants.WORLD_ID + ", submission access denied.");
			break;
		case SUCCESSFUL_SUBMISSION:
			Lobby.recieveLoginServerUpdate(getFlag("worlds", new ArrayList<WorldHub>()));
			System.out.println("Successful connection to login server on server " + DataServer.getConnection().getContext().getChannel().getId() + " - " + DataServer.getConnection().getContext().getChannel().getRemoteAddress());
			break;
		default:
			break;
		}
	}
}
