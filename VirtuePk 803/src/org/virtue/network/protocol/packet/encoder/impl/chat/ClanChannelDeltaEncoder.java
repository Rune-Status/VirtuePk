package org.virtue.network.protocol.packet.encoder.impl.chat;

import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.game.logic.social.clans.ccdelta.ClanChannelDelta;
import org.virtue.game.logic.social.messages.ClanChannelDeltaPacket;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

/**
 * 
 * @author Virtue Development Team 2014 (c).
 */
public class ClanChannelDeltaEncoder implements PacketEncoder<ClanChannelDeltaPacket> {

	@Override
	public RS3PacketBuilder buildPacket(ClanChannelDeltaPacket node) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacketVarShort(OutgoingOpcodes.CLAN_CHANNEL_DELTA);
		buffer.put(node.isGuestUpdate() ? 0 : 1);
		buffer.putLong(node.getClanHash());
		buffer.putLong(node.getUpdateNumber());
		for (ClanChannelDelta delta : node.getDeltaNodes()) {
			buffer.put(delta.getTypeID());
			delta.packDelta(buffer);
		}
		buffer.put(0);//End delta
		buffer.endPacketVarShort();
		return buffer;
	}

}
