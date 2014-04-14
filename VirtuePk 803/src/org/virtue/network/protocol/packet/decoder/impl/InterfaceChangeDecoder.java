package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.impl.InterfaceChangeHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

public class InterfaceChangeDecoder implements PacketDecoder<InterfaceChangeHandler> {

	@Override
	public InterfaceChangeHandler decodePacket(RS3PacketReader packet, Session session, int opcode) {
		packet.getLEShort();
		int oldHash = packet.getInt();
		int newHash = packet.getInt();
		
		System.out.println("Switched interfaces: oldID: "+(oldHash >> 16)+", oldPos: "+(oldHash & 0xffff)
				+", newID: "+(newHash >> 16)+", newPos: "+(newHash & 0xffff));
		return null;
	}

	@Override
	public int[] getPossiblePackets() {
		return new int[] { IncommingOpcodes.INTERFACE_CHANGE_PACKET };
	}

}
