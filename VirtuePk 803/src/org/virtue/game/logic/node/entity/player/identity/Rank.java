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
}
