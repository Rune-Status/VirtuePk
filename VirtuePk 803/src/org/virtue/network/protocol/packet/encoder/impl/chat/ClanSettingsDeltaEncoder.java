package org.virtue.network.protocol.packet.encoder.impl.chat;

import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.game.logic.social.clans.csdelta.ClanSettingsDelta;
import org.virtue.game.logic.social.messages.ClanSettingsDeltaPacket;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

/**
 * 
 * @author Virtue Development Team 2014 (c).
 */
public class ClanSettingsDeltaEncoder implements PacketEncoder<ClanSettingsDeltaPacket> {

	@Override
	public RS3PacketBuilder buildPacket(ClanSettingsDeltaPacket node) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacketVarShort(OutgoingOpcodes.CLAN_SETTINGS_DELTA);
		buffer.put(node.isGuestUpdate() ? 0 : 1);
		buffer.putLong(0L);//owner (doesn't seem to be used)
		buffer.putInt(node.getUpdateNumber());
		for (ClanSettingsDelta delta : node.getDeltaNodes()) {
			if (delta.getTypeID() <= 0) {
				continue;
			}
			buffer.put(delta.getTypeID());
			delta.packDelta(buffer);
		}
		buffer.put(0);//End delta
		buffer.endPacketVarShort();
		return buffer;
	}

}
