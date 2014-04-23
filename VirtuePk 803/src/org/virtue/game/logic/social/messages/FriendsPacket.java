package org.virtue.game.logic.social.messages;

import org.virtue.game.logic.social.internal.Friend;

public class FriendsPacket {

	private Friend[] friends;
	
	private boolean isNameChange;
	private boolean fullUpdate;
	
	public FriendsPacket (Friend[] friends) {
		this.friends = friends;
		fullUpdate = true;
	}
	
	public FriendsPacket (Friend friend, boolean isNameChange) {
		this.isNameChange = isNameChange;
		friends = new Friend[] { friend.clone() };
	}
	
	public boolean isFullUpdate () {
		return fullUpdate;
	}
	
	public boolean isNameChange () {
		return isNameChange;
	}
	
	public Friend[] getFriends () {
		return friends;
	}
}
