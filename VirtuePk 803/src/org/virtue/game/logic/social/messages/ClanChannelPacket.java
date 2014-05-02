package org.virtue.game.logic.social.messages;

import org.virtue.game.logic.social.clans.ClanRank;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class ClanChannelPacket {
	public static class User {
		private final String replyName;
		private final String displayName;
		private final ClanRank rank;
		private final int nodeID;
		private final String worldName;
		
		/**
		 * Constructs a new {@code User} object for use within the {@code ClanChannelPacket}
		 * @param displayName	The display name of the player
		 * @param replyName		The reply-to name of the player
		 * @param rank			The player's rank within the clan
		 * @param nodeID		The node ID of the world the player is in
		 * @param worldName		The name of the world the player is in
		 */
		public User (String displayName, String replyName, ClanRank rank, int nodeID, String worldName) {
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
		
		/**
		 * Gets the rank of the player within the clan
		 * @return	The rank
		 */
		public ClanRank getRank () {
			return rank;
		}
		
		/**
		 * Gets the node ID of the world the player is currently in
		 * @return	The world node ID
		 */
		public int getWorldNodeID () {
			return nodeID;
		}
		
		/**
		 * Gets the name of the world the player is currently in
		 * @return	The world name
		 */
		public String getWorldName () {
			return worldName;
		}
		
		/**
		 * Returns whether the 'reply-to' name is different to the 'display' name
		 * @return	True if the names are different, false if they are the same
		 */
		public boolean hasReplyName () {
			if (replyName == null) {
				return false;
			}
			return !displayName.equalsIgnoreCase(replyName);
		}
	}
}
