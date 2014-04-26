package org.virtue.game.logic.social;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public enum ChannelRank {
	GUEST(-1, "Guest"), FRIEND(0, "Friend"), RECRUIT(1, "Recruit"), CORPORAL(2, "Corporal"), 
	SERGEANT(3, "Sergeant"), LIEUTENANT(4, "Lieutenant"), CAPTAIN(5, "Captain"), 
	GENERAL(6, "General"), OWNER(7, "Channel Owner"), JMOD(127, "Jagex Moderator");
	
	private final int id;
	private final String name;
	
	ChannelRank(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getID () {
		return id;
	}
	
	public String getName () {
		return name;
	}
	
	public static ChannelRank forID (int id) {
		if (id <= ChannelRank.values().length && id > -2) {
			ChannelRank rank = ChannelRank.values()[id+1];
			if (rank.id == id) {
				return rank;
			}
		}
		for (ChannelRank r : ChannelRank.values()) {
			if (r.id == id) {
				return r;
			}
		}
		return null;
	}
}
