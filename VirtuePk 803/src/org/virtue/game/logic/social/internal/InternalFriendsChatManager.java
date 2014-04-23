package org.virtue.game.logic.social.internal;

import java.util.ArrayList;
import java.util.HashMap;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.social.FriendsChatManager;
import org.virtue.utility.StringUtils;
import org.virtue.utility.StringUtils.FormatType;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class InternalFriendsChatManager implements FriendsChatManager {
	
	private final HashMap<String, FriendsChannel> friendsChannelCache = new HashMap<String, FriendsChannel>();
	
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
		} else {
			InternalFriendManager owner = new InternalFriendManager(null);//TODO: Replace this with a method which loads the player data
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
		if (channel == null) {//TODO: Figure out the opcode for friends chat system messages
			player.getPacketDispatcher().dispatchMessage("The channel you tried to join does not exist.");
			return;
		}
		String protocolPlayer = player.getAccount().getUsername().getAccountNameAsProtocol();
		if (channel.getPlayerRank(protocolPlayer).getID() < channel.getJoinReq().getID()) {
			player.getPacketDispatcher().dispatchMessage("You do not have a high enough rank to join this friends chat channel.");
			return;
		}
		channel.join(new SocialUser(player));
	}

	@Override
	public void leaveChannel(Player player) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
}
