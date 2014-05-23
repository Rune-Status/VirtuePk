/*
 * This file is part of the RS3Emulator social module.
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
 * An {@link Object} that represents a private chat message
 *
 * @author Sundays211
 */
public class PrivateMessage {
	
	private String message;
	
	private Rank rank;
	
	private String senderName;
	
	private String nameUnfiltered;
	
	private byte[] messageHash;
	
	private boolean incomming;
	
	/**
	 * Creates a new {@code PrivateMessage}
	 * @param message			The message
	 * @param senderName		The name of the player sending the message
	 * @param nameUnfiltered	The unfiltered name of the player sending the message
	 * @param senderRank		The {@link Rank} of the player sending the message
	 */
	public PrivateMessage (String message, String senderName, String nameUnfiltered, Rank senderRank) {
		this.message = message;
		this.senderName = senderName;
		this.nameUnfiltered = nameUnfiltered;
		this.rank = senderRank;
		this.incomming = true;
		this.messageHash = ChatManager.generateMessageHash();
	}
	
	/**
	 * Sets whether the message should be treated as an incoming or outgoing message
	 * @param incomming		true for incoming, false for outgoing
	 */
	public void setIncomming (boolean incomming) {
		this.incomming = incomming;
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
	 * Returns the unfiltered display name of the sender
	 * @return	The unfiltered sender display name
	 */
	public String getSenderNameUnfiltered () {
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
	
	/**
	 * Returns whether the message should be treated as an incoming message or not
	 * @return	true if the message should be incoming, false if outgoing
	 */
	public boolean isIncomming () {
		return incomming;
	}
	
	@Override
	public PrivateMessage clone () {
		PrivateMessage msg = new PrivateMessage(this.message, this.senderName, this.nameUnfiltered, this.rank);
		msg.incomming = this.incomming;
		msg.messageHash = this.messageHash;
		return msg;
	}
}
