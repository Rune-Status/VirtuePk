package org.virtue.network.protocol.packet.encoder.impl;

import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.network.protocol.messages.VarMessage;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 8, 2014
 */
public class VarEncoder implements PacketEncoder<VarMessage> {


	@Override
	public RS3PacketBuilder buildPacket(VarMessage context) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		if (context.isVarClient()) {
			packVarClient(context, buffer);
			/*if (value <= Byte.MIN_VALUE || value >= Byte.MAX_VALUE) {
				buffer.putPacket(OutgoingOpcodes.LARGE_VARBIT_PACKET);
				buffer.putLEShortA(context.getVarpId());
				buffer.putIntV2(value);
			} else {
				buffer.putPacket(OutgoingOpcodes.SMALL_VARBIT_PACKET);
				buffer.putShort(context.getVarpId());
				buffer.putByteS(value);
			}*/
		} else {
			packVarPlayer(context, buffer);
		}
		return buffer;
	}
	
	private void packVarClient (VarMessage message, RS3PacketBuilder buffer) {
		int value = message.getValue();
		if (value <= Byte.MIN_VALUE || value >= Byte.MAX_VALUE) {
			buffer.putPacket(OutgoingOpcodes.LARGE_VARC_PACKET);
			buffer.putLEShortA(message.getVarID());
			buffer.putInt(value);
		} else {
			buffer.putPacket(OutgoingOpcodes.SMALL_VARC_PACKET);
			buffer.putByteC(value);
			buffer.putShortA(message.getVarID());
		}
	}
	
	private void packVarPlayer (VarMessage message, RS3PacketBuilder buffer) {
		int value = message.getValue();
		if (value <= Byte.MIN_VALUE || value >= Byte.MAX_VALUE) {
			buffer.putPacket(OutgoingOpcodes.LARGE_VARP_PACKET);
			buffer.putLEShortA(message.getVarID());
			buffer.putInt(value);
		} else {
			buffer.putPacket(OutgoingOpcodes.SMALL_VARP_PACKET);
			buffer.putShortA(message.getVarID());
			buffer.putByteA(value);
		}
	}
}
