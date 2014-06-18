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
package org.virtue.game.logic.social.clans;

import org.virtue.game.logic.social.SocialUserAPI;

/**
 * An {@link Interface} for communicating between the chat management module and the clan channel manager module
 *
 * @author Sundays211
 * @since May 22, 2014
 */
public interface ClanChannelAPI {
	
	/**
	 * Sends a request to join the player's clan chat channel
	 * @param player	The player joining the channel
	 */
	public void joinMyChannel(SocialUserAPI player);
	
	/**
	 * Sends a request for the player to join another clan's chat channel as a guest
	 * @param player	The player joining the channel
	 * @param clanName	The name of the clan the player is attempting to join the chat channel of
	 */
	public void joinGuestChannel (SocialUserAPI player, String clanName);
	
	/**
	 * Sends a request to remove the player from their current clan chat channel
	 * @param player	The player leaving the channel
	 * @param isGuest	True if the player is leaving their guest channel
	 * @param isLogout	If true, the player will rejoin the channel when they next log in
	 */
	public void leaveChannel (SocialUserAPI player, boolean isGuest, boolean isLogout);
	
	/**
	 * Sends a message in a clan channel the player is currently in
	 * @param player	The player sending the message
	 * @param message	The message to send
	 * @param isGuest	Whether the message is being sent in a guest channel or not
	 */
	public void sendMessage (SocialUserAPI player, String message, boolean isGuest);

}
