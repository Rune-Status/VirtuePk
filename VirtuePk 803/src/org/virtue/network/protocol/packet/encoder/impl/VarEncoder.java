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
			if (context.isVarBit()) {
				packVarPlayerBit(context, buffer);
			} else {
				packVarPlayer(context, buffer);
			}
			break;
		case CLIENT:
			if (context.isStringValue()) {
				packVarClientString(context, buffer);
			} else if (context.isVarBit()) {
				packVarClientBit(context, buffer);
			} else {
				packVarClient(context, buffer);
			}
			break;
		default:
			System.err.print("Could not encode var - type "+context.getType()+" is not currently supported.");
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
	
	private void packVarClientBit (VarMessage message, RS3PacketBuilder buffer) {
		int value = message.getIntValue();
		if (value <= Byte.MIN_VALUE || value >= Byte.MAX_VALUE) {
			buffer.putPacket(OutgoingOpcodes.LARGE_VARC_BIT_PACKET);
			buffer.putLEShortA(message.getVarID());
			buffer.putIntV1(value);
		} else {
			buffer.putPacket(OutgoingOpcodes.SMALL_VARC_BIT_PACKET);
			buffer.putByteA(value);
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
	
	private void packVarPlayerBit (VarMessage message, RS3PacketBuilder buffer) {
		int value = message.getIntValue();
		if (value <= Byte.MIN_VALUE || value >= Byte.MAX_VALUE) {
			buffer.putPacket(OutgoingOpcodes.LARGE_VARPBIT_PACKET);
			buffer.putLEShortA(message.getVarID());
			buffer.putIntV2(value);
		} else {
			buffer.putPacket(OutgoingOpcodes.SMALL_VARPBIT_PACKET);
			buffer.putLEShort(message.getVarID());
			buffer.putByteS(value);
		}
	}
}
