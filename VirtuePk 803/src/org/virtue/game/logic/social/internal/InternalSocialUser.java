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

import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.identity.Rank;
import org.virtue.game.logic.node.interfaces.impl.FriendsChatSettings;
import org.virtue.game.logic.social.ChannelPermission;
import org.virtue.game.logic.social.ChannelRank;
import org.virtue.game.logic.social.SocialUser;
import org.virtue.game.logic.social.messages.ClanChannelDeltaPacket;
import org.virtue.game.logic.social.messages.ClanChannelMessage;
import org.virtue.game.logic.social.messages.ClanChannelPacket;
import org.virtue.game.logic.social.messages.ClanSettingsPacket;
import org.virtue.game.logic.social.messages.FriendsChatMessage;
import org.virtue.game.logic.social.messages.FriendsChatPacket;
import org.virtue.game.logic.social.messages.FriendsPacket;
import org.virtue.game.logic.social.messages.IgnoresPacket;
import org.virtue.game.logic.social.messages.PrivateMessage;
import org.virtue.network.protocol.messages.ClientScriptVar;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.impl.chat.ClanChannelDeltaEncoder;
import org.virtue.network.protocol.packet.encoder.impl.chat.ClanChannelEncoder;
import org.virtue.network.protocol.packet.encoder.impl.chat.ClanChannelMessageEncoder;
import org.virtue.network.protocol.packet.encoder.impl.chat.ClanSettingsEncoder;
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
public class InternalSocialUser implements SocialUser {
	
	private final Player player;
	private final String protocolName;
	
	public InternalSocialUser (Player p) {
		this(p, p.getAccount().getUsername().getAccountNameAsProtocol());
	}
	
	public InternalSocialUser (Player p, String protocolName) {
		this.player = p;
		this.protocolName = protocolName;
	}
	
	@Override
	public String getDisplayName () {
		return (player == null ? StringUtils.format(protocolName, FormatType.NAME) : player.getAccount().getUsername().getName());
	}
	
	@Override
	public String getProtocolName () {
		return protocolName;
	}
	
	@Override
	public boolean isInWorld () {
		return player.isInWorld();
	}
	
	@Override
	public String getWorldName () {
		return "World 1";
	}
	
	@Override
	public int getWorldID () {
		return (player.isInWorld() ? 1 : 1100);
	}
	
	public boolean isOnline () {
		return player.exists();
	}
	
	@Override
	public Rank getRights () {
		return player.getAccount().getRank();
	}
	
	@Override
	public void sendSystemMessage (String message, MessageOpcode type) {
		player.getPacketDispatcher().dispatchMessage(message, type);
	}
	
	//=================================Friends/Ignores section=================================//
	
	@Override
	public void sendPrivateMessage (PrivateMessage message) {
		player.getAccount().getSession().getTransmitter().send(PrivateMessageEncoder.class, message);
	}
	
	@Override
	public void sendFriendsList (Friend[] friends) {
		FriendsPacket.Entry[] entries = new FriendsPacket.Entry[friends.length];
		for (int i=0;i<friends.length;i++) {
			entries[i] = FriendsPacket.Entry.fromFriendObject(friends[i]);
		}
		player.getAccount().getSession().getTransmitter().send(FriendEncoder.class, new FriendsPacket(entries));
	}
	
	@Override
	public void sendIgnoreList(Ignore[] ignores) {
		IgnoresPacket.Entry[] entries = new IgnoresPacket.Entry[ignores.length];
		for (int i=0;i<ignores.length;i++) {
			entries[i] = IgnoresPacket.Entry.fromIgnoreObject(ignores[i]);
		}
		player.getAccount().getSession().getTransmitter().send(IgnoreEncoder.class, new IgnoresPacket(entries));
	}
	
	@Override
	public void sendFriendUpdate (Friend friend, boolean isNameChange) {
		FriendsPacket.Entry entry = FriendsPacket.Entry.fromFriendObject(friend);
		player.getAccount().getSession().getTransmitter().send(FriendEncoder.class, new FriendsPacket(entry, false));
	}
	
	@Override
	public void sendIgnoreUpdate (Ignore ignore, boolean isNameChange) {
		IgnoresPacket.Entry entry = IgnoresPacket.Entry.fromIgnoreObject(ignore);
		player.getAccount().getSession().getTransmitter().send(IgnoreEncoder.class, new IgnoresPacket(entry, false));
	}
	
	//=================================Friends chat section=================================//
	
	@Override
	public String getCurrentChannelOwner () {
		return player.getChatManager().getCurrentChannelOwner();
	}
	
	@Override
	public void setCurrentChannelOwner (String ownerName) {
		player.getChatManager().setCurrentChannelOwner(ownerName);;
	}
	
	@Override
	public void sendFriendsChatPacket (FriendsChatPacket packet) {
		try {
			player.getAccount().getSession().getTransmitter().send(FriendsChatEncoder.class, packet);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void sendLeaveFriendsChat () {
		player.getChatManager().setCurrentChannelOwner(null);
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacketVarShort(OutgoingOpcodes.FRIENDS_CHANNEL_PACKET);
		buffer.endPacketVarShort();
		player.getAccount().getSession().getTransmitter().send(buffer);
	}
	
	@Override
	public void sendFriendsChatMessage (FriendsChatMessage message) {
		player.getAccount().getSession().getTransmitter().send(FriendsChatMessageEncoder.class, message);
	}
	
	@Override
	public void sendPermissionUpdate (ChannelPermission permission, ChannelRank rank) {
		FriendsChatSettings settings = player.getChatManager().getFriendsChatSettings();
		if (settings == null) {
			return;
		}
		settings.sendPermission(permission, rank);
	}
	
	@Override
	public void sendChannelPrefix (String prefix) {
		FriendsChatSettings settings = player.getChatManager().getFriendsChatSettings();
		if (settings == null) {
			return;
		}
		settings.sendPrefix(prefix);
	}
	
	//=================================Clan channel section=================================//
	
	@Override
	public long getMyClanHash () {
		return player.getChatManager().getMyClanHash();
	}
	
	@Override
	public void setMyClanHash (long myClanHash) {
		player.getChatManager().setMyClanHash(myClanHash);
	}
	
	@Override
	public long getGuestClanHash () {
		return player.getChatManager().getGuestClanHash();
	}
	
	@Override
	public void setGuestClanHash (long clanHash) {
		player.getChatManager().setGuestClanHash(clanHash);
	}
	
	@Override
	public boolean inClanChannel () {
		return player.getChatManager().inClanChannel();
	}
	
	@Override
	public void setInClanChannel (boolean inClanChannel) {
		player.getChatManager().setInClanChannel(inClanChannel);
	}
	
	@Override
	public boolean isMyClan (long hash) {
		return hash == player.getChatManager().getMyClanHash();
	}
	
	@Override
	public void sendClanChannelDelta (ClanChannelDeltaPacket packet) {
		player.getAccount().getSession().getTransmitter().send(ClanChannelDeltaEncoder.class, packet);
	}
	
	@Override
	public void sendClanChannelFull (ClanChannelPacket packet) {
		if (packet.isGuestUpdate()) {
			player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(4453, 72744972, 72744974, 72745036, 72745072));
			player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(4453, 94371842, 94371866, 94371861, 94371968));
		}
		player.getAccount().getSession().getTransmitter().send(ClanChannelEncoder.class, packet);
	}
	
	@Override
	public void sendLeaveClanChannel (boolean isGuest) {
		if (isGuest) {
			player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(4438, 72744972));
			setGuestClanHash(0L);
		} else {
			setInClanChannel(false);
		}
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacketVarShort(OutgoingOpcodes.CLAN_CHANNEL_FULL);
		buffer.put(isGuest ? 0 : 1);
		buffer.endPacketVarShort();
		player.getAccount().getSession().getTransmitter().send(buffer);
	}
	
	@Override
	public void sendClanChatMessage (ClanChannelMessage message) {
		player.getAccount().getSession().getTransmitter().send(ClanChannelMessageEncoder.class, message);
	}
	
	//=================================Clan data section=================================//
	
	public void sendClanSettingsFull (ClanSettingsPacket packet) {
		player.getAccount().getSession().getTransmitter().send(ClanSettingsEncoder.class, packet);
	}
	
	@Override
	public boolean equals (Object anotherObject) {
		if (anotherObject == this) {
			return true;
		}
		if (anotherObject instanceof InternalSocialUser) {
			InternalSocialUser anotherUser = (InternalSocialUser) anotherObject;
			return anotherUser.player.equals(this.player);
		}
		return false;
	}
}
