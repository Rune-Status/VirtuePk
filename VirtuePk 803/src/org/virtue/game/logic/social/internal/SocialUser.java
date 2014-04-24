package org.virtue.game.logic.social.internal;

import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.identity.Rank;
import org.virtue.game.logic.social.messages.FriendsChatMessage;
import org.virtue.game.logic.social.messages.FriendsChatPacket;
import org.virtue.game.logic.social.messages.FriendsPacket;
import org.virtue.game.logic.social.messages.IgnoresPacket;
import org.virtue.game.logic.social.messages.PrivateMessage;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.impl.chat.FriendEncoder;
import org.virtue.network.protocol.packet.encoder.impl.chat.FriendsChatEncoder;
import org.virtue.network.protocol.packet.encoder.impl.chat.FriendsChatMessageEncoder;
import org.virtue.network.protocol.packet.encoder.impl.chat.IgnoreEncoder;
import org.virtue.network.protocol.packet.encoder.impl.chat.PrivateMessageEncoder;
import org.virtue.utility.StringUtils;
import org.virtue.utility.StringUtils.FormatType;

/**
 *
 * @author Virtue Development Team 2014 (c).
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
	
	@Deprecated
	public Player getPlayer() {
		return player;
	}
	
	public String getDisplayName () {
		return (player == null ? StringUtils.format(protocolName, FormatType.NAME) : player.getAccount().getUsername().getName());
	}
	
	public String getProtocolName () {
		return protocolName;
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
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacketVarShort(OutgoingOpcodes.FRIENDS_CHANNEL_PACKET);
		buffer.endPacketVarShort();
		player.getAccount().getSession().getTransmitter().send(buffer);
	}
	
	public void sendFriendsChatMessage (FriendsChatMessage message) {
		player.getAccount().getSession().getTransmitter().send(FriendsChatMessageEncoder.class, message);
	}
	
	public void sendFriendsList (Friend[] friends) {
		player.getAccount().getSession().getTransmitter().send(FriendEncoder.class, new FriendsPacket(friends));
	}
	
	public void sendIgnoreList(Ignore[] ignores) {
		player.getAccount().getSession().getTransmitter().send(IgnoreEncoder.class, new IgnoresPacket(ignores));
	}
	
	public void sendFriendUpdate (Friend friend, boolean isNameChange) {
		player.getAccount().getSession().getTransmitter().send(FriendEncoder.class, new FriendsPacket(friend, false));
	}
	
	public void sendIgnoreUpdate (Ignore ignore, boolean isNameChange) {
		player.getAccount().getSession().getTransmitter().send(IgnoreEncoder.class, new IgnoresPacket(ignore, false));
	}
}
