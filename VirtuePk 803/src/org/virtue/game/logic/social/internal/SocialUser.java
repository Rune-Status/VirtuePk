package org.virtue.game.logic.social.internal;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.identity.Rank;
import org.virtue.game.logic.social.messages.FriendsChatPacket;
import org.virtue.game.logic.social.messages.FriendsPacket;
import org.virtue.game.logic.social.messages.IgnoresPacket;
import org.virtue.network.protocol.messages.GameMessage;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;
import org.virtue.network.protocol.packet.encoder.impl.chat.FriendEncoder;
import org.virtue.network.protocol.packet.encoder.impl.chat.FriendsChatEncoder;
import org.virtue.network.protocol.packet.encoder.impl.chat.IgnoreEncoder;
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
		this.player = p;
		this.protocolName = p.getAccount().getUsername().getAccountNameAsProtocol();
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
	
	public Rank getRank () {
		return player.getAccount().getRank();
	}
	
	public void sendGameMessage (String message, MessageOpcode type) {
		player.getPacketDispatcher().dispatchMessage(message, type);
	}
	
	public void sendFriendsChatPacket (FriendsChatPacket packet) {
		player.getAccount().getSession().getTransmitter().send(FriendsChatEncoder.class, packet);
	}
	
	public void sendFriendUpdate (Friend friend, boolean isNameChange) {
		player.getAccount().getSession().getTransmitter().send(FriendEncoder.class, new FriendsPacket(friend, false));
	}
	
	public void sendIgnoreUpdate (Ignore ignore, boolean isNameChange) {
		player.getAccount().getSession().getTransmitter().send(IgnoreEncoder.class, new IgnoresPacket(ignore, false));
	}
}
