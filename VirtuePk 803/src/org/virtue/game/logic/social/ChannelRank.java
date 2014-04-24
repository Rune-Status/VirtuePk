package org.virtue.game.logic.social;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public enum ChannelRank {
	GUEST(-1), FRIEND(0), RECRUIT(1), CORPORAL(2), 
	SERGEANT(3), LIEUTENANT(4), CAPTAIN(5), GENERAL(6),
	OWNER(7), JMOD(127);
	
	private final int id;
	
	ChannelRank(int id) {
		this.id = id;
	}
	
	public int getID () {
		return id;
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
