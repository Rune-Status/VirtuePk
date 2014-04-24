package org.virtue.network.protocol.packet.encoder.impl.chat;

import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.game.logic.social.messages.FriendsChatPacket;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class FriendsChatEncoder implements PacketEncoder<FriendsChatPacket> {

	@Override
	public RS3PacketBuilder buildPacket(FriendsChatPacket node) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		if (node.isFullUpdate()) {
			buffer.putPacketVarShort(OutgoingOpcodes.FRIENDS_CHANNEL_PACKET);
			buffer.putString(node.getOwnerName());
			buffer.put(0);//Since it's not handled on the client side, no point in using owner reply-to names
			buffer.putString(node.getChannelName());
			buffer.put(node.getKickReq().getID());
			buffer.put(node.getUsers().length);
		} else {
			buffer.putPacketVarByte(OutgoingOpcodes.FRIENDS_CHANNEL_UPDATE_PACKET);
		}
		for (FriendsChatPacket.User user : node.getUsers()) {
			packUser(user, buffer);
		}
		if (node.isFullUpdate()) {
			buffer.endPacketVarShort();
		} else {
			buffer.endPacketVarByte();
		}
		return buffer;
	}
	
	public void packUser (FriendsChatPacket.User user, RS3PacketBuilder buffer) {
		buffer.putString(user.getDisplayName());
		buffer.put(user.hasReplyName() ? 1 : 0);
		if (user.hasReplyName()) {
			buffer.putString(user.getReplyName());
		}
		buffer.putShort(user.getWorldNodeID());
		if (user.getRank() == null) {
			buffer.put(Byte.MIN_VALUE);
		} else {
			buffer.put(user.getRank().getID());
			buffer.putString(user.getWorldName());
		}
	}
	
}
