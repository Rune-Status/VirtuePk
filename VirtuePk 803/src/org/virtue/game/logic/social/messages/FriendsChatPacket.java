package org.virtue.game.logic.social.messages;

import org.virtue.game.logic.social.ChannelRank;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class FriendsChatPacket {
	
	public class User {
		private String replyName;
		private String displayName;
		private ChannelRank rank;
		private int nodeID;
		private String worldName;
		
		public User (String replyName, String displayName, ChannelRank rank, int nodeID, String worldName) {
			this.replyName = replyName;
			this.displayName = displayName;
			this.rank = rank;
			this.nodeID = nodeID;
			this.worldName = worldName;
		}
		
		/**
		 * Gets the display name of the player
		 * @return	The display name
		 */
		public String getDisplayName () {
			return displayName;
		}
		
		/**
		 * Gets the name used for comparing and responding to
		 * @return The reply-to name
		 */
		public String getReplyName () {
			return replyName;
		}
		
		public ChannelRank getRank () {
			return rank;
		}
		
		public int getWorldNodeID () {
			return nodeID;
		}
		
		public String getWorldName () {
			return worldName;
		}
		
		public boolean hasReplyName () {
			return !displayName.equalsIgnoreCase(replyName);
		}
	}
	
	private String ownerName;
	
	private String channelName;
	
	private ChannelRank kickReq;
	
	private User[] users;
	
	private boolean fullUpdate;
	
	public FriendsChatPacket (User[] users, String ownerName, String channelName, ChannelRank kickReq) {
		this.ownerName = ownerName;
		this.channelName = channelName;
		this.kickReq = kickReq;
		this.users = users;
		fullUpdate = true;
	}
	
	public FriendsChatPacket (User user) {
		fullUpdate = false;
		users = new User[] { user }; 
	}
	
	public boolean isFullUpdate () {
		return fullUpdate;
	}
	
	public String getChannelName () {
		return channelName;
	}
	
	public String getOwnerName () {
		return ownerName;
	}
	
	public ChannelRank getKickReq () {
		return kickReq;
	}
	
	public User[] getUsers () {
		return users;
	}
	
}
