package org.virtue.network.protocol.codec.login;

/**
 * @author Taylor
 * @version 1.0
 */
public enum LoginType {

	/**
	 * Represents logging into the <i>lobby</i>.
	 */
	LOBBY,
	
	/**
	 * Represents the initial world login (before NIS is sent)
	 */
	WORLD_PART_1, 
	
	/**
	 * Represents the world login stage after NIS is sent
	 */
	WORLD_PART_2
	
}
