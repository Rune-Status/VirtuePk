/*
 * This file is part of RS3Emulator.
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

import org.virtue.game.logic.node.entity.player.identity.Rank;

/**
 * An {@link Object} that represents a public chat message
 *
 * @author Sundays211
 */
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
