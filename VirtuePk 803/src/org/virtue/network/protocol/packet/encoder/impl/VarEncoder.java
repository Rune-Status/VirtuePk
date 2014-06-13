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
		switch (context.getType()) {
		case PLAYER:
			packVarPlayer(context, buffer);
			break;
		case CLIENT:
			packVarClient(context, buffer);
			break;
		case CLIENT_STR:
			packVarClientString(context, buffer);
			break;
		case BIT:
			packVarBit(context, buffer);
			break;
		default:
			break;
		}
		return buffer;
	}
	
	private void packVarClient (VarMessage message, RS3PacketBuilder buffer) {
		int value = message.getIntValue();
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
	
	private void packVarClientString (VarMessage message, RS3PacketBuilder buffer) {
		String value = message.getStrValue();
		if (value.length() >= Byte.MAX_VALUE) {
			buffer.putPacketVarShort(OutgoingOpcodes.LARGE_VARC_STRING_PACKET);
			buffer.putString(value);
			buffer.putLEShortA(message.getVarID());
			buffer.endPacketVarShort();
		} else {
			buffer.putPacketVarByte(OutgoingOpcodes.SMALL_VARC_STRING_PACKET);
			buffer.putLEShortA(message.getVarID());
			buffer.putString(value);
			buffer.endPacketVarByte();
		}
	}
	
	private void packVarPlayer (VarMessage message, RS3PacketBuilder buffer) {
		int value = message.getIntValue();
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
	
	private void packVarBit (VarMessage message, RS3PacketBuilder buffer) {
		int value = message.getIntValue();
		if (value <= Byte.MIN_VALUE || value >= Byte.MAX_VALUE) {
			buffer.putPacket(OutgoingOpcodes.LARGE_VARBIT_PACKET);
			buffer.putLEShortA(message.getVarID());
			buffer.putIntV2(value);
		} else {
			buffer.putPacket(OutgoingOpcodes.SMALL_VARBIT_PACKET);
			buffer.putLEShort(message.getVarID());
			buffer.putByteS(value);
		}
	}
}
