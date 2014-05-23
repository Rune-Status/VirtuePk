package org.virtue.network.protocol.packet.encoder.impl.chat;

import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.game.logic.social.messages.IgnoresPacket;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

/**
  *
  * @author Virtue Development Team 2014 (c).
  */
public class IgnoreEncoder implements PacketEncoder<IgnoresPacket> {

	@Override
	public RS3PacketBuilder buildPacket(IgnoresPacket node) {
		RS3PacketBuilder builder = new RS3PacketBuilder();
		builder.putPacketVarShort(OutgoingOpcodes.IGNORES_PACKET);
		if (node.isFullUpdate()) {
			for (IgnoresPacket.Entry i : node.getIgnores()) {
				packIgnore(builder, i, false);
			}
		} else {
			packIgnore(builder, node.getIgnores()[0], node.isNameChange());
		}
		builder.endPacketVarShort();
		return builder;
	}
	
	private void packIgnore (RS3PacketBuilder builder, IgnoresPacket.Entry i, boolean isNameChange) {		
		builder.put((isNameChange ? 0x1 : 0) | (i.temporary ? 0x2 : 0));
		builder.putString(i.currentName);
		builder.putString(i.previousName);
		builder.putString(i.note);
	}

}
