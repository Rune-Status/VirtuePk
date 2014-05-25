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

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.identity.Rank;
import org.virtue.game.logic.social.internal.Friend;
import org.virtue.game.logic.social.internal.Ignore;
import org.virtue.game.logic.social.messages.ClanChannelDeltaPacket;
import org.virtue.game.logic.social.messages.ClanChannelMessage;
import org.virtue.game.logic.social.messages.ClanChannelPacket;
import org.virtue.game.logic.social.messages.FriendsChatMessage;
import org.virtue.game.logic.social.messages.FriendsChatPacket;
import org.virtue.game.logic.social.messages.PrivateMessage;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;

/**
 * An abstraction interface between the social module and the {@link Player} object. 
 * For external social modules, this will contain the actual player data. For internal modules, it links the the player data
 *
 * @author Sundays211
 */
public interface SocialUser {
	
	/**
	 * Gets the current display name for the player
	 * @return	The display name
	 */
	public String getDisplayName ();
	
	/**
	 * Gets the protocol name for the player. Used as the unique ID for representing the player
	 * @return	The protocol name
	 */
	public String getProtocolName ();
	
	public boolean isInWorld ();
	public String getWorldName ();
	public int getWorldID ();
	//TODO: Move all the world details into a single class
	
	public Rank getRights ();
	
	/**
	 * Sends a system message of the specified type to the player
	 * @param message	The message to send
	 * @param type		The type of message
	 */
	public void sendSystemMessage (String message, MessageOpcode type);
	
	//=================================Friends/Ignores section=================================//
	
	public void sendPrivateMessage (PrivateMessage message);
	
	public void sendFriendsList (Friend[] friends);
	
	public void sendIgnoreList(Ignore[] ignores);
	
	/**
	 * Sends an update to an entry on the player's friends list
	 * @param ignore		The update
	 * @param isNameChange	Whether the update represents a display name change
	 */
	public void sendFriendUpdate (Friend friend, boolean isNameChange);
	
	/**
	 * Sends an update to an entry on the player's ignore list
	 * @param ignore		The update
	 * @param isNameChange	Whether the update represents a display name change
	 */
	public void sendIgnoreUpdate (Ignore ignore, boolean isNameChange);
	
	//=================================Friends chat section=================================//
	
	/**
	 * Gets the owner of the friends chat channel the player is currently in
	 * @return	The owner name, or null if the player isn't in a friends chat
	 */
	public String getCurrentChannelOwner ();
	
	/**
	 * Sets the owner of the friends chat channel the player is currently in
	 * @param ownerName		The owner name, or null if the player isn't in a friends chat
	 */
	public void setCurrentChannelOwner (String ownerName);
	
	/**
	 * Sends a {@link FriendsChatPacket} to the player
	 * @param packet	The packet to send
	 */
	public void sendFriendsChatPacket (FriendsChatPacket packet);
	
	/**
	 * Sends data informing the player that they have left the current friends chat. Also sets the current channel owner to null
	 */
	public void sendLeaveFriendsChat ();
	
	/**
	 * Sends a friends chat channel message to the player
	 * @param message	The message to send
	 */
	public void sendFriendsChatMessage (FriendsChatMessage message);
	
	/**
	 * Sends an update to a permission in the player's friends chat
	 * @param permission	The permission to update
	 * @param rank			The new minimum rank
	 */
	public void sendPermissionUpdate (ChannelPermission permission, ChannelRank rank);
	
	/**
	 * Sends the prefix for the player's friends chat
	 * @param prefix	The friends chat prefix
	 */
	public void sendChannelPrefix (String prefix);
	
	//=================================Clan channel section=================================//
	
	/**
	 * Returns the hash for the clan the player belongs to
	 * @return	The clan hash, or 0L if the player isn't in any clan
	 */
	public long getMyClanHash ();
	
	/**
	 * Sets the hash for the clan the player belongs to
	 * @param myClanHash	The hash to set to, or 0L if the player isn't in any clan
	 */
	public void setMyClanHash (long myClanHash);
	
	/**
	 * Returns the hash for the clan the player is currently a guest in
	 * @return	The clan hash, or 0L if the player isn't a guest in any clan
	 */
	public long getGuestClanHash ();
	
	/**
	 * Sets the hash for the clan the player is a guest in
	 * @param myClanHash	The hash to set to, or 0L if the player isn't a guest in any clan
	 */
	public void setGuestClanHash (long clanHash);
	
	/**
	 * Returns whether the player is currently in their clan's chat channel
	 * @return	True if the player is in the channel, false otherwise
	 */
	public boolean inClanChannel ();
	
	/**
	 * Sets whether the player is currently in their clan's chat channel
	 * @param inClanChannel		True if the player is in the channel, false otherwise
	 */
	public void setInClanChannel (boolean inClanChannel);
	
	/**
	 * Returns whether the clan with the specified hash is the clan the player belongs to
	 * @param hash	The clan hash to check
	 * @return		True if the clan is the player's clan, false otherwise
	 */
	public boolean isMyClan (long hash);
	
	/**
	 * Sends a packet representing recent changes within a clan chat channel
	 * @param packet	The packet to send
	 */	
	public void sendClanChannelDelta (ClanChannelDeltaPacket packet);
	
	/**
	 * Sends a full packet representing a clan chat channel
	 * @param packet	The packet to send
	 */
	public void sendClanChannelFull (ClanChannelPacket packet);
	
	/**
	 * Sends a clan chat channel message to the player
	 * @param message	The message to send
	 */
	public void sendClanChatMessage (ClanChannelMessage message);
	
	public void sendLeaveClanChannel (boolean isGuest);
	
}
