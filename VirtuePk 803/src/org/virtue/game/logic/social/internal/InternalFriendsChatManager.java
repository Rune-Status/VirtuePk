package org.virtue.game.logic.social.internal;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

import org.virtue.Launcher;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.social.FriendsChatManager;
import org.virtue.network.io.IOHub;
import org.virtue.network.protocol.messages.GameMessage;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;
import org.virtue.utility.StringUtils;
import org.virtue.utility.StringUtils.FormatType;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class InternalFriendsChatManager implements FriendsChatManager {
	
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
								owner.resetFcUpdateFlag();
							}
						}
					}
				}
			}			
		}, 60000, 60000);
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
			if (owner.getChannelName() == null) {
				return;
			}
			FriendsChannel channel = new FriendsChannel(owner.getDisplayName(), owner.getChannelName());
			channel.refreshSettings(owner);
			friendsChannelCache.put(ownerName, channel);
		} else if (IOHub.getFriendsIO().exists(ownerName)) {
			InternalFriendManager owner;
			try {
				owner = IOHub.getFriendsIO().load(ownerName, new SocialUser(null, ownerName));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return;
			}
			if (owner.getChannelName() == null) {
				return;
			}
			FriendsChannel channel = new FriendsChannel(StringUtils.format(ownerName, FormatType.NAME), owner.getChannelName());
			channel.refreshSettings(owner);
			friendsChannelCache.put(ownerName, channel);			
		}
	}

	@Override
	public void joinChannel(Player player, String owner) {
		String protocolOwner = StringUtils.format(owner, FormatType.PROTOCOL);
		if (!friendsChannelCache.containsKey(protocolOwner)) {
			loadChannel(protocolOwner);
		}
		FriendsChannel channel = friendsChannelCache.get(protocolOwner);
		if (channel == null) {
			player.getPacketDispatcher().dispatchMessage("The channel you tried to join does not exist.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
			player.getChatManager().setCurrentChannelOwner(null);
			return;
		}
		String protocolPlayer = player.getAccount().getUsername().getAccountNameAsProtocol();
		if (channel.isBanned(protocolPlayer)) {
			player.getPacketDispatcher().dispatchMessage("You are not allowed to join this user's friends chat channel.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
			player.getChatManager().setCurrentChannelOwner(null);
			return;
		}
		if (!channel.canJoin(channel.getPlayerRank(protocolPlayer))) {
			player.getPacketDispatcher().dispatchMessage("You do not have a high enough rank to join this friends chat channel.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
			player.getChatManager().setCurrentChannelOwner(null);
			return;
		}//TODO: Check for temporary bans and full channels
		channel.join(new SocialUser(player, protocolPlayer));
		player.getChatManager().setCurrentChannelOwner(channel.getOwner());
		player.getPacketDispatcher().dispatchMessage("Now talking in friends chat channel "+channel.getName(), MessageOpcode.FRIENDS_CHAT_SYSTEM);
	}

	@Override
	public void leaveChannel(Player player) {		
		String owner = StringUtils.format(player.getChatManager().getCurrentChannelOwner(), FormatType.PROTOCOL);
		if (owner == null) {
			player.getPacketDispatcher().dispatchMessage("You are not currently in a channel.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
			player.getChatManager().setCurrentChannelOwner(null);
			return;
		}
		if (friendsChannelCache.containsKey(owner)) {
			if (friendsChannelCache.get(owner).leave(new SocialUser(player))) {
				friendsChannelCache.remove(owner);
			}
		}		
		player.getChatManager().setCurrentChannelOwner(null);
		player.getPacketDispatcher().dispatchMessage("You have left the channel.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
	}
	
}
