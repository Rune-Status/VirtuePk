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
import org.virtue.game.logic.social.ChatManager;

/**
 * An {@link Object} that represents a friends chat message
 *
 * @author Sundays211
 */
public class FriendsChatMessage {
	
	private String message;
	
	private Rank rank;
	
	private String senderName;
	
	private String nameUnfiltered;
	
	private byte[] messageHash;
	
	private String messagePrefix;
	
	/**
	 * Creates a new {@code FriendsChatMessage}
	 * @param message			The message
	 * @param senderName		The name of the player sending the message
	 * @param nameUnfiltered	The unfiltered name of the player sending the message
	 * @param senderRank		The {@link Rank} of the player sending the message
	 * @param channelPrefix		The prefix (name) of the channel in which this message was sent
	 */
	public FriendsChatMessage (String message, String senderName, String nameUnfiltered, Rank senderRank, String channelPrefix) {
		this.message = message;
		this.senderName = senderName;
		this.nameUnfiltered = nameUnfiltered;
		this.rank = senderRank;
		this.messagePrefix = channelPrefix;
		this.messageHash = ChatManager.generateMessageHash();
	}
	
	/**
	 * Returns the message itself
	 * @return	The message
	 */
	public String getMessage () {
		return message;
	}
	
	/**
	 * Returns the rank of the player sending the message
	 * @return	The player's rank
	 */
	public Rank getRank () {
		return rank;
	}
	
	/**
	 * Returns the prefix for the channel in which the message was sent
	 * @return	The channel prefix
	 */
	public String getPrefix () {
		return messagePrefix;
	}
	
	/**
	 * Returns the unfiltered display name of the sender
	 * @return	The sender's unfiltered display name
	 */
	public String getNameUnfiltered () {
		return nameUnfiltered;
	}
	
	/**
	 * Returns the filtered display name of the sender
	 * @return	The sender's display name
	 */
	public String getSenderName () {
		return senderName;
	}
	
	/**
	 * Returns whether the sender has a filtered name
	 * @return	true if the sender has a filtered name
	 */
	public boolean hasFilteredName () {
		return nameUnfiltered.equals(senderName);
	}
	
	/**
	 * Returns the unique hash code for the message
	 * @return	The hash code
	 */
	public byte[] getMessageHash () {
		return messageHash;
	}
	
}
