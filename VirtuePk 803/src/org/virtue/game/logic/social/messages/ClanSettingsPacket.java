package org.virtue.game.logic.social.messages;

import org.virtue.game.logic.social.clans.ClanRank;

public class ClanSettingsPacket {
	
	public static class Member {
		
		private final String displayName;
		private final ClanRank rank;
		
		/**
		 * Constructs a new {@code User} object for use within the {@code ClanSettingsPacket}
		 * @param displayName	The display name of the player
		 * @param rank			The player's rank within the clan
		 */
		public Member (String displayName, ClanRank rank) {
			this.displayName = displayName;
			this.rank = rank;
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
	}
	
	private final Member[] members;
	
	private final String[] bans;
	
	private final boolean isGuestData;
	
	private final boolean allowGuests;
	
	private final ClanRank minTalk, minKick;
	
	private final int updateNum;
	
	private final String clanName;
	
	public ClanSettingsPacket (boolean guestData, String clanName, Member[] members, String[] bans, int updateNum, boolean allowGuests, ClanRank minTalk, ClanRank minKick) {
		this.isGuestData = guestData;
		this.clanName = clanName;
		this.members = members;
		this.bans = bans;
		this.allowGuests = allowGuests;
		this.updateNum = updateNum;
		this.minTalk = minTalk;
		this.minKick = minKick;
	}
	
	/**
	 * Returns whether or not this is clan data for a guest clan
	 * @return	True if this is guest clan data, false if it's main clan data
	 */
	public boolean isGuestData () {
		return isGuestData;
	}
	
	/**
	 * Returns the current settings update number
	 * @return	The update number
	 */
	public int getUpdateNumber () {
		return updateNum;
	}
	
	/**
	 * Returns the name of the clan
	 * @return	The clan name
	 */
	public String getClanName () {
		return clanName;
	}
	
	/**
	 * Returns the minimum rank needed to talk in the clan chat channel
	 * @return	The minimum talk rank
	 */
	public ClanRank getMinTalk () {
		return minTalk;
	}
	
	/**
	 * Returns the minimum rank needed to kick guests from the clan chat channel
	 * @return	The minimum kick rank
	 */
	public ClanRank getMinKick () {
		return minKick;
	}
	
	/**
	 * Returns whether guests are allowed to join the associated clan chat channel
	 * @return	True if guests are allowed to join, false otherwise
	 */
	public boolean allowNonMembers () {
		return allowGuests;
	}
	
	/**
	 * Returns an array of members currently in the clan
	 * @return	An array of members
	 */
	public Member[] getMembers () {
		return members;
	}
	
	/**
	 * Returns an array of people currently banned from the clan
	 * @return	A string array containing the names of all banned players
	 */
	public String[] getBans () {
		return bans;
	}

}
