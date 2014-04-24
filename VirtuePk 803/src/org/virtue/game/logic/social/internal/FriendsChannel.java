package org.virtue.game.logic.social.internal;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

import org.virtue.game.logic.social.ChannelRank;
import org.virtue.game.logic.social.internal.InternalFriendManager.FcPermission;
import org.virtue.game.logic.social.messages.FriendsChatPacket;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class FriendsChannel {
	
	private String ownerName;
	
	private String channelName;
	
	private final EnumMap<FcPermission, ChannelRank> requirements = new EnumMap<FcPermission, ChannelRank>(InternalFriendManager.FcPermission.class);
	
	/*private ChannelRank kickRequirement = ChannelRank.OWNER;
	
	private ChannelRank talkRequirement = ChannelRank.FRIEND;
	
	private ChannelRank joinRequirement = ChannelRank.GUEST;*/
	
	private ArrayList<String> bans = new ArrayList<String>();
	
	private HashMap<String, ChannelRank> ranks = new HashMap<String, ChannelRank>();
	
	private HashMap<String, SocialUser> users = new HashMap<String, SocialUser>();
	
	public FriendsChannel(String ownerName, String channelName) {
		this.ownerName = ownerName;
		this.channelName = channelName;		
		requirements.put(FcPermission.JOIN, ChannelRank.GUEST);
		requirements.put(FcPermission.TALK, ChannelRank.FRIEND);
		requirements.put(FcPermission.KICK, ChannelRank.OWNER);
	}
	
	public String getName () {
		return channelName;
	}
	
	public String getOwner () {
		return ownerName;
	}
	
	public ChannelRank getKickReq () {
		return requirements.get(FcPermission.KICK);
	}
	
	public ChannelRank getJoinReq () {
		return requirements.get(FcPermission.JOIN);
	}
	
	/**
	 * Returns whether or not the specified rank can join the channel
	 * @param rank	The rank to check
	 * @return		True if the rank can join, false otherwise
	 */
	public boolean canJoin (ChannelRank rank) {
		return getJoinReq().getID() <= rank.getID();
	}
	
	public ChannelRank getTalkReq () {
		return requirements.get(FcPermission.TALK);
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
		boolean needsFullRefresh = false;
		bans = data.getChannelBans();
		if (!ownerName.equals(data.getDisplayName())) {
			ownerName = data.getDisplayName();
			needsFullRefresh = true;
		}
		for (FcPermission req : requirements.keySet()) {
			if (!requirements.get(req).equals(data.getPermission(req))) {
				requirements.put(req, data.getPermission(req));
				needsFullRefresh = true;
			}
		}
		HashMap<String, ChannelRank> newRanks = data.getChannelRanks();
		synchronized (users) {
			for (SocialUser u : users.values()) {
				String name = u.getProtocolName();
				ChannelRank newRank = newRanks.get(name) == null ? (u.getProtocolName().equalsIgnoreCase(ownerName) ? ChannelRank.OWNER : ChannelRank.GUEST) : newRanks.get(name);
				if (!getPlayerRank(name).equals(newRank)) {
					needsFullRefresh = true;//Much easier just to do a full refresh if one or more users have been updated
					if(!canJoin(newRank)) {
						leave(u);
						u.sendGameMessage("You have been removed from this channel.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
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
	
	public boolean isBanned (String protocolName) {
		return bans.contains(protocolName);
	}
	
	public void join (SocialUser player) {
		synchronized (users) {
			if (!users.containsKey(player.getProtocolName())) {
				FriendsChatPacket.User packet = makeUpdatePacket(player);
				sendPacket(new FriendsChatPacket(packet));
				users.put(player.getProtocolName(), player);
			}
		}
		player.sendFriendsChatPacket(makeFullPacket());
	}
	
	/**
	 * Removes the player from the friends chat channel
	 * @param player	The player to remove
	 * @return			True if the player was the last user in the channel (and therefore is empty), false otherwise
	 */
	public boolean leave (SocialUser player) {
		String name = player.getProtocolName();
		synchronized (users) {
			if (users.containsKey(name)) {
				String displayName = users.get(name).getDisplayName();
				users.remove(name);
				FriendsChatPacket.User packet = new FriendsChatPacket.User(displayName, null, null, player.getWorldID(), null);
				sendPacket(new FriendsChatPacket(packet));
			}
		}
		player.sendLeaveFriendsChat();
		return users.isEmpty();
	}
	
	private void sendPacket (FriendsChatPacket packet) {		
		for (SocialUser u : users.values()) {
			if (u == null) {
				continue;
			}
			u.sendFriendsChatPacket(packet);
		}
	}
	
	private FriendsChatPacket.User makeUpdatePacket (SocialUser player) {
		return new FriendsChatPacket.User(player.getDisplayName(), player.getDisplayName(), 
				getPlayerRank(player.getProtocolName()), player.getWorldID(), player.getWorldName());
	}
	
	private FriendsChatPacket makeFullPacket () {
		FriendsChatPacket.User[] currentUsers;
		synchronized (users) {
			 currentUsers = new FriendsChatPacket.User[users.size()];
			 int i = 0;
			 for (SocialUser user : users.values()) {
				 FriendsChatPacket.User u = new FriendsChatPacket.User(user.getDisplayName(), user.getDisplayName(), 
						 getPlayerRank(user.getProtocolName()), user.getWorldID(), user.getWorldName());
				 currentUsers[i] = u;
				 i++;
			 }
		}
		return new FriendsChatPacket(currentUsers, ownerName, channelName, getKickReq());
	}
	
}
