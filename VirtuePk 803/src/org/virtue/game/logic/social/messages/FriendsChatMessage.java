package org.virtue.game.logic.social.messages;

import org.virtue.game.logic.node.entity.player.identity.Rank;
import org.virtue.game.logic.social.ChatManager;

public class FriendsChatMessage {
	
	private String message;
	
	private Rank rank;
	
	private String senderName;
	
	private String senderDisplayName;
	
	private byte[] messageHash;
	
	private String messagePrefix;
	
	/**
	 * Creates a new {@code FriendsChatMessage}
	 * @param message			The message
	 * @param senderReply		The name of the player sending the message, used for responding to
	 * @param senderDisplay		The name of the player sending the message, displayed with the message itself
	 * @param senderRank		The {@link Rank} of the player sending the message
	 * @param channelPrefix		The prefix (name) of the channel in which this message was sent
	 */
	public FriendsChatMessage (String message, String senderReply, String senderDisplay, Rank senderRank, String channelPrefix) {
		this.message = message;
		this.senderName = senderReply;
		this.senderDisplayName = senderDisplay;
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
	 * Returns the name that should be displayed with the message
	 * @return	The sender display name
	 */
	public String getSenderDisplay () {
		return senderDisplayName;
	}
	
	/**
	 * Returns the name that should be used to respond to the player
	 * @return	The sender reply-to name
	 */
	public String getSenderRespond () {
		return senderName;
	}
	
	/**
	 * Returns whether the sender has different display and respond-to names
	 * @return	true if the sender has different names
	 */
	public boolean hasDifferentNames () {
		return senderDisplayName.equals(senderName);
	}
	
	/**
	 * Returns the unique hash code for the message
	 * @return	The hash code
	 */
	public byte[] getMessageHash () {
		return messageHash;
	}
	
}
