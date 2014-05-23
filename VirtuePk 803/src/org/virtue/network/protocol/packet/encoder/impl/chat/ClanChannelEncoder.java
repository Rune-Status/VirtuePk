package org.virtue.network.protocol.packet.encoder.impl.chat;

import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.game.logic.social.messages.ClanChannelPacket;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

/**
 * 
 * @author Virtue Development Team 2014 (c).
 */
public class ClanChannelEncoder implements PacketEncoder<ClanChannelPacket> {

	@Override
	public RS3PacketBuilder buildPacket(ClanChannelPacket node) {
		int version = 2;
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacketVarShort(OutgoingOpcodes.CLAN_CHANNEL_FULL);
		buffer.put(node.isGuestUpdate() ? 0 : 1);
		
		int flags = 0;
		flags |= 0x2;//Use display names
		flags |= 0x4;//Includes version number
		buffer.put(flags);
		buffer.put(version);
		buffer.putLong(node.getClanHash());
		buffer.putLong(node.getUpdateNumber());
		buffer.putString(node.getClanName());
		buffer.put(0);//Not used
		buffer.put(node.getMinKick().getID());
		buffer.put(node.getMinTalk().getID());
		
		buffer.putShort(node.getUsers().length);		
		for (ClanChannelPacket.User user : node.getUsers()) {
			packUser(buffer, user);
		}		
		buffer.endPacketVarShort();
		return buffer;
	}
	
	private void packUser (RS3PacketBuilder buffer, ClanChannelPacket.User user) {
		buffer.putString(user.getDisplayName());
		buffer.put(user.getRank().getID());
		buffer.putShort(user.getWorldNodeID());
	}

}
