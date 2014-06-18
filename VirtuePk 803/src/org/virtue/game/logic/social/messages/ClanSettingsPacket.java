package org.virtue.game.logic.social.messages;

import org.virtue.game.logic.social.clans.ClanRank;

public class ClanSettingsPacket {
	
	public static class Member {
		
		private final String displayName;
		private final ClanRank rank;
		private final int varClanMember;
		private final int joinDay;
		
		
		/**
		 * Constructs a new {@code User} object for use within the {@code ClanSettingsPacket}
		 * @param displayName	The display name of the player
		 * @param rank			The player's rank within the clan
		 * @param varClanMember	The var data for this clan member
		 * @param joinDay		The day on which the member joined the clan
		 */
		public Member (String displayName, ClanRank rank, int varClanMember, int joinDay) {
			this.displayName = displayName;
			this.rank = rank;
			this.varClanMember = varClanMember;
			this.joinDay = joinDay;
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
		 * Gets the variable data for this clan member
		 * @return	The varClanMember data
		 */
		public int getVarClanMember () {
			return varClanMember;
		}
		
		/**
		 * Gets the day on which the member joined the clan
		 * @return	The join day
		 */
		public int getJoinDay () {
			return joinDay;
		}
	}
	
	public static class VarClanSetting {
		
		private final int key;
		private final Object value;
		
		public VarClanSetting (int key, Object value) {
			this.key = key;
			this.value = value;
		}
		
		public int getKey () {
			return key;
		}
		
		public Object getValue () {
			return value;
		}
	}
	
	private final Member[] members;
	
	private final String[] bans;
	
	private final boolean isGuestData;
	
	private final boolean allowGuests;
	
	private final ClanRank minTalk, minKick;
	
	private final int updateNum;
	
	private final String clanName;
	
	private final VarClanSetting[] varClanSettings;
	
	public ClanSettingsPacket (boolean guestData, String clanName, Member[] members, String[] bans, int updateNum, boolean allowGuests, ClanRank minTalk, ClanRank minKick, VarClanSetting[] varClanSettings) {
		this.isGuestData = guestData;
		this.clanName = clanName;
		this.members = members;
		this.bans = bans;
		this.allowGuests = allowGuests;
		this.updateNum = updateNum;
		this.minTalk = minTalk;
		this.minKick = minKick;
		this.varClanSettings = varClanSettings;
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
	
	/**
	 * Returns an array containing the misc setting data for the clan
	 * @return	An array of VarClanSettings
	 */
	public VarClanSetting[] getVarClanSettings () {
		return varClanSettings;
	}

}
