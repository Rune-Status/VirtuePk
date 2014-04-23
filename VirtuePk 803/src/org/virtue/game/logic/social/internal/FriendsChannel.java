package org.virtue.game.logic.social.internal;

import org.virtue.game.logic.social.ChannelRank;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.social.internal.InternalFriendManager.FcPermission;
import org.virtue.game.logic.social.messages.FriendsChatPacket;

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
	
	public ChannelRank getTalkReq () {
		return requirements.get(FcPermission.TALK);
	}
	
	public ChannelRank getPlayerRank (String name) {
		if (name.equalsIgnoreCase(ownerName)) {
			return ChannelRank.OWNER;
		}
		if (ranks.containsKey(name)) {
			return ranks.get(name);
		}
		return ChannelRank.GUEST;
	}
	
	public void refreshSettings (InternalFriendManager data) {
		boolean needsFullRefresh = false;
		ranks = data.getChannelRanks();
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
	}
	
	public boolean isBanned (String protocolName) {
		return bans.contains(protocolName);
	}
	
	public void join (SocialUser player) {
		synchronized (users) {
			users.put(player.getProtocolName(), player);
		}
		
	}
	
	private FriendsChatPacket makeFullPacket () {
		FriendsChatPacket.User[] currentUsers = null;
		synchronized (users) {
			 currentUsers = new FriendsChatPacket.User[users.size()];
			 for (SocialUser user : users.values()) {
				 
			 }
		}
		return new FriendsChatPacket(currentUsers, ownerName, channelName, getKickReq());
	}
	
}
