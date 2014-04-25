package org.virtue.game.logic.social.internal;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

import org.virtue.game.logic.social.ChannelRank;
import org.virtue.game.logic.social.internal.InternalFriendManager.FcPermission;
import org.virtue.game.logic.social.messages.FriendsChatMessage;
import org.virtue.game.logic.social.messages.FriendsChatPacket;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;
import org.virtue.utility.GameClock;

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
	
	private HashMap<String, Long> tempBans = new HashMap<String, Long>();
	
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
	
	public boolean canKick (ChannelRank rank) {
		return getKickReq().getID() <= rank.getID();
	}
	
	/**
	 * Returns the minimum rank needed to join the channel
	 * @return	The minimum join rank
	 */
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
	
	/**
	 * Returns the minimum rank needed to talk in the channel
	 * @return	The minimum talk rank
	 */
	public ChannelRank getTalkReq () {
		return requirements.get(FcPermission.TALK);
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
	
	public boolean join (SocialUser player) {
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
	 * @return				True if a user was successfully bumpped, false otherwise
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
					users.get(name).sendGameMessage("You have been removed from this channel.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
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
	public boolean leave (SocialUser player) {
		String name = player.getProtocolName();
		synchronized (users) {
			if (users.containsKey(name)) {
				removeUser(users.get(name));
			}
		}
		player.sendLeaveFriendsChat();
		return users.isEmpty();
	}
	
	private void removeUser (SocialUser user) {
		String displayName = users.get(user.getProtocolName()).getDisplayName();
		users.remove(user.getProtocolName());
		FriendsChatPacket.User packet = new FriendsChatPacket.User(displayName, null, null, user.getWorldID(), null);
		sendPacket(new FriendsChatPacket(packet));
	}
	
	public void kickBanUser (String name) {
		synchronized (users) {
			if (users.containsKey(name)) {
				SocialUser u = users.get(name);
				removeUser(u);
				u.sendGameMessage("You have been kicked from the channel.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
				u.sendLeaveFriendsChat();
			}
		}
		long banExpires = System.currentTimeMillis() + GameClock.ONE_HOUR*GameClock.ONE_TICK;
		tempBans.put(name, banExpires);
	}
	
	public void sendMessage(FriendsChatMessage message) {
		synchronized (users) {
			for (SocialUser u : users.values()) {
				if (u == null) {
					continue;
				}
				u.sendFriendsChatMessage(message);
			}
		}
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
