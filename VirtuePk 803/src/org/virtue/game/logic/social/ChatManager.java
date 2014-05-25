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

import java.io.FileNotFoundException;

import org.virtue.Launcher;
import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.game.logic.World;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.impl.FriendsChatSettings;
import org.virtue.game.logic.social.clans.ClanChannelManager;
import org.virtue.game.logic.social.clans.ClanManager;
import org.virtue.game.logic.social.internal.InternalFriendManager;
import org.virtue.game.logic.social.internal.InternalFriendsChatManager;
import org.virtue.game.logic.social.internal.InternalSocialUser;
import org.virtue.game.logic.social.messages.PublicMessage;
import org.virtue.network.io.IOHub;
import org.virtue.network.protocol.messages.ClientScriptVar;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;
import org.virtue.network.protocol.packet.encoder.impl.EmptyPacketEncoder;
import org.virtue.network.protocol.packet.encoder.impl.chat.OnlineStatusEncoder;
import org.virtue.network.protocol.packet.encoder.impl.chat.PublicMessageEncoder;
import org.virtue.utility.StringUtils;
import org.virtue.utility.StringUtils.FormatType;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Contains the tools necessary to manage chat for the player, including interacting with friends chat channels, friends and ignores management, and public messaging
 * 
 * @author Sundays211
 * @since Apr 17, 2014
 */
public class ChatManager {
	
	private enum ChannelStage { NONE, JOINING, JOINED, LEAVING }
	
	private static final FriendsChatManager friendsChatManager = new InternalFriendsChatManager();
	
	//TODO: Make this private
	public static final ClanManager clanManager = new ClanManager();
	
	/**
	 * Represents the player.
	 */
	private final Player player;
	
	private final InternalSocialUser socialUser;
	
	/**
	 * Represents the type of message that the next message(s) will be
	 */
	private ChatType chatType;
	
	/**
	 * Represents the current online status of the player
	 */
	private OnlineStatus onlineStatus = OnlineStatus.FRIENDS;
	
	/**
	 * Represents the friends/ignores manager for the player
	 */
	private FriendManager friendManager;
	
	//=================================Friends chat data=================================//
	
	/**
	 * Represents the name of the channel the player is currently in, or "null" if the player is not in a channel
	 */
	private String currentFriendsChat = null;
	
	/**
	 * Represents the name of the last channel the player was or "null" if the player has never been in a friends chat
	 */
	private String lastFriendsChat = "";
	
	/**
	 * Represents whether the player should automatically join the last friends chat they were in when they log into the game.
	 */
	private boolean autoJoinFriendsChat = false;
	
	/**
	 * Represents the current friends chat status of the player
	 */
	private ChannelStage channelStage = ChannelStage.NONE;
	
	/**
	 * Represents the friends chat settings interface for the player
	 */
	private FriendsChatSettings friendsChatSettings = null;
	
	//=================================Clan data=================================//
	
	private long myClanHash = 0L;
	
	private long guestClanHash = 0L;
	
	private boolean inClanChannel = false;
	
	/**
	 * Constructs a new {@code ChatManager} instance for the specified player
	 * @param player	The player
	 */
	public ChatManager (Player player) {
		this.player = player;
		this.socialUser = new InternalSocialUser(player);
		String protocolName = player.getAccount().getUsername().getAccountNameAsProtocol();
		if (IOHub.getFriendsIO().exists(protocolName)) {
			try {
				friendManager = IOHub.getFriendsIO().load(protocolName, new InternalSocialUser(player));
			} catch (FileNotFoundException e) {
				friendManager = new InternalFriendManager(new InternalSocialUser(player));
			}
		} else {
			friendManager = new InternalFriendManager(new InternalSocialUser(player));
		}
	}
	
	/**
	 * Sets the type for the player's chat messages
	 * @param typeCode	The code representing the type
	 */
	public void setChatType (int typeCode) {
		setChatType(ChatType.forCode(typeCode));
	}
	
	/**
	 * Sets the type for the player's chat messages
	 * @param type	The chat type
	 */
	public void setChatType (ChatType type) {
		chatType = type;
	}
	
	public void init (boolean lobby) {
		player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(1303, lastFriendsChat, autoJoinFriendsChat ? 1 : 0, lastFriendsChat.length() == 0 ? 0 : 1, 93519895));//Runscript: [1303, 93519895, 1, 1, Test]
		
		player.getAccount().getSession().getTransmitter().send(OnlineStatusEncoder.class, onlineStatus);
		player.getAccount().getSession().getTransmitter().send(EmptyPacketEncoder.class, OutgoingOpcodes.UNLOCK_FRIENDS_LIST);
		friendManager.init();
		if (myClanHash != 0L) {
			clanManager.getChannelManager().joinMyChannel(socialUser);
		}
	}
	
	/**
	 * Handles the disconnection of the player's friends list and friends chat, and saves the player's friend data
	 */
	public void disconnect () {
		IOHub.getFriendsIO().save(player.getAccount().getUsername().getAccountNameAsProtocol(), friendManager);
		friendManager.shutdown();
		if (channelStage.equals(ChannelStage.JOINED)) {
			friendsChatManager.leaveChannel(socialUser, true);
		}
		//Leave both the clan channel and guest clan channel
		clanManager.getChannelManager().leaveChannel(socialUser, true, true);
		clanManager.getChannelManager().leaveChannel(socialUser, false, true);
	}
	
	/**
	 * Gets the manager for this player's friends and ignores
	 * @return	The friend manager
	 */
	public FriendManager getFriendManager () {
		return friendManager;
	}
	
	public ClanChannelManager getClanChatManager () {
		return clanManager.getChannelManager();
	}
	
	public JsonObject serialiseData () {
		JsonObject chatData = new JsonObject();
		chatData.addProperty("onlineStatus", onlineStatus.getStatusCode());
		chatData.addProperty("lastFriendsChat", lastFriendsChat);
		chatData.addProperty("autoJoinFc", currentFriendsChat != null);
		chatData.addProperty("clanHash", myClanHash);
		return chatData;
	}
	
	public void deserialiseData (JsonObject chatData) {
		onlineStatus = OnlineStatus.forCode(chatData.get("onlineStatus").getAsInt());
		if (onlineStatus == null) {
			onlineStatus = OnlineStatus.FRIENDS;
		}
		lastFriendsChat = chatData.get("lastFriendsChat").getAsString();
		autoJoinFriendsChat = chatData.get("autoJoinFc").getAsBoolean();
		JsonElement clanData = chatData.get("clanHash");
		if (clanData != null) {
			myClanHash = clanData.getAsLong();
		}
	}
	
	/**
	 * Gets the type for the player's chat messages
	 * @return
	 */
	public ChatType getChatType () {
		return chatType;
	}
	
	//=================================Friends chat section=================================//
	
	/**
	 * Handles a request to join or leave a friends chat channel
	 * @param name	The name of the channel to join (or an empty string to leave)
	 */
	public void handleFriendsChatJoin (String name) {
		if (name.isEmpty()) {
			if (channelStage.equals(ChannelStage.LEAVING)) {
				player.getPacketDispatcher().dispatchMessage("Leave request already in progress - please wait...", MessageOpcode.FRIENDS_CHAT_SYSTEM);
			} else if (channelStage.equals(ChannelStage.NONE)) {
				player.getPacketDispatcher().dispatchMessage("You are not currently in a channel.", MessageOpcode.FRIENDS_CHAT_SYSTEM);				
			} else if (channelStage.equals(ChannelStage.JOINED)) {
				channelStage = ChannelStage.LEAVING;
				friendsChatManager.leaveChannel(socialUser, false);				
			}
		} else {
			if (channelStage.equals(ChannelStage.LEAVING) || channelStage.equals(ChannelStage.JOINED)) {
				player.getPacketDispatcher().dispatchMessage("Please wait until you are logged out of your previous channel.", MessageOpcode.FRIENDS_CHAT_SYSTEM);
			} else if (channelStage.equals(ChannelStage.JOINING)) {
				player.getPacketDispatcher().dispatchMessage("Already attempting to join a channel - please wait...", MessageOpcode.FRIENDS_CHAT_SYSTEM);
			} else if (channelStage.equals(ChannelStage.NONE)) {
				player.getPacketDispatcher().dispatchMessage("Attempting to join channel...", MessageOpcode.FRIENDS_CHAT_SYSTEM);
				channelStage = ChannelStage.JOINING;
				friendsChatManager.joinChannel(socialUser, name);
			}
		}
	}
	
	public String getCurrentChannelOwner () {
		return currentFriendsChat;
	}
	
	public void setCurrentChannelOwner (String ownerName) {
		currentFriendsChat = ownerName;
		if (ownerName == null) {
			channelStage = ChannelStage.NONE;
		} else {
			lastFriendsChat = ownerName;
			channelStage = ChannelStage.JOINED;
		}
	}
	
	public String getLastFriendsChat () {
		return lastFriendsChat;
	}
	
	public void setLastFriendsChat (String lastFriendsChat) {
		this.lastFriendsChat = lastFriendsChat;
	}
	
	public FriendsChatSettings getFriendsChatSettings () {
		return friendsChatSettings;
	}
	
	public void setFriendsChatSettings (FriendsChatSettings friendsChatSettings) {
		this.friendsChatSettings = friendsChatSettings;
	}
	
	public void handleFriendsChatMessage (String message) {
		String formattedMessage = StringUtils.format(message, FormatType.DISPLAY);
		friendsChatManager.sendMessage(socialUser, formattedMessage);
	}
	
	public void handleFriendsChatKick (String name) {		
		friendsChatManager.kickBanUser(socialUser, name);
	}
	
	//=================================Clan section=================================//
	
	public long getMyClanHash () {
		return myClanHash;
	}
	
	public void setMyClanHash (long myClanHash) {
		this.myClanHash = myClanHash;
	}
	
	public long getGuestClanHash () {
		return guestClanHash;
	}
	
	public void setGuestClanHash (long clanHash) {
		this.guestClanHash = clanHash;
	}
	
	public boolean inClanChannel () {
		return inClanChannel;
	}
	
	public void setInClanChannel (boolean inClanChannel) {
		this.inClanChannel = inClanChannel;
	}
	
	public void joinMyClanChannel () {
		if (!inClanChannel) {
			clanManager.getChannelManager().joinMyChannel(socialUser);
		}
	}
	
	public void joinGuestChannel (String clanName) {
		if (guestClanHash == 0L) {
			clanManager.getChannelManager().joinGuestChannel(socialUser, clanName);
		}
	}
	
	public void leaveClanChannel (boolean isGuest) {
		clanManager.getChannelManager().leaveChannel(socialUser, isGuest, false);
	}
	
	public void handleClanChannelMessage (String message, boolean isGuest) {
		String formattedMessage = StringUtils.format(message, FormatType.DISPLAY);
		clanManager.getChannelManager().sendMessage(socialUser, formattedMessage, isGuest);
	}
	
	/**
	 * Handles the specified public chat message
	 * @param message		The message
	 * @param colourEffect	The colour effect
	 * @param moveEffect	The movement effect
	 */
	public void handlePublicMessage (String message, int colourEffect, int moveEffect) {
		System.out.println("Received message: message="+message+", type="+chatType+", colour="+colourEffect+", move="+moveEffect);
		String formattedMessage = StringUtils.format(message, FormatType.DISPLAY);
		int effects = (colourEffect << 8) | (moveEffect & 0xff);
		
		PublicMessage msgObject = new PublicMessage(formattedMessage, effects, player.getIndex(), player.getAccount().getRank());
		
		for (Player p : World.getWorld().getPlayers()) {
			if (p == null || !p.exists() || p.getViewport().getLocalPlayers()[player.getIndex()] == null) {
				continue;
			}
			p.getAccount().getSession().getTransmitter().send(PublicMessageEncoder.class, msgObject);
		}
	}
	
	public void setOnlineStatus (OnlineStatus status) {
		this.onlineStatus = status;
		friendManager.setOnlineStatus(status);
	}
	
	public OnlineStatus getOnlineStatus () {
		return onlineStatus;
	}
	
	public static byte[] generateMessageHash () {
		byte[] hash = new byte[5];
		Launcher.getRandom().nextBytes(hash);
		return hash;
	}
}
