package org.virtue.game.logic.social.messages;

import org.virtue.game.logic.node.entity.player.identity.Rank;
import org.virtue.game.logic.social.ChatManager;

public class PrivateMessage {
	
	private String message;
	
	private Rank rank;
	
	private String senderName;
	
	private String senderDisplayName;
	
	private byte[] messageHash;
	
	private boolean incomming;
	
	/**
	 * Creates a new {@code PrivateMessage}
	 * @param message			The message
	 * @param senderReply		The name of the player sending the message, used for responding to
	 * @param senderDisplay		The name of the player sending the message, displayed with the message itself
	 * @param senderRank		The {@link Rank} of the player sending the message
	 */
	public PrivateMessage (String message, String senderReply, String senderDisplay, Rank senderRank) {
		this.message = message;
		this.senderName = senderReply;
		this.senderDisplayName = senderDisplay;
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
	
	/**
	 * Returns whether the message should be treated as an incoming message or not
	 * @return	true if the message should be incoming, false if outgoing
	 */
	public boolean isIncomming () {
		return incomming;
	}
	
	@Override
	public PrivateMessage clone () {
		PrivateMessage msg = new PrivateMessage(this.message, this.senderName, this.senderDisplayName, this.rank);
		msg.incomming = this.incomming;
		msg.messageHash = this.messageHash;
		return msg;
	}
}
