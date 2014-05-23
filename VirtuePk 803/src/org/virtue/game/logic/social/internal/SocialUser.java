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

import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.identity.Rank;
import org.virtue.game.logic.node.interfaces.impl.FriendsChatSettings;
import org.virtue.game.logic.social.ChannelPermission;
import org.virtue.game.logic.social.ChannelRank;
import org.virtue.game.logic.social.clans.ccdelta.ClanChannelDelta;
import org.virtue.game.logic.social.messages.ClanChannelDeltaPacket;
import org.virtue.game.logic.social.messages.ClanChannelPacket;
import org.virtue.game.logic.social.messages.FriendsChatMessage;
import org.virtue.game.logic.social.messages.FriendsChatPacket;
import org.virtue.game.logic.social.messages.FriendsPacket;
import org.virtue.game.logic.social.messages.IgnoresPacket;
import org.virtue.game.logic.social.messages.PrivateMessage;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.impl.chat.ClanChannelDeltaEncoder;
import org.virtue.network.protocol.packet.encoder.impl.chat.ClanChannelEncoder;
import org.virtue.network.protocol.packet.encoder.impl.chat.FriendEncoder;
import org.virtue.network.protocol.packet.encoder.impl.chat.FriendsChatEncoder;
import org.virtue.network.protocol.packet.encoder.impl.chat.FriendsChatMessageEncoder;
import org.virtue.network.protocol.packet.encoder.impl.chat.IgnoreEncoder;
import org.virtue.network.protocol.packet.encoder.impl.chat.PrivateMessageEncoder;
import org.virtue.utility.StringUtils;
import org.virtue.utility.StringUtils.FormatType;

/**
 * An abstraction object between the social module and the {@link Player} object. 
 * For external social modules, this will contain the actual player data. For internal modules, it links the the player data
 *
 * @author Sundays211
 */
public class SocialUser {
	
	private Player player;
	private final String protocolName;
	
	public SocialUser (Player p) {
		this(p, p.getAccount().getUsername().getAccountNameAsProtocol());
	}
	
	public SocialUser (Player p, String protocolName) {
		this.player = p;
		this.protocolName = protocolName;
	}
	
	/**
	 * Gets the current display name for the player
	 * @return	The display name
	 */
	public String getDisplayName () {
		return (player == null ? StringUtils.format(protocolName, FormatType.NAME) : player.getAccount().getUsername().getName());
	}
	
	/**
	 * Gets the protocol name for the player. Used as the unique ID for representing the player
	 * @return	The protocol name
	 */
	public String getProtocolName () {
		return protocolName;
	}
	
	public boolean isInWorld () {
		return player.isInWorld();
	}
	
	public String getWorldName () {
		return "World 1";
	}
	
	public int getWorldID () {
		return (player.isInWorld() ? 1 : 1100);
	}
	
	public Rank getRank () {
		return player.getAccount().getRank();
	}
	
	public void sendGameMessage (String message, MessageOpcode type) {
		player.getPacketDispatcher().dispatchMessage(message, type);
	}
	
	public void sendPrivateMessage (PrivateMessage message) {
		player.getAccount().getSession().getTransmitter().send(PrivateMessageEncoder.class, message);
	}
	
	public void sendFriendsChatPacket (FriendsChatPacket packet) {
		try {
			player.getAccount().getSession().getTransmitter().send(FriendsChatEncoder.class, packet);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void sendLeaveFriendsChat () {
		player.getChatManager().setCurrentChannelOwner(null);
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacketVarShort(OutgoingOpcodes.FRIENDS_CHANNEL_PACKET);
		buffer.endPacketVarShort();
		player.getAccount().getSession().getTransmitter().send(buffer);
	}
	
	public void sendFriendsChatMessage (FriendsChatMessage message) {
		player.getAccount().getSession().getTransmitter().send(FriendsChatMessageEncoder.class, message);
	}
	
	public void sendFriendsList (Friend[] friends) {
		FriendsPacket.Entry[] entries = new FriendsPacket.Entry[friends.length];
		for (int i=0;i<friends.length;i++) {
			entries[i] = FriendsPacket.Entry.fromFriendObject(friends[i]);
		}
		player.getAccount().getSession().getTransmitter().send(FriendEncoder.class, new FriendsPacket(entries));
	}
	
	public void sendIgnoreList(Ignore[] ignores) {
		IgnoresPacket.Entry[] entries = new IgnoresPacket.Entry[ignores.length];
		for (int i=0;i<ignores.length;i++) {
			entries[i] = IgnoresPacket.Entry.fromIgnoreObject(ignores[i]);
		}
		player.getAccount().getSession().getTransmitter().send(IgnoreEncoder.class, new IgnoresPacket(entries));
	}
	
	public void sendFriendUpdate (Friend friend, boolean isNameChange) {
		FriendsPacket.Entry entry = FriendsPacket.Entry.fromFriendObject(friend);
		player.getAccount().getSession().getTransmitter().send(FriendEncoder.class, new FriendsPacket(entry, false));
	}
	
	public void sendIgnoreUpdate (Ignore ignore, boolean isNameChange) {
		IgnoresPacket.Entry entry = IgnoresPacket.Entry.fromIgnoreObject(ignore);
		player.getAccount().getSession().getTransmitter().send(IgnoreEncoder.class, new IgnoresPacket(entry, false));
	}
	
	public void sendPermissionUpdate (ChannelPermission permission, ChannelRank rank) {
		FriendsChatSettings settings = player.getChatManager().getFriendsChatSettings();
		if (settings == null) {
			return;
		}
		settings.sendPermission(permission, rank);
	}
	
	public void sendChannelPrefix (String prefix) {
		FriendsChatSettings settings = player.getChatManager().getFriendsChatSettings();
		if (settings == null) {
			return;
		}
		settings.sendPrefix(prefix);
	}
	
	public void sendClanChannelDelta (boolean isGuestCc, long clanHash, long updateNum, ClanChannelDelta[] deltaNodes) {
		ClanChannelDeltaPacket deltaPacket = new ClanChannelDeltaPacket(isGuestCc, clanHash, 
				updateNum, deltaNodes);
		player.getAccount().getSession().getTransmitter().send(ClanChannelDeltaEncoder.class, deltaPacket);
	}
	
	public void sendClanChannelFull (ClanChannelPacket packet) {
		player.getAccount().getSession().getTransmitter().send(ClanChannelEncoder.class, packet);
	}
	
	public void sendLeaveClanChannel (boolean isGuest) {
		//TODO: Also set the current clan chat to null
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacketVarShort(OutgoingOpcodes.CLAN_CHANNEL_FULL);
		buffer.put(isGuest ? 0 : 1);
		buffer.endPacketVarShort();
		player.getAccount().getSession().getTransmitter().send(buffer);
	}
}
