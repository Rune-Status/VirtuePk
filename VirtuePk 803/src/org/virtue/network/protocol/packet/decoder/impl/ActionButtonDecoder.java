package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.impl.ActionButtonHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

public class ActionButtonDecoder implements PacketDecoder<ActionButtonHandler> {

	@Override
	public ActionButtonHandler decodePacket(RS3PacketReader packet, Session session, int opcode) {
		ActionButtonHandler handler = new ActionButtonHandler();
		
		int buttonID = packet.getLEShortA() & 0xFFFF;
		if(buttonID == 65535) {
			buttonID = -1;
		}
		int buttonID2 = packet.getShort() & 0xFFFF;
		if(buttonID2 == 65535) {
			buttonID2 = -1;
		}
		int interfaceID = packet.getInt();
		handler.putFlag("interface", interfaceID);
		handler.putFlag("slot1", buttonID);
		handler.putFlag("slot2", buttonID2);
		handler.putFlag("opcode", opcode);
		return handler;
	}

	@Override
	public int[] getPossiblePackets() {
		return new int[] { IncommingOpcodes.ACTION_BUTTON_1_PACKET, IncommingOpcodes.ACTION_BUTTON_2_PACKET,
				IncommingOpcodes.ACTION_BUTTON_3_PACKET, IncommingOpcodes.ACTION_BUTTON_4_PACKET,
				IncommingOpcodes.ACTION_BUTTON_5_PACKET, IncommingOpcodes.ACTION_BUTTON_6_PACKET,
				IncommingOpcodes.ACTION_BUTTON_7_PACKET, IncommingOpcodes.ACTION_BUTTON_8_PACKET,
				IncommingOpcodes.ACTION_BUTTON_9_PACKET, IncommingOpcodes.ACTION_BUTTON_10_PACKET };
	}

}
