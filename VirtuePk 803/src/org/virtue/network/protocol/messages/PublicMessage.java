package org.virtue.network.protocol.messages;

import org.virtue.game.logic.node.entity.player.identity.Rank;

public class PublicMessage {
	
	private String message;
	
	private Rank rank;
	
	private int effects = 0;
	
	private int playerIndex = 0;
	
	/**
	 * Creates a new {@code PublicMessage}
	 * @param message		The message
	 * @param effects		The effects for the message
	 * @param playerIndex	The index of the player sending the message
	 * @param rank			The {@link Rank} of the player sending the message
	 */
	public PublicMessage (String message, int effects, int playerIndex, Rank rank) {
		this.message = message;
		this.effects = effects;
		this.playerIndex = playerIndex;
		this.rank = rank;
	}
	
	/**
	 * Returns the message itself
	 * @return	The message
	 */
	public String getMessage () {
		return message;
	}
	
	/**
	 * Returns the index of the player sending the message
	 * @return	The player index
	 */
	public int getPlayerIndex () {
		return playerIndex;
	}
	
	/**
	 * Returns the rank of the player sending the message
	 * @return	The player's rank
	 */
	public Rank getRank () {
		return rank;
	}
	
	/**
	 * Returns the chat effects of the message
	 * @return	The chat effects
	 */
	public int getEffects () {
		return effects;
	}
}
