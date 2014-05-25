package org.virtue.network.protocol.packet.encoder.impl.chat;

import org.virtue.game.logic.social.messages.ClanSettingsDeltaPacket;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

public class ClanSettingsDeltaEncoder implements PacketEncoder<ClanSettingsDeltaPacket> {

	@Override
	public RS3PacketBuilder buildPacket(ClanSettingsDeltaPacket node) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		return buffer;
	}

}
