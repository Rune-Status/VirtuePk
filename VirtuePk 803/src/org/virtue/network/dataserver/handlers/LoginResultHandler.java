package org.virtue.network.dataserver.handlers;

import org.virtue.Launcher;
import org.virtue.game.node.entity.player.identity.Account;
import org.virtue.network.dataserver.DataServer;
import org.virtue.network.dataserver.LoginSessions;
import org.virtue.network.dataserver.message.LoginResult;
import org.virtue.network.dataserver.output.LoginVerificationEncoder;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.session.Session;

/**
 * @author Taylor
 * @date Jan 16, 2014
 */
public class LoginResultHandler extends PacketHandler<Session> {

	@Override
	public void handle(Session session) {
		String username = getFlag("username", "");
		Account account;
		if ((account = LoginSessions.getPendingRequest(username)) == null) {
			try {
				throw new Exception("Could not find pending session for " + username + ". FATAL");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		LoginResult result = getFlag("login_result", LoginResult.ERROR);
		if (result.equals(LoginResult.ERROR)) {
			System.err.println("Failed loading login result for player " + username + ".");
		} else {
			DataServer.getConnection().send(LoginVerificationEncoder.class, account);
			LoginSessions.removePendingRequest(username);
			account.putFlag("login_result", result);
			Launcher.getEngine().getLoginFilter().getPendingLogins().add(account);
		}
	}
}
