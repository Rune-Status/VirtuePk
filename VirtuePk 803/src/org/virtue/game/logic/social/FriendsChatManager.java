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
package org.virtue.game.logic.social;

import org.virtue.game.logic.node.entity.player.Player;

/**
 * An {@link Interface} for communicating between the chat management module and the friends chat manager module
 *
 * @author Sundays211
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
	
	/**
	 * Sends a normal message within the friends chat channel
	 * @param player	The player sending the message
	 * @param message	The message being sent
	 */
	public void sendMessage(Player player, String message);
	
	/**
	 * Sends a request to kick/ban a user from the channel
	 * @param player	The player sending the request
	 * @param user		The name of the user being kicked/banned.
	 */
	public void kickBanUser (Player player, String user);
	
}
