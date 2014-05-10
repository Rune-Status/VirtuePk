package org.virtue.game.logic.social.clans;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public enum ClanRank {
	GUEST(-1), RECRUIT(0), CORPORAL(1), SERGEANT(2),
	LIEUTENANT(3), CAPTAIN(4), GENERAL(5),
	ADMIN(100), ORGANISER(101), COORDINATOR(102),
	OVERSEER(103), DEPUTY_OWNER(125), OWNER(126), JMOD(127);
	
	private final int id;
	
	ClanRank (int id) {
		this.id = id;
	}
	
	public int getID () {
		return id;
	}
}
