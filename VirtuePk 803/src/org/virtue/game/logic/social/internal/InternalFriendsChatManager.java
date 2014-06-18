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

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Queue;

import org.virtue.Launcher;
import org.virtue.game.logic.social.FriendsChatAPI;
import org.virtue.game.logic.social.SocialUserAPI;
import org.virtue.game.logic.social.messages.FriendsChatMessage;
import org.virtue.network.io.IOHub;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;
import org.virtue.utility.GameClock;
import org.virtue.utility.StringUtils;
import org.virtue.utility.StringUtils.FormatType;

/**
 * Contains the tools necessary for managing friends chat channels. 
 * Only one instance of this class should be created, and all players should interact with that instance.
 *
 * @author Sundays211
 */
public class InternalFriendsChatManager implements FriendsChatAPI {
	
	private final HashMap<String, FriendsChannel> friendsChannelCache = new HashMap<String, FriendsChannel>();
	
	public InternalFriendsChatManager () {
		Launcher.getEngine().registerLogicEvent(new Runnable() {
			@Override
			public void run() {
				synchronized (friendsChannelCache) {
					for (String channelOwner : friendsChannelCache.keySet()) {
						if (InternalFriendManager.isOnline(channelOwner)) {
							InternalFriendManager owner = InternalFriendManager.getPlayer(channelOwner);
							if (owner.fcUpdateFlagged()) {
								friendsChannelCache.get(channelOwner).refreshSettings(owner);
								if (!owner.hasFriendsChat()) {
									friendsChannelCache.remove(owner.getProtocolName());
								}
							}
						}
					}
				}
			}			
		}, GameClock.ONE_MINUTE * GameClock.ONE_TICK, GameClock.ONE_MINUTE * GameClock.ONE_TICK);
		Launcher.getEngine().registerLogicEvent(new Runnable() {
			@Override
			public void run() {
				synchronized (InternalFriendManager.immediateChangeQueue) {
					Queue<InternalFriendManager> queue = InternalFriendManager.immediateChangeQueue;
					if (queue.isEmpty()) {
						return;
					}
					for (InternalFriendManager change = queue.poll(); !queue.isEmpty(); change = queue.poll()) {						
						if (friendsChannelCache.containsKey(change.getProtocolName())) {
							friendsChannelCache.get(change.getProtocolName()).refreshSettings(change);
							if (!change.hasFriendsChat()) {
								friendsChannelCache.remove(change.getProtocolName());
							}							
						}
					}
				}
			}	
		}, 600, 600);
	}
	
	private void loadChannel (String ownerName) {
		if (friendsChannelCache.containsKey(ownerName)) {
			return;
		}
		if (InternalFriendManager.isOnline(ownerName)) {
			InternalFriendManager owner = InternalFriendManager.getPlayer(ownerName);
			if (!owner.hasFriendsChat()) {
				return;
			}
			FriendsChannel channel = new FriendsChannel(owner.getDisplayName(), owner.getChannelName());
			channel.refreshSettings(owner);
			friendsChannelCache.put(ownerName, channel);
		} else if (IOHub.getFriendsIO().exists(ownerName)) {
			InternalFriendManager owner;
			try {
				owner = IOHub.getFriendsIO().load(ownerName, new InternalSocialUser(null, ownerName));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return;
			}
			if (!owner.hasFriendsChat()) {
				return;
			}
			FriendsChannel channel = new FriendsChannel(StringUtils.format(ownerName, FormatType.NAME), owner.getChannelName());
			channel.refreshSettings(owner);
			friendsChannelCache.put(ownerName, channel);			
		}
	}

	@Override
	public void joinChannel(SocialUserAPI player, String owner) {
		String protocolOwner = StringUtils.format(owner, FormatType.PROTOCOL);
		if (!friendsChannelCache.containsKey(protocolOwner)) {
			loadChannel(protocolOwner);
		}
		FriendsChannel channel = friendsChannelCache.get(protocolOwner);
		if (channel == null) {
			player.sendSystemMessage("The channel you tried to join does not exist.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
			player.setCurrentChannelOwner(null);
			return;
		}
		if (channel.isBanned(player.getProtocolName())) {
			player.sendSystemMessage("You are not allowed to join this user's friends chat channel.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
			player.setCurrentChannelOwner(null);
			return;
		}
		if (!channel.canJoin(channel.getPlayerRank(player.getProtocolName()))) {
			player.sendSystemMessage("You do not have a high enough rank to join this friends chat channel.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
			player.setCurrentChannelOwner(null);
			return;
		}
		if (channel.isTempBanned(player.getProtocolName())) {
			player.sendSystemMessage("You are temporarily banned from this friends chat channel.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
			player.setCurrentChannelOwner(null);
			return;
		}
		if (channel.join(player)) {
			player.setCurrentChannelOwner(channel.getOwner());
			player.sendSystemMessage("Now talking in friends chat channel "+channel.getName(), MessageOpcode.FRIENDS_CHAT_SYSTEM);
		} else {
			player.setCurrentChannelOwner(null);
			player.sendSystemMessage("The channel you tried to join is currently full.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
		}
	}

	@Override
	public void leaveChannel(SocialUserAPI player, boolean isLoggedOut) {		
		String owner = StringUtils.format(player.getCurrentChannelOwner(), FormatType.PROTOCOL);
		if (owner == null) {
			if (isLoggedOut) {
				return;
			}
			player.sendSystemMessage("You are not currently in a channel.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
			player.setCurrentChannelOwner(null);
			return;
		}
		if (friendsChannelCache.containsKey(owner)) {
			if (friendsChannelCache.get(owner).leave(player)) {
				friendsChannelCache.remove(owner);
			}
		}		
		if (isLoggedOut) {
			return;
		}
		player.setCurrentChannelOwner(null);
		player.sendSystemMessage("You have left the channel.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
	}

	@Override
	public void sendMessage(SocialUserAPI user, String message) {
		//SocialUser user = new SocialUser(player);
		String owner = StringUtils.format(user.getCurrentChannelOwner(), FormatType.PROTOCOL);
		if (owner == null || !friendsChannelCache.containsKey(owner)) {
			user.sendSystemMessage("You are not currently in a channel.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
			user.sendLeaveFriendsChat();
			user.setCurrentChannelOwner(null);
			return;
		}
		FriendsChannel channel = friendsChannelCache.get(owner);
		if (!channel.canTalk(channel.getPlayerRank(user.getProtocolName()))) {
			user.sendSystemMessage("You are not allowed to talk in this friends chat channel.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
			return;
		}
		FriendsChatMessage messageObject = new FriendsChatMessage(message, user.getDisplayName(), 
				user.getDisplayName(), user.getRights(), channel.getName());
		channel.sendMessage(messageObject);
	}

	@Override
	public void kickBanUser(SocialUserAPI user, String username) {
		//SocialUser user = new SocialUser(player);
		String owner = StringUtils.format(user.getCurrentChannelOwner(), FormatType.PROTOCOL);
		if (owner == null || !friendsChannelCache.containsKey(owner)) {
			user.sendSystemMessage("You are not currently in a channel.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
			user.sendLeaveFriendsChat();
			user.setCurrentChannelOwner(null);
			return;
		}
		FriendsChannel channel = friendsChannelCache.get(owner);
		String protocolBanName = StringUtils.format(username, FormatType.PROTOCOL);
		if (!channel.canKick(channel.getPlayerRank(user.getProtocolName()))) {
			user.sendSystemMessage("You do not have permission to kick users in this channel.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
			return;
		}
		if (user.getProtocolName().equalsIgnoreCase(protocolBanName)) {
			return;//Cannot kick self
		}
		if (channel.getPlayerRank(user.getProtocolName()).getID() <= channel.getPlayerRank(protocolBanName).getID()) {
			user.sendSystemMessage("You do not have permission to kick this user.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
			return;
		}
		if (channel.isTempBanned(protocolBanName)) {
			channel.kickBanUser(protocolBanName);
			user.sendSystemMessage("Your request to refresh this user's temporary ban was successful.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
		} else {
			channel.kickBanUser(protocolBanName);
			user.sendSystemMessage("Your request to kick/ban this user was successful.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
		}
	}
	
}
