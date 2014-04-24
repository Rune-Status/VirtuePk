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
package org.virtue.game.logic.social.internal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import org.virtue.game.logic.World;
import org.virtue.game.logic.WorldHub;
import org.virtue.game.logic.social.ChannelRank;
import org.virtue.game.logic.social.FriendManager;
import org.virtue.game.logic.social.OnlineStatus;
import org.virtue.game.logic.social.messages.PrivateMessage;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;
import org.virtue.utility.RS2Utils;
import org.virtue.utility.StringUtils;
import org.virtue.utility.StringUtils.FormatType;

/**
 * The tools necessary to control a player's friends list, ignore list, and private messaging. 
 * Also has the tools for managing the player's own friends chat (though the friends chat channel itself is run separately)
 *
 * @author Sundays211
 *
 */
public class InternalFriendManager implements FriendManager {
	
	private static final ConcurrentHashMap<String, InternalFriendManager> onlinePlayers = new ConcurrentHashMap<String, InternalFriendManager>();
	
	protected static final Queue<InternalFriendManager> immediateChangeQueue = new LinkedList<InternalFriendManager>();
	
	static {
		onlinePlayers.put("test44", new InternalFriendManager(null));
	}
	
	protected static boolean isOnline (String name) {
		return onlinePlayers.containsKey(name);
	}
	
	protected static InternalFriendManager getPlayer (String name) {
		return onlinePlayers.get(name);
	}
	
	private SocialUser player;
	//private NameManager nameManager;
	
	private static final int FRIENDS_LIST_MAX = 400;
	private static final int IGNORE_LIST_MAX = 400;
	
	private static final int FILE_VERSION = 2;
	
	public enum FcPermission {
		JOIN, TALK, KICK
	}
	
	private final EnumMap<FcPermission, ChannelRank> fcPermissions = new EnumMap<FcPermission, ChannelRank>(FcPermission.class);
	
	private String channelName = "Test";//TODO: Replace this with "null" when done
	
	private boolean fcUpdateFlagged = false;
	
	private final HashMap<String, Friend> friends = new HashMap<String, Friend>(FRIENDS_LIST_MAX);
	private final HashMap<String, Ignore> ignores = new HashMap<String, Ignore>(IGNORE_LIST_MAX);
	
	private OnlineStatus onlineStatus = OnlineStatus.EVERYONE;
	
	private WorldHub currentWorld = null;
	
	private boolean isLobby;
	
	public InternalFriendManager (SocialUser player) {
		this.player = player;
		fcPermissions.put(FcPermission.JOIN, ChannelRank.GUEST);
		fcPermissions.put(FcPermission.TALK, ChannelRank.FRIEND);
		fcPermissions.put(FcPermission.KICK, ChannelRank.OWNER);
	}
	
	
	public String getProtocolName () {
		return player.getProtocolName();
	}
	
	public String getDisplayName () {
		return player.getDisplayName();
	}
	
	@Override
	public void init () {
		friends.put("test222", new Friend("test222", false, ChannelRank.FRIEND, "Hi!"));
		friends.put("test_3", new Friend("test_3", true));//TODO: Remove this (it's only for testing purposes)
		ignores.put("test4", new Ignore("test4"));
		ignores.put("test_5", new Ignore("test_5", "This is a note."));
		//player.getActionSender().sendOnlineStatus(onlineStatus);
		//player.getActionSender().sendUnlockFriendsList();
		currentWorld = World.getWorld();//player.getWorld().getData();
		isLobby = !player.getPlayer().isInWorld();
		for (Friend f : friends.values()) {
			//DisplayName nameData = nameManager.getDisplayNamesFromUsername(f.username);
			//if (nameData == null) {
				f.setDisplayNames(StringUtils.format(f.username, FormatType.DISPLAY), "");
			/*} else {
				f.setDisplayNames(nameData.getDisplayName(), nameData.getPrevName());
			}*/
			if (onlinePlayers.containsKey(f.username)) {
				InternalFriendManager friendData = onlinePlayers.get(f.username);
				f.setWorld(friendData.getWorldInfo(this));
				if (f.getWorld() != null && friendData.isLobby) {//TODO: Replace this work-around with proper handling for the lobby
					f.setWorld(1100, "Lobby", 0);
				}
			}
		}
		for (Ignore i : ignores.values()) {
			//DisplayName nameData = nameManager.getDisplayNamesFromUsername(i.username);
			//if (nameData == null) {
				i.setDisplayNames(StringUtils.format(i.username, FormatType.DISPLAY), "");
			/*} else {
				i.setDisplayNames(nameData.getDisplayName(), nameData.getPrevName());
			}*/
		}
		player.sendFriendsList(friends.values().toArray(new Friend[friends.size()]));//.getPlayer().getAccount().getSession().getTransmitter().send(FriendEncoder.class, new FriendsPacket(friends.values().toArray(new Friend[friends.size()])));
		player.sendIgnoreList(ignores.values().toArray(new Ignore[ignores.size()]));//.getPlayer().getAccount().getSession().getTransmitter().send(IgnoreEncoder.class, new IgnoresPacket(ignores.values().toArray(new Ignore[ignores.size()])));
		onlinePlayers.put(player.getProtocolName(), this);
		sendStatusUpdate(this, false);
		System.out.println("Registered "+player.getDisplayName()+" on friend server.");
	}
	
	@Override
	public void shutdown () {
		if (fcUpdateFlagged) {
			synchronized (immediateChangeQueue) {
				immediateChangeQueue.offer(this);
			}
		}
		this.currentWorld = null;
		sendStatusUpdate(this, false);
	}
	
	/**
	 * Queues an update to the player's friends chat channel
	 */
	private void queueChannelUpdate () {
		if (!fcUpdateFlagged) {
			player.sendGameMessage("Changes will take effect on your friends chat in the next 60 seconds.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
		}
		fcUpdateFlagged = true;
	}
	
	public boolean fcUpdateFlagged () {
		return fcUpdateFlagged;
	}
	
	public void resetFcUpdateFlag () {
		fcUpdateFlagged = false;		
	}
	
	protected HashMap<String, ChannelRank> getChannelRanks () {
		HashMap<String, ChannelRank> ranks = new HashMap<String, ChannelRank>(friends.size());
		synchronized (friends) {
			for (Friend f : friends.values()) {
				ranks.put(f.username, f.getFcRank());
			}
		}
		return ranks;
	}
	
	protected ArrayList<String> getChannelBans () {
		ArrayList<String> bans;
		synchronized (ignores) {
			bans = new ArrayList<String>(ignores.keySet());
		}
		return bans;
	}
	
	protected ChannelRank getPermission (FcPermission p) {
		return fcPermissions.get(p);
	}
	
	protected String getChannelName () {
		return channelName;
	}
	
	public void serialise (DataOutputStream output) throws IOException {
		output.write(FILE_VERSION);
		synchronized (this) {
			output.writeShort(friends.size());
			for (Friend f : friends.values()) {
				RS2Utils.writeString(output, f.username);
				output.writeByte(f.isReferred() ? 1 : 0);
				output.writeByte(f.getFcRank().getID());
				RS2Utils.writeString(output, f.getNote());
			}
			output.writeShort(ignores.size());
			for (Ignore i : ignores.values()) {
				RS2Utils.writeString(output, i.username);
				RS2Utils.writeString(output, i.getNote());
			}
		}
	}
	
	public void deserialise (DataInputStream input) throws IOException {
		int version = input.read();
		synchronized (this) {
			if (version >= 2) {
				int friendListSize = input.readUnsignedShort();
				String name, note;
				ChannelRank fcRank;
				boolean isReferred;
				friends.clear();
				for (int i=0;i<friendListSize;i++) {
					name = StringUtils.format(RS2Utils.readString(input), FormatType.PROTOCOL);
					isReferred = (input.readUnsignedByte() == 1);
					fcRank = ChannelRank.forID(input.readByte());
					note = RS2Utils.readString(input);
					if (fcRank == null || fcRank.getID() > ChannelRank.GENERAL.getID() || fcRank.getID() < ChannelRank.FRIEND.getID()) {
						fcRank = ChannelRank.FRIEND;
					}
					if (name.length() == 0) {
						continue;
					}
					Friend f = new Friend(name, isReferred, fcRank, note);
					friends.put(name, f);
				}
				int ignoreListSize = input.readUnsignedShort();
				for (int i=0;i<ignoreListSize;i++) {
					name = StringUtils.format(RS2Utils.readString(input), FormatType.PROTOCOL);
					note = RS2Utils.readString(input);
					if (name.length() == 0) {
						continue;
					}
					Ignore ig = new Ignore(name, note);
					ignores.put(name, ig);
				}
			}
		}
	}
	
	public WorldHub getWorldInfo (InternalFriendManager friend) {
		if (onlineStatus == OnlineStatus.EVERYONE) {
			return currentWorld;
		} else if (onlineStatus == OnlineStatus.FRIENDS 
				&& friends.containsKey(friend.getProtocolName())) {
			return currentWorld;
		} else {
			return null;
		}
	}
	
	/*private WorldHub getFriendWorld (String friendUsername) {
		if (!onlinePlayers.containsKey(StringUtils.format(friendUsername.trim(), FormatType.PROTOCOL))) {
			return null;
		} else {
			InternalFriendManager friendData = onlinePlayers.get(StringUtils.format(friendUsername.trim(), FormatType.PROTOCOL));
			return friendData.getWorldInfo(this);
			/*if (friendData.getOnlineStatus() == OnlineStatus.EVERYONE) {
				return friendData.getWorldInfo();
			} else if (friendData.getOnlineStatus() == OnlineStatus.FRIENDS
					&& friendData.friends.containsKey(NameManager.simplifyName(player.getUsername()))) {
				return friendData.getWorldInfo();
			}*//*
		}
	}*/
	
	private static void sendStatusUpdate (InternalFriendManager p, boolean isNameChange) {
		for (InternalFriendManager f : onlinePlayers.values()) {
			//if (onlineStatus == OnlineStatus.FRIENDS && friends.containsKey(NameManager.simplifyName(f.player.getUsername()))
			f.setFriendStatus(p, isNameChange);
		}
	}
	
	private void setFriendStatus (InternalFriendManager p2, boolean isNameChange) {
		if (friends.containsKey(p2.getProtocolName())) {
			Friend f = friends.get(p2.getProtocolName());
			f.setWorld(p2.getWorldInfo(this));
			if (p2.isLobby) {
				f.setWorld(1100, "Lobby", 0);
			}
			player.sendFriendUpdate(f, isNameChange);
			//player.getAccount().getSession().getTransmitter().send(FriendEncoder.class, new FriendsPacket(f, isNameChange));
		}
	}

	@Override
	public void addFriend(String displayName) {
		if (displayName.length() == 0) {
			return;//Don't bother adding empty names
		}
		String protocolName = StringUtils.format(displayName, FormatType.PROTOCOL);
		if (protocolName.equals(player.getProtocolName())) {
			player.sendGameMessage("You can't add yourself to your own friends list.", MessageOpcode.PRIVATE_SYSTEM);
			return;
		}
		Friend friend = new Friend(protocolName, false);
		friend.setDisplayNames(StringUtils.format(displayName, FormatType.NAME), "");
		/*DisplayName nameData = nameManager.getNameObject(displayName);		
		Friend friend = null;
		FriendManager friendData;
		if (nameData == null) {
			//Player does not exist. In the main game, this would spring an error. Here, we will allow it.
			friend = new Friend(displayName, false);
			friend.setDisplayNames(displayName, "");
			friend.setWorld(getFriendWorld(displayName));//Finds the world data for the friend
			friendData = onlinePlayers.get(NameManager.simplifyName(displayName));
		} else {
			friend = new Friend(nameData.username, false);
			friend.setDisplayNames(nameData.getDisplayName(), nameData.getPrevName());
			friend.setWorld(getFriendWorld(nameData.username));//Finds the world data for the friend
			friendData = onlinePlayers.get(NameManager.simplifyName(displayName));
		}*/
		
		synchronized (this) {//Synchronised to avoid concurrent modification issues
			if (friends.size() >= FRIENDS_LIST_MAX) {
				player.sendGameMessage("Your friends list is full (400 names maximum)", MessageOpcode.PRIVATE_SYSTEM);
				return;
			}
			
			if (ignores.containsKey(protocolName)) {
				player.sendGameMessage("Please remove "+displayName+" from your ignore list first.", MessageOpcode.PRIVATE_SYSTEM);
				return;
			}		
			if (friends.containsKey(protocolName)) {
				player.sendGameMessage(displayName+" is already on your friends list.", MessageOpcode.PRIVATE_SYSTEM);
				return;
			}
			friends.put(protocolName, friend);
		}
		InternalFriendManager friendData = onlinePlayers.get(protocolName);
		if (friendData != null) {
			friend.setWorld(friendData.getWorldInfo(this));
			if (friend.getWorld() != null && friendData.isLobby) {//TODO: Replace this work-around with proper handling for the lobby
				friend.setWorld(1100, "Lobby", 0);
			}
			friendData.setFriendStatus(this, false);//Updates the online status displayed of the current player visible to the player who was removed
		}
		//System.out.println("Adding friend: "+displayName);
		player.sendFriendUpdate(friend, false);
		queueChannelUpdate();
		//player.getAccount().getSession().getTransmitter().send(FriendEncoder.class, new FriendsPacket(friend, false));
	}

	@Override
	public void removeFriend(String displayName) {
		String protocolName = StringUtils.format(displayName, FormatType.PROTOCOL);
		synchronized (this) {
			friends.remove(protocolName);
		}
		InternalFriendManager friendData = onlinePlayers.get(protocolName);
		if (friendData != null) {
			friendData.setFriendStatus(this, false);//Updates the online status displayed of the current player visible to the player who was removed
		}
		/*DisplayName names = nameManager.getNameObject(displayName);
		FriendManager friendData;
		if (names == null) {
			//This removes a player based off their display name. If the server allows players to choose display names which are being used as usernames, this should be removed.
			friends.remove(NameManager.simplifyName(displayName));
			friendData = onlinePlayers.get(NameManager.simplifyName(displayName));
		} else {
			friends.remove(NameManager.simplifyName(names.username));
			friendData = onlinePlayers.get(NameManager.simplifyName(names.username));
		}*/
		queueChannelUpdate();
	}

	@Override
	public void addIgnore(String displayName, boolean tillLogout) {
		if (displayName.length() == 0) {
			return;//Don't bother adding empty names
		}
		String protocolName = StringUtils.format(displayName, FormatType.PROTOCOL);
		if (protocolName.equals(player.getProtocolName())) {
			player.sendGameMessage("You can't add yourself to your own ignore list.", MessageOpcode.PRIVATE_SYSTEM);
			return;
		}
		Ignore ignore = new Ignore(protocolName);
		ignore.setDisplayNames(StringUtils.format(displayName, FormatType.NAME), "");
		/*DisplayName nameData = nameManager.getNameObject(displayName);
		Ignore ignore = null;
		if (nameData == null) {
			//Player does not exist. In the main game, this would spring an error. Here, we will allow it.
			ignore = new Ignore(displayName);
			ignore.setDisplayNames(displayName, "");
		} else {
			ignore = new Ignore(nameData.username);
			ignore.setDisplayNames(nameData.getDisplayName(), nameData.getPrevName());
		}*/
		synchronized (this) {
			if (ignores.size() >= IGNORE_LIST_MAX) {
				player.sendGameMessage("Your ignore list is full. Max of 400 users.", MessageOpcode.PRIVATE_SYSTEM);
				return;
			}
			
			if (friends.containsKey(ignore.username)) {
				player.sendGameMessage("Please remove "+displayName+" from your friends list first.", MessageOpcode.PRIVATE_SYSTEM);
				return;
			}
			if (ignores.containsKey(ignore.username)) {
				player.sendGameMessage(displayName+" is already on your ignore list.", MessageOpcode.PRIVATE_SYSTEM);
				return;
			}
			ignores.put(protocolName, ignore);
		}
		player.sendIgnoreUpdate(ignore, false);
		queueChannelUpdate();
		//player.getAccount().getSession().getTransmitter().send(IgnoreEncoder.class, new IgnoresPacket(ignore, false));
	}

	@Override
	public void removeIgnore(String displayName) {
		String protocolName = StringUtils.format(displayName, FormatType.PROTOCOL);
		synchronized (this) {
			ignores.remove(protocolName);
		}
		queueChannelUpdate();
		/*DisplayName names = nameManager.getNameObject(displayName);
		if (names == null) {
			//This removes a player based off their display name. If the server allows players to choose display names which are being used as usernames, this should be removed.
			ignores.remove(NameManager.simplifyName(displayName));
		} else {
			ignores.remove(NameManager.simplifyName(names.username));
		}
		 */
	}

	@Override
	public void setOnlineStatus(OnlineStatus status) {
		onlineStatus = status;		
		//player.getActionSender().sendOnlineStatus(status);//No need to do this as the client updates automatically
		sendStatusUpdate(this, false);//Corrects the online status for all other players	
	}

	@Override
	public void sendPrivateMessage(String recipient, String message) {
		String protocolName = StringUtils.format(recipient, FormatType.PROTOCOL);
		System.out.println("Sending message '"+message+"' to player "+recipient);
		if (!friends.containsKey(protocolName)) {
			player.sendGameMessage("Unable to send message - player not on your friends list.", MessageOpcode.PRIVATE_SYSTEM);
			return;
		}
		if (!onlinePlayers.containsKey(protocolName)) {
			player.sendGameMessage("That player is offline, or has privacy mode enabled.", MessageOpcode.PRIVATE_SYSTEM);
			return;
		}
		InternalFriendManager friend = onlinePlayers.get(protocolName);
		if (friend.getWorldInfo(this) == null) {
			player.sendGameMessage("That player is offline, or has privacy mode enabled.", MessageOpcode.PRIVATE_SYSTEM);
			return;
		}
		message = StringUtils.format(message, FormatType.DISPLAY);
		PrivateMessage messageObject = new PrivateMessage(message, player.getDisplayName(),
				player.getDisplayName(), player.getRank());
		friend.receivePrivateMessage(getProtocolName(), messageObject);
		messageObject = messageObject.clone();
		messageObject.setIncomming(false);
		player.sendPrivateMessage(messageObject);
	}	
	
	public void receivePrivateMessage (String sender, PrivateMessage message) {
		player.sendPrivateMessage(message);
	}

	@Override
	public void setNote(String displayName, String note, boolean isFriendNote) {
		String protocolName = StringUtils.format(displayName, FormatType.PROTOCOL);
		if (isFriendNote) {
			Friend f = friends.get(protocolName);
			if (f == null) {
				return;
			}
			f.setNote(note);
			player.sendFriendUpdate(f, false);
		} else {
			Ignore i = ignores.get(protocolName);
			if (i == null) {
				return;
			}
			i.setNote(note);
			player.sendIgnoreUpdate(i, false);
		}
	}
	
}
