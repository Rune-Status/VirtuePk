package org.virtue.network.protocol.packet.encoder.impl.r803;

import org.virtue.config.OutgoingOpcodes;
import org.virtue.network.messages.VarpMessage;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 8, 2014
 */
public class VarpEncoder implements PacketEncoder<VarpMessage> {


	@Override
	public RS3PacketBuilder buildPacket(VarpMessage context) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		int value = context.getValue();
		if (context.isCs2()) {
			if (value <= Byte.MIN_VALUE || value >= Byte.MAX_VALUE) {
				buffer.putPacket(81);//TODO: Update this to 803
				buffer.putInt(value);
				buffer.putShortA(context.getVarpId());
			} else {
				buffer.putPacket(111);
				buffer.putLEShortA(context.getVarpId());
				buffer.putByteS(value);
			}
		} else if (value <= Byte.MIN_VALUE || value >= Byte.MAX_VALUE) {
			buffer.putPacket(OutgoingOpcodes.LARGE_VARP_PACKET);
			buffer.putLEShortA(context.getVarpId());
			buffer.putInt(value);
		} else {
			buffer.putPacket(OutgoingOpcodes.SMALL_VARP_PACKET);
			buffer.putShortA(context.getVarpId());
			buffer.putByteA(value);
		}
		return buffer;
	}
}
