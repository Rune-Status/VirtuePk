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
package org.virtue.game.logic.social;


/**
 * An {@link Interface} for communicating between the chat management module and the friend management module for a player
 *
 * @author Sundays211
 */
public interface FriendManager {

	/**
	 * Runs the initialisation tasks for the player's friends list, such as sending the friends and ignore lists to the player
	 */
	public void init ();
	
	/**
	 * Runs any tasks which need to be run when the player logs out, such as informing others that the player has logged out
	 */
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
	
	/**
	 * Adds the specified friend to the player's ignore list, after performing a few checks
	 * @param displayName 	The display name of the ignore to add
	 * @param tillLogout	Whether the ignore should be removed when the player logs out
	 */
	public void addIgnore (String displayName, boolean tillLogout);
	
	/**
	 * Removes the specified name from the current player's ignore list
	 * @param displayName	The display name of the player to remove
	 */
	public void removeIgnore (String displayName);
	
	/**
	 * Sets the current online status for the player, and informs everyone else of the online status change
	 * @param status	The {@link OnlineStatus} to change to
	 */
	public void setOnlineStatus (OnlineStatus status);
	
	/**
	 * Sends a private message to the specified recipient, after performing checks
	 * @param recipient		The display name of the player to send the message to
	 * @param message		The message to send
	 */
	public void sendPrivateMessage (String recipient, String message);
	
	public void setNote (String displayName, String note, boolean isFriendNote);
	
	/**
	 * Sets the rank of a player within the current player's friends chat
	 * @param displayName	The display name of the player to change the rank of
	 * @param rank			The rank to change to
	 */
	public void setFriendRank (String displayName, ChannelRank rank);
	
	/**
	 * Sets the specified friends chat permission within the player's own friends chat
	 * @param permission	The permission to change
	 * @param rank			The rank to set the permission to
	 */
	public void setFriendsChatPermission (ChannelPermission permission, ChannelRank rank);
	
	/**
	 * Sets the prefix for the player's friends chat
	 * @param prefix	The prefix to set. An empty string or null to disable the friends chat
	 */
	public void setFriendsChatPrefix (String prefix);
	
	/**
	 * Sends the data required to display the friends chat settings interface
	 */
	public void sendFriendsChatSettings ();
	
}
