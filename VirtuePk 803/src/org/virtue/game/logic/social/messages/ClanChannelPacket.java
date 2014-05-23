/*
 * This file is part of the RS3Emulator social module.
 *
 * RS3Emulator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RS3Emulator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RS3Emulator.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.virtue.game.logic.social.messages;

import org.virtue.game.logic.social.clans.ClanRank;

/**
 * A packet that initialises the clan chat channel
 *
 * @author Sundays211
 */
public class ClanChannelPacket {
	
	public static class User {
		private final String displayName;
		private final ClanRank rank;
		private final int nodeID;
		
		/**
		 * Constructs a new {@code User} object for use within the {@code ClanChannelPacket}
		 * @param displayName	The display name of the player
		 * @param rank			The player's rank within the clan
		 * @param nodeID		The node ID of the world the player is in
		 */
		public User (String displayName, ClanRank rank, int nodeID) {
			this.displayName = displayName;
			this.rank = rank;
			this.nodeID = nodeID;
		}
		
		/**
		 * Gets the display name of the player
		 * @return	The display name
		 */
		public String getDisplayName () {
			return displayName;
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
	}
		
	private final long clanHash;
	
	private final long updateNumber;
	
	private final String clanName;
	
	private final ClanRank minTalkRank;
	
	private final ClanRank minKickRank;
	
	private final User[] users;
	
	private final boolean isGuestCc;
	
	/**
	 * Constructs a new {@code ClanChannelPacket} representing a full update to the clan chat channel
	 * @param guestCc		True if this update is for a guest clan chat, false if it is for the main channel
	 * @param users			An array of users currently in the channel
	 * @param clanHash		The unique hash for the clan
	 * @param updateNum		The current clan channel update number
	 * @param clanName		The name of the clan
	 * @param minTalk		The minimum rank required to talk in the channel
	 * @param minKick		The minimum rank required to kick guests from the channel
	 */
	public ClanChannelPacket (boolean guestCc, User[] users, long clanHash, long updateNum, String clanName, ClanRank minTalk, ClanRank minKick) {
		this.isGuestCc = guestCc;
		this.users = users;
		this.clanHash = clanHash;
		this.updateNumber = updateNum;
		this.clanName = clanName;
		this.minTalkRank = minTalk;
		this.minKickRank = minKick;
	}
	
	/**
	 * Returns whether or not this is an update for a guest clan chat
	 * @return	True if this is a guest cc update, false if it's a main cc update
	 */
	public boolean isGuestUpdate () {
		return isGuestCc;
	}
	
	/**
	 * Returns the unique hash code for the clan
	 * @return	The clan hash
	 */
	public long getClanHash () {
		return clanHash;
	}
	
	/**
	 * Returns the current channel update number
	 * @return	The update number
	 */
	public long getUpdateNumber () {
		return updateNumber;
	}
	
	/**
	 * Returns the name of the clan
	 * @return	The clan name
	 */
	public String getClanName () {
		return clanName;
	}
	
	/**
	 * Returns the minimum rank needed to talk in the channel
	 * @return	The minimum talk rank
	 */
	public ClanRank getMinTalk () {
		return minTalkRank;
	}
	
	/**
	 * Returns the minimum rank needed to kick guests from the channel
	 * @return	The minimum kick rank
	 */
	public ClanRank getMinKick () {
		return minKickRank;
	}
	
	/**
	 * Returns an array of users currently in the clan chat channel
	 * @return	An array of users
	 */
	public User[] getUsers () {
		return users;
	}
}
