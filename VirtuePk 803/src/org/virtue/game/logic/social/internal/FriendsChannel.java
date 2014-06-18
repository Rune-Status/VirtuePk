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
package org.virtue.game.logic.social.internal;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

import org.virtue.game.logic.social.ChannelPermission;
import org.virtue.game.logic.social.ChannelRank;
import org.virtue.game.logic.social.SocialUserAPI;
import org.virtue.game.logic.social.messages.FriendsChatMessage;
import org.virtue.game.logic.social.messages.FriendsChatPacket;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;
import org.virtue.utility.GameClock;

/**
 * Represents a friends chat channel
 *
 * @author Sundays211
 */
public class FriendsChannel {
	
	private String ownerName;
	
	private String channelName;
	
	private final EnumMap<ChannelPermission, ChannelRank> requirements = new EnumMap<ChannelPermission, ChannelRank>(ChannelPermission.class);
	
	private ArrayList<String> bans = new ArrayList<String>();
	
	private HashMap<String, ChannelRank> ranks = new HashMap<String, ChannelRank>();
	
	private HashMap<String, SocialUserAPI> users = new HashMap<String, SocialUserAPI>();
	
	private HashMap<String, Long> tempBans = new HashMap<String, Long>();
	
	public FriendsChannel(String ownerName, String channelName) {
		this.ownerName = ownerName;
		this.channelName = channelName;		
		requirements.put(ChannelPermission.JOIN, ChannelRank.GUEST);
		requirements.put(ChannelPermission.TALK, ChannelRank.FRIEND);
		requirements.put(ChannelPermission.KICK, ChannelRank.OWNER);
	}
	
	public String getName () {
		return channelName;
	}
	
	public String getOwner () {
		return ownerName;
	}
	
	/**
	 * Returns the minimum rank needed to kick other users from the channel
	 * @return	The minimum kick rank
	 */
	public ChannelRank getKickReq () {
		return requirements.get(ChannelPermission.KICK);
	}
	
	/**
	 * Returns whether or not the specified rank can kick other users from the channel
	 * @param rank	The rank to check
	 * @return		True if the rank can kick, false otherwise
	 */
	public boolean canKick (ChannelRank rank) {
		return getKickReq().getID() <= rank.getID();
	}
	
	/**
	 * Returns the minimum rank needed to join the channel
	 * @return	The minimum join rank
	 */
	public ChannelRank getJoinReq () {
		return requirements.get(ChannelPermission.JOIN);
	}
	
	/**
	 * Returns whether or not the specified rank can join the channel
	 * @param rank	The rank to check
	 * @return		True if the rank can join, false otherwise
	 */
	public boolean canJoin (ChannelRank rank) {
		return getJoinReq().getID() <= rank.getID();
	}
	
	/**
	 * Returns the minimum rank needed to talk in the channel
	 * @return	The minimum talk rank
	 */
	public ChannelRank getTalkReq () {
		return requirements.get(ChannelPermission.TALK);
	}
	
	/**
	 * Returns whether or not the specified rank can talk in the channel
	 * @param rank	The rank to check
	 * @return		True if the rank can talk in the channel, false otherwise
	 */
	public boolean canTalk (ChannelRank rank) {
		return getTalkReq().getID() <= rank.getID();
	}
	
	public ChannelRank getPlayerRank (String name) {
		ChannelRank response = null;
		if (name.equalsIgnoreCase(ownerName)) {
			response = ChannelRank.OWNER;
		}
		if (ranks.containsKey(name)) {
			response = ranks.get(name);
		}
		return (response == null ? ChannelRank.GUEST : response);
	}
	
	public void refreshSettings (InternalFriendManager data) {
		data.resetFcUpdateFlag();
		if (!data.hasFriendsChat()) {
			synchronized (users) {
				for (SocialUserAPI u : users.values()) {
					u.sendSystemMessage("You have been removed from this channel.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
					u.sendLeaveFriendsChat();
				}
			}
			return;
		}
		boolean needsFullRefresh = false;
		bans = data.getChannelBans();
		if (!ownerName.equals(data.getDisplayName())) {
			ownerName = data.getDisplayName();
			needsFullRefresh = true;
		}
		if (!channelName.equals(data.getChannelName())) {
			channelName = data.getChannelName();
			needsFullRefresh = true;
		}
		for (ChannelPermission req : requirements.keySet()) {
			if (!requirements.get(req).equals(data.getPermission(req))) {
				requirements.put(req, data.getPermission(req));
				needsFullRefresh = true;
			}
		}
		HashMap<String, ChannelRank> newRanks = data.getChannelRanks();
		synchronized (users) {
			for (SocialUserAPI u : users.values()) {
				String name = u.getProtocolName();
				ChannelRank newRank = newRanks.get(name) == null ? (u.getProtocolName().equalsIgnoreCase(ownerName) ? ChannelRank.OWNER : ChannelRank.GUEST) : newRanks.get(name);
				if (!getPlayerRank(name).equals(newRank)) {
					needsFullRefresh = true;//Much easier just to do a full refresh if one or more users have been updated
					if(!canJoin(newRank)) {
						leave(u);
						u.sendSystemMessage("You have been removed from this channel.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
					}
					//sendPacket(new FriendsChatPacket(makeUpdatePacket(u)));
				}
			}
		}
		//needsFullRefresh = true;
		ranks = newRanks;
		if (needsFullRefresh) {
			synchronized (users) {
				FriendsChatPacket packet = makeFullPacket();
				sendPacket(packet);
			}
		}
	}
	
	/**
	 * Returns whether the specified user is permanently banned from the channel
	 * @param protocolName	The username of the user to check
	 * @return				True if the user is banned, false otherwise
	 */
	public boolean isBanned (String protocolName) {
		return bans.contains(protocolName);
	}
	
	/**
	 * Returns whether the specified user is temporarily banned from the channel. Removes their temporary ban if it has expired.
	 * @param protocolName	The username of the user to check
	 * @return				True if the user is temporarily bannend, false otherwise
	 */
	public boolean isTempBanned (String protocolName) {
		if (!tempBans.containsKey(protocolName)) {
			return false;
		} else {
			long banExpires = tempBans.get(protocolName);
			if (banExpires < System.currentTimeMillis()) {
				tempBans.remove(protocolName);
				return false;
			}
			return true;
		}
	}
	
	/**
	 * Returns the number of users currently in the channel
	 * @return	The number of users in the channel
	 */
	public int getSize () {
		return users.size();
	}
	
	public boolean join (SocialUserAPI player) {
		synchronized (users) {
			if (!users.containsKey(player.getProtocolName())) {
				if (users.size() >= 100) {
					if (!tryBump(getPlayerRank(player.getProtocolName()))) {
						return false;
					}
				}
				FriendsChatPacket.User packet = makeUpdatePacket(player);
				sendPacket(new FriendsChatPacket(packet));
				users.put(player.getProtocolName(), player);
			}
		}
		player.sendFriendsChatPacket(makeFullPacket());
		return true;
	}
	
	/**
	 * Attempts to bump a lower-ranking user from the channel, in order to allow a higher-ranking one to join a full channel
	 * @param playerRank	The rank of the player wishing to join
	 * @return				True if a user was successfully bumped, false otherwise
	 */
	private boolean tryBump (ChannelRank playerRank) {
		for (ChannelRank rank : ChannelRank.values()) {
			if (playerRank.equals(rank)) {
				return false;
			}
			for (String name : users.keySet()) {
				if (getPlayerRank(name).equals(rank)) {
					removeUser(users.get(name));
					users.get(name).sendLeaveFriendsChat();
					users.get(name).sendSystemMessage("You have been removed from this channel.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Removes the player from the friends chat channel
	 * @param player	The player to remove
	 * @return			True if the player was the last user in the channel (and therefore is empty), false otherwise
	 */
	public boolean leave (SocialUserAPI player) {
		String name = player.getProtocolName();
		synchronized (users) {
			if (users.containsKey(name)) {
				removeUser(users.get(name));
			}
		}
		player.sendLeaveFriendsChat();
		return users.isEmpty();
	}
	
	/**
	 * Removes a user from the friends chat channel. This method assumes the user is actually in the channel, and does not send the leave packet to the player.
	 * Should only be used in conjunction with a method where "users" is synchronised and the player is confirmed to be in the channel.
	 * @param user	The user to remove
	 */
	private void removeUser (SocialUserAPI user) {
		String displayName = users.get(user.getProtocolName()).getDisplayName();
		users.remove(user.getProtocolName());
		FriendsChatPacket.User packet = new FriendsChatPacket.User(displayName, null, null, user.getWorldID(), null);
		sendPacket(new FriendsChatPacket(packet));
	}
	
	public void kickBanUser (String name) {
		synchronized (users) {
			if (users.containsKey(name)) {
				SocialUserAPI u = users.get(name);
				removeUser(u);
				u.sendSystemMessage("You have been kicked from the channel.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
				u.sendLeaveFriendsChat();
			}
		}
		long banExpires = System.currentTimeMillis() + GameClock.ONE_HOUR*GameClock.ONE_TICK;
		tempBans.put(name, banExpires);
	}
	
	public void sendMessage(FriendsChatMessage message) {
		synchronized (users) {
			for (SocialUserAPI u : users.values()) {
				if (u == null) {
					continue;
				}
				u.sendFriendsChatMessage(message);
			}
		}
	}
	
	/**
	 * Sends the specified friends chat update packet to all users currently in the channel
	 * @param packet	The packet to send
	 */
	private void sendPacket (FriendsChatPacket packet) {		
		for (SocialUserAPI u : users.values()) {
			if (u == null) {
				continue;
			}
			u.sendFriendsChatPacket(packet);
		}
	}
	
	/**
	 * Creates a {@link FriendsChatPacket.User} packet representing an update to the specified user
	 * @param player	The player to include in the update
	 * @return			A packet representing the update
	 */
	private FriendsChatPacket.User makeUpdatePacket (SocialUserAPI player) {
		return new FriendsChatPacket.User(player.getDisplayName(), player.getDisplayName(), 
				getPlayerRank(player.getProtocolName()), player.getWorldID(), player.getWorldName());
	}
	
	/**
	 * Creates a full {@link FriendsChatPacket} containing all the information about the friends chat channel
	 * @return	A full FriendsChatPacket
	 */
	private FriendsChatPacket makeFullPacket () {
		FriendsChatPacket.User[] currentUsers;
		synchronized (users) {
			 currentUsers = new FriendsChatPacket.User[users.size()];
			 int i = 0;
			 for (SocialUserAPI user : users.values()) {
				 FriendsChatPacket.User u = new FriendsChatPacket.User(user.getDisplayName(), user.getDisplayName(), 
						 getPlayerRank(user.getProtocolName()), user.getWorldID(), user.getWorldName());
				 currentUsers[i] = u;
				 i++;
			 }
		}
		return new FriendsChatPacket(currentUsers, ownerName, channelName, getKickReq());
	}
	
}
