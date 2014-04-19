package org.virtue.network.protocol.packet.encoder.impl;

import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.game.logic.social.Ignore;
import org.virtue.game.logic.social.messages.IgnoresMessage;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

public class IgnoreEncoder implements PacketEncoder<IgnoresMessage> {

	@Override
	public RS3PacketBuilder buildPacket(IgnoresMessage node) {
		RS3PacketBuilder builder = new RS3PacketBuilder();
		builder.putPacketVarShort(OutgoingOpcodes.IGNORES_PACKET);
		if (node.isFullUpdate()) {
			for (Ignore i : node.getIgnores()) {
				packIgnore(builder, i, false);
			}
		} else {
			packIgnore(builder, node.getIgnores()[0], node.isNameChange());
		}
		builder.endPacketVarShort();
		return builder;
	}
	
	private void packIgnore (RS3PacketBuilder builder, Ignore i, boolean isNameChange) {
		builder.put((isNameChange ? 1 : 0));//TODO: There's another flag here (0x2). Figure out what it does...
		builder.putString(i.getName());
		builder.putString(i.getPreviousName());
		builder.putString(i.getNote());
	}

}
