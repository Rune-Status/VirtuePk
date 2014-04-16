package org.virtue.network.protocol.messages;

import org.virtue.game.World;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 5, 2014
 */
public class LobbyLoginData {
	
	public final int rights;
	public final long membershipEndDate;
	public final int membershipFlags;
	public final int lastLoggedInDay;
	public final int recoveryQuestionsSetDay;
	public final int messageCount;
	public final String lastLoginIp;
	public final int emailStatus;
	public final String displayName;
	public final String defaultWorldEndpoint;
	public final int defaultWorldNodeID;
	
	public LobbyLoginData (int rights, long memberEndDate, int memberFlags, int lastLoginDay, int recoverySetDay, 
			int msgCount, String loginIP, int emailStatus, String displayName, World defaultWorld) {
		this.rights = rights;
		this.membershipEndDate = memberEndDate;
		this.membershipFlags = memberFlags;
		this.lastLoggedInDay = lastLoginDay;
		this.recoveryQuestionsSetDay = recoverySetDay;
		this.messageCount = msgCount;
		this.lastLoginIp = loginIP;
		this.emailStatus = emailStatus;
		this.displayName = displayName;
		this.defaultWorldEndpoint = defaultWorld.getIp();
		this.defaultWorldNodeID = defaultWorld.getNodeID();
	}
}
