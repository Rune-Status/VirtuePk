package org.virtue.network.protocol.packet.encoder.impl;

import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.network.protocol.messages.InterfaceSettingsMessage;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

public class InterfaceSettingsEncoder implements PacketEncoder<InterfaceSettingsMessage> {

	@Override
	public RS3PacketBuilder buildPacket(InterfaceSettingsMessage node) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		switch (node.getType()) {
		case SLOT:
			buffer.putPacket(OutgoingOpcodes.INTERFACE_SETTINGS_PACKET);
			buffer.putIntV1(node.getInterface());//Interface Component Hash
			buffer.putShortA(node.getToSlot());//The end slot
			buffer.putShortA(node.getFromSlot());//The start slot
			buffer.putIntV2(node.getSettings());//The settings hash
			break;
		case CLOSE:
			buffer.putPacket(OutgoingOpcodes.CLOSE_INTERFACE_PACKET);
			buffer.putIntV1(node.getInterface());//Parent component hash
			break;
		case STRING:
			buffer.putPacketVarShort(OutgoingOpcodes.COMPONENT_TEXT_PACKET);
			buffer.putIntV2(node.getInterface());
			buffer.putString(node.getComponentText());
			buffer.endPacketVarShort();
			break;
		case HIDECOMP:
			buffer.putPacket(OutgoingOpcodes.HIDE_COMPONENT_PACKET);
			buffer.putInt(node.getInterface());
			buffer.putByteC(node.getSettings());
			break;
		}		
		return buffer;
	}

}
