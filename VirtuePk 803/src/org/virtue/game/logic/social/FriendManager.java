package org.virtue.game.logic.social;

public interface FriendManager {

	public void init ();
	
	public void shutdown();
	
	/**
	 * Adds the specified friend to the player's friends list, after performing a few checks
	 * @param displayName The display name of the friend to add
	 */
	public void addFriend (String displayName);
	
	/**
	 * Removes the player of the specified name from the current player's friends list
	 * @param displayName the display name of the player to remove
	 */
	public void removeFriend (String displayName);
	
	public void addIgnore (String displayName, boolean tillLogout);
	
	public void removeIgnore (String displayName);
	
	public void setOnlineStatus (OnlineStatus status);
	
	public void sendPrivateMessage (String recipient, String message);
	
	public void setNote (String displayName, String note, boolean isFriendNote);
	
}
