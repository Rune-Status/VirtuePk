package org.virtue.game.logic.social.internal;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.social.messages.FriendsChatPacket;
import org.virtue.network.protocol.packet.encoder.impl.chat.FriendsChatEncoder;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class SocialUser {
	
	private Player player;
	
	public SocialUser (Player p) {
		this.player = p;
	}
	
	public String getDisplayName () {
		return player.getAccount().getUsername().getName();
	}
	
	public String getProtocolName () {
		return player.getAccount().getUsername().getAccountNameAsProtocol();
	}
	
	public void sendFriendsChatPacket (FriendsChatPacket packet) {
		player.getAccount().getSession().getTransmitter().send(FriendsChatEncoder.class, packet);
	}
}
