package org.virtue.game.logic.social;

import org.virtue.game.logic.node.entity.player.Player;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public interface FriendsChatManager {
	
	/**
	 * Sends a request for the specified player to join the specified channel
	 * @param player	The player wishing to join the channel
	 * @param owner		The name of the owner who's channel the player wishes to join
	 */
	public void joinChannel(Player player, String owner);
	
	/**
	 * Sends a request for the specified player to leave their current channel
	 * @param player	The player who wishes to leave the channel
	 */
	public void leaveChannel(Player player, boolean isLoggedOut);
	
	public void sendMessage(Player player, String message);
	
}
