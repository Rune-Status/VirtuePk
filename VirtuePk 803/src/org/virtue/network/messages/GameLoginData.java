package org.virtue.network.messages;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 5, 2014
 */
public class GameLoginData {
	
	public final int rights;
	public final String displayName;
	public final int playerIndex;
	public final boolean isMember;
	
	public GameLoginData (int rights, String displayName, int playerIndex, boolean isMembersWorld) {
		this.rights = rights;
		this.displayName = displayName;
		this.playerIndex = playerIndex;
		this.isMember = isMembersWorld;
	}
	
}
