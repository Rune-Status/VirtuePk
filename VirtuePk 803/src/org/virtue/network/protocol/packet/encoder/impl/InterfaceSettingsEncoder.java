package org.virtue.network.protocol.packet.encoder.impl;

import org.virtue.config.OutgoingOpcodes;
import org.virtue.network.messages.InterfaceSettingsMessage;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

public class InterfaceSettingsEncoder implements PacketEncoder<InterfaceSettingsMessage> {

	@Override
	public RS3PacketBuilder buildPacket(InterfaceSettingsMessage node) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacket(OutgoingOpcodes.INTERFACE_SETTINGS_PACKET);
		buffer.putIntV1(node.getInterface());//Interface Component Hash
		buffer.putShortA(node.getToSlot());//The end slot
		buffer.putShortA(node.getFromSlot());//The start slot
		buffer.putIntV2(node.getSettings());//The settings hash
		return buffer;
	}

}
