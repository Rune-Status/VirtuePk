package org.virtue.network.protocol.packet.encoder.impl.chat;

import org.virtue.game.logic.social.messages.ClanSettingsPacket;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

public class ClanSettingsEncoder implements PacketEncoder<ClanSettingsPacket> {

	@Override
	public RS3PacketBuilder buildPacket(ClanSettingsPacket node) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		return buffer;
	}

}
