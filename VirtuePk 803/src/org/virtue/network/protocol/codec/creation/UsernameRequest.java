package org.virtue.network.protocol.codec.creation;

/**
 * 
 * @author Virtue Development Team 2014 (c).
 * @since Apr 14, 2014
 */
public class UsernameRequest {

	private String username;

	public UsernameRequest(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

}
