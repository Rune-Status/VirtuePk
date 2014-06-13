package org.virtue.game.logic.node.entity.player.identity;


/**
 * @author Taylor
 * @date Jan 16, 2014
 */
public enum Rank {

	/**
	 * Represents a normal player.
	 */
	PLAYER(0), 
	
	/**
	 * Represents a player moderator.
	 */
	MODERATOR(1), 
	
	/**
	 * Represents a psyc administrator.
	 */
	ADMINISTRATOR(2);
	
	private final int code;
	
	Rank (int code) {
		this.code = code;
	}
	
	/**
	 * Gets the id used to represent this rank
	 * @return	the ID.
	 */
	public int getID () {
		return code;
	}
	
	public static Rank forID (int id) {
		if (id < Rank.values().length) {
			Rank rank = Rank.values()[id];
			if (rank.code == id) {
				return rank;
			}
		}
		for (Rank r : Rank.values()) {
			if (r.code == id) {
				return r;
			}
		}
		return null;
	}
}
