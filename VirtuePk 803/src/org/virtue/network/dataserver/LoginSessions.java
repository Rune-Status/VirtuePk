package org.virtue.network.dataserver;

import java.util.ArrayList;
import java.util.List;

import org.virtue.game.node.entity.player.identity.Account;

/**
 * @author Taylor Moon
 * @date Jan 16, 2014
 */
public class LoginSessions {

	/**
	 * Represents a list of pending requests.
	 */
	private static List<Account> pending_requests = new ArrayList<>();
	
	/**
	 * Returns a pending request.
	 * @param username The username of the requester.
	 * @return The player.
	 */
	public static Account getPendingRequest(String username) {
		for (Account account : pending_requests) {
			if (account.getUsername().getAccountName().equalsIgnoreCase(username)) {
				return account;
			}
		}
		return null;
	}
	
	/**
	 * Returns a pending request.
	 * @param username The username of the requester.
	 * @return The player.
	 */
	public static void removePendingRequest(String username) {
		for (int i = 0; i < pending_requests.size(); i++) {
			if (pending_requests.get(i).getUsername().getAccountName().equalsIgnoreCase(username)) {
				pending_requests.remove(i);
			}
		}
	}
	
	/**
	 * @return the pending_requests
	 */
	public static List<Account> getPendingRequests() {
		return pending_requests;
	}

	/**
	 * @param pending_requests the pending_requests to set
	 */
	public static void setPendingRequests(List<Account> pending_requests) {
		LoginSessions.pending_requests = pending_requests;
	}
	
}
