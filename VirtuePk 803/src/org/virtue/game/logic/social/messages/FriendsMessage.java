package org.virtue.game.logic.social.messages;

import org.virtue.game.logic.social.Friend;

public class FriendsMessage {

	private Friend[] friends;
	
	private boolean isNameChange;
	private boolean fullUpdate;
	
	public FriendsMessage (Friend[] friends) {
		this.friends = friends;
		fullUpdate = true;
	}
	
	public FriendsMessage (Friend friend, boolean isNameChange) {
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
