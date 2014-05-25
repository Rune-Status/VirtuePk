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
 * A packet that represents a clan chat channel message
 *
 * @author Sundays211
 */
public class ClanChannelMessage {
	
	private final boolean isGuestCc;
	
	private final String message;
	
	private final String senderName;
	
	private final Rank senderRank;
	
	private final byte[] messageHash;
	
	/**
	 * Creates a new {@code ClanChannelMessage}
	 * @param isGuestCc		Whether this message is for a guest clan channel
	 * @param message		The message
	 * @param senderName	The name of the player sending the message
	 * @param senderRank	The {@link Rank} of the player sending the message
	 */
	public ClanChannelMessage (boolean isGuestCc, String message, String senderName, Rank senderRank) {
		this.isGuestCc = isGuestCc;
		this.message = message;
		this.senderRank = senderRank;
		this.senderName = senderName;
		this.messageHash = ChatManager.generateMessageHash();
	}
	
	/**
	 * Creates a new {@code ClanChannelMessage} based on an existing message
	 * @param message		The message to retrieve data from
	 * @param isGuestCc		Whether this represents a message in a guest clan channel
	 */
	public ClanChannelMessage (ClanChannelMessage message, boolean isGuestCc) {
		this.senderName = message.senderName;
		this.senderRank = message.senderRank;
		this.message = message.message;
		this.messageHash = message.messageHash;
		this.isGuestCc = isGuestCc;
	}
	
	/**
	 * Returns whether or not this is a message from a guest clan chat
	 * @return	True if this is a guest cc message, false if it's a main cc message
	 */
	public boolean isGuestCc () {
		return isGuestCc;
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
		return senderRank;
	}
	
	/**
	 * Returns the display name of the sender
	 * @return	The sender's display name
	 */
	public String getSenderName () {
		return senderName;
	}
	
	/**
	 * Returns the unique hash code for the message
	 * @return	The hash code
	 */
	public byte[] getMessageHash () {
		return messageHash;
	}

}
