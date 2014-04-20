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
package org.virtue.game.logic.social;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.virtue.Launcher;
import org.virtue.game.logic.World;
import org.virtue.game.logic.WorldHub;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.social.messages.FriendsMessage;
import org.virtue.game.logic.social.messages.IgnoresMessage;
import org.virtue.game.logic.social.messages.PrivateMessage;
import org.virtue.network.protocol.messages.GameMessage;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.impl.chat.FriendEncoder;
import org.virtue.network.protocol.packet.encoder.impl.chat.IgnoreEncoder;
import org.virtue.network.protocol.packet.encoder.impl.chat.PrivateMessageEncoder;
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
	
	static {
		onlinePlayers.put("test44", new InternalFriendManager(null));
	}
	
	private Player player;
	//private NameManager nameManager;
	
	private static final int FRIENDS_LIST_MAX = 400;
	private static final int IGNORE_LIST_MAX = 400;
	
	private HashMap<String, Friend> friends = new HashMap<String, Friend>(FRIENDS_LIST_MAX);
	private HashMap<String, Ignore> ignores = new HashMap<String, Ignore>(IGNORE_LIST_MAX);
	
	private OnlineStatus onlineStatus = OnlineStatus.EVERYONE;
	
	private WorldHub currentWorld = null;
	
	private boolean isLobby;
	
	public InternalFriendManager (Player player) {
		this.player = player;
	}
	
	
	public String getProtocolName () {
		return player.getAccount().getUsername().getAccountNameAsProtocol();
	}
	
	@Override
	public void init () {
		friends.put("test222", new Friend("test222", false));
		friends.put("test_3", new Friend("test_3", true));//TODO: Remove this (it's only for testing purposes)
		ignores.put("test4", new Ignore("test4"));
		ignores.put("test_5", new Ignore("test_5"));
		//player.getActionSender().sendOnlineStatus(onlineStatus);
		//player.getActionSender().sendUnlockFriendsList();
		currentWorld = World.getWorld();//player.getWorld().getData();
		isLobby = !player.isInWorld();
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
		player.getAccount().getSession().getTransmitter().send(FriendEncoder.class, new FriendsMessage(friends.values().toArray(new Friend[friends.size()])));
		player.getAccount().getSession().getTransmitter().send(IgnoreEncoder.class, new IgnoresMessage(ignores.values().toArray(new Ignore[ignores.size()])));
		onlinePlayers.put(player.getAccount().getUsername().getAccountNameAsProtocol(), this);
		sendStatusUpdate(this, false);
		System.out.println("Registered "+player.getAccount().getUsername().getName()+" on friend server.");
	}
	
	@Override
	public void shutdown () {
		this.currentWorld = null;
		sendStatusUpdate(this, false);
	}
	
	/*public void serialise (DataOutputStream output) throws IOException {
		synchronized (this) {
			output.writeShort(friends.size());
			for (Friend f : friends.values()) {
				StreamUtil.writeString(output, f.username);
				output.writeByte(f.isReferred() ? 1 : 0);
				output.writeByte(f.getFcRank());
				StreamUtil.writeString(output, f.getNote());
			}
			output.writeShort(ignores.size());
			for (Ignore i : ignores.values()) {
				StreamUtil.writeString(output, i.username);
				StreamUtil.writeString(output, i.getNote());
			}
			output.writeByte(onlineStatus.getCode());
		}
	}
	
	public void deserialise (DataInputStream input, int version) throws IOException {
		synchronized (this) {
			if (version >= 1) {
				int friendListSize = input.readUnsignedShort();
				String name, note;
				int fcRank;
				boolean isReferred;
				friends.clear();
				for (int i=0;i<friendListSize;i++) {
					name = StreamUtil.readString(input);
					isReferred = (input.readUnsignedByte() == 1);
					fcRank = input.readByte();
					note = StreamUtil.readString(input);
					if (name.length() == 0) {
						continue;
					}
					Friend f = new Friend(name, isReferred, fcRank, note);
					friends.put(NameManager.simplifyName(name), f);
				}
				int ignoreListSize = input.readUnsignedShort();
				for (int i=0;i<ignoreListSize;i++) {
					name = StreamUtil.readString(input);
					note = StreamUtil.readString(input);
					if (name.length() == 0) {
						continue;
					}
					Ignore ig = new Ignore(name, note);
					ignores.put(NameManager.simplifyName(name), ig);
				}
				int onlineStatusCode = input.readUnsignedByte();
				onlineStatus = OnlineStatus.get(onlineStatusCode);
			}
		}
	}*/
	
	/*public OnlineStatus getOnlineStatus () {
		return onlineStatus;
	}*/
	
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
			player.getAccount().getSession().getTransmitter().send(FriendEncoder.class, new FriendsMessage(f, isNameChange));
		}
	}

	@Override
	public void addFriend(String displayName) {
		if (displayName.length() == 0) {
			return;//Don't bother adding empty names
		}
		String protocolName = StringUtils.format(displayName, FormatType.PROTOCOL);
		if (protocolName.equals(player.getAccount().getUsername().getAccountNameAsProtocol())) {
			player.getPacketDispatcher().dispatchMessage("You can't add yourself to your own friends list.", MessageOpcode.GAME_PRIVATE);
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
				player.getPacketDispatcher().dispatchMessage("Your friends list is full (400 names maximum)", MessageOpcode.GAME_PRIVATE);
				return;
			}
			
			if (ignores.containsKey(protocolName)) {
				player.getPacketDispatcher().dispatchMessage("Please remove "+displayName+" from your ignore list first.", MessageOpcode.GAME_PRIVATE);
				return;
			}		
			if (friends.containsKey(protocolName)) {
				player.getPacketDispatcher().dispatchMessage(displayName+" is already on your friends list.", MessageOpcode.GAME_PRIVATE);
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
		player.getAccount().getSession().getTransmitter().send(FriendEncoder.class, new FriendsMessage(friend, false));
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
	}

	@Override
	public void addIgnore(String displayName, boolean tillLogout) {
		if (displayName.length() == 0) {
			return;//Don't bother adding empty names
		}
		String protocolName = StringUtils.format(displayName, FormatType.PROTOCOL);
		if (protocolName.equals(player.getAccount().getUsername().getAccountNameAsProtocol())) {
			player.getPacketDispatcher().dispatchMessage("You can't add yourself to your own ignore list.", MessageOpcode.GAME_PRIVATE);
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
				player.getPacketDispatcher().dispatchMessage("Your ignore list is full. Max of 400 users.", MessageOpcode.GAME_PRIVATE);
				return;
			}
			
			if (friends.containsKey(ignore.username)) {
				player.getPacketDispatcher().dispatchMessage("Please remove "+displayName+" from your friends list first.", MessageOpcode.GAME_PRIVATE);
				return;
			}
			if (ignores.containsKey(ignore.username)) {
				player.getPacketDispatcher().dispatchMessage(displayName+" is already on your ignore list.", MessageOpcode.GAME_PRIVATE);
				return;
			}
			ignores.put(protocolName, ignore);
		}
		player.getAccount().getSession().getTransmitter().send(IgnoreEncoder.class, new IgnoresMessage(ignore, false));
	}

	@Override
	public void removeIgnore(String displayName) {
		String protocolName = StringUtils.format(displayName, FormatType.PROTOCOL);
		synchronized (this) {
			ignores.remove(protocolName);
		}
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
			System.out.println("Cannot send message - "+recipient+" is not on friends list.");
			//TODO: Not on friends list; Show a message for this...
			return;
		}
		if (!onlinePlayers.containsKey(protocolName)) {
			System.out.println("Cannot send message - "+recipient+" is offline.");
			//TODO: Not online; Show a message for this...
			return;
		}
		InternalFriendManager friend = onlinePlayers.get(protocolName);
		if (friend.getWorldInfo(this) == null) {
			System.out.println("Cannot send message - "+recipient+" does not show online to player.");
			//TODO: Not displayed as online; Show a message for this...
			return;
		}
		message = StringUtils.format(message, FormatType.DISPLAY);
		PrivateMessage messageObject = new PrivateMessage(message, player.getAccount().getUsername().getName(),
				player.getAccount().getUsername().getName(), player.getAccount().getRank());
		friend.receivePrivateMessage(getProtocolName(), messageObject);
		messageObject = messageObject.clone();
		messageObject.setIncomming(false);
		player.getAccount().getSession().getTransmitter().send(PrivateMessageEncoder.class, messageObject);
		/*RS3PacketBuilder buffer = new RS3PacketBuilder(260);//This is only for testing purposes. Remove when done.
		buffer.putPacketVarShort(38);
		buffer.putString(getProtocolName());
		Launcher.getHuffman().huffmanEncrypt(buffer, message);
		buffer.endPacketVarShort();
		player.getAccount().getSession().getTransmitter().send(buffer);*/
	}	
	
	public void receivePrivateMessage (String sender, PrivateMessage message) {
		/*RS3PacketBuilder buffer = new RS3PacketBuilder(260);//This is only for testing purposes. Remove when done.
		buffer.putPacketVarShort(116);
		buffer.put(0);
		buffer.putString(sender);
		byte[] hash = new byte[5];
		Launcher.getRandom().nextBytes(hash);
		for (byte v : hash) {
			buffer.put(v);
		}
		buffer.put(0);
		buffer.endPacketVarShort();*/
		player.getAccount().getSession().getTransmitter().send(PrivateMessageEncoder.class, message);
	}
	
}
