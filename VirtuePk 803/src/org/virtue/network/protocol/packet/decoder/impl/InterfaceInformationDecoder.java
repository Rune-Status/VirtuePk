package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.game.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.impl.InterfaceLayoutHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;
import org.virtue.network.session.impl.WorldSession;

public class InterfaceInformationDecoder implements PacketDecoder<InterfaceLayoutHandler> {

	@Override
	public InterfaceLayoutHandler decodePacket(RS3PacketReader packet, Session session, int opcode) { 
		if (opcode == IncommingOpcodes.CLOSE_INTERFACE_PACKET) {
			((WorldSession) session).getPlayer().getInterfaces().closeTopInterface();
			return null;
		}
		InterfaceLayoutHandler handler = new InterfaceLayoutHandler();
		handler.putFlag("isFinished", packet.get() == 1);		
		while (packet.remaining() > 0) {
			int key = packet.getUnsignedShort();
			int value = packet.getInt();
			((WorldSession) session).getPlayer().getInterfaces().getScreen().setLayoutKey(key, value);
			//System.out.println("Key="+key+", value="+value);
		}
		return handler;
	}

	@Override
	public int[] getPossiblePackets() {
		return new int[] { IncommingOpcodes.INTERFACE_LAYOUT_PACKET, IncommingOpcodes.CLOSE_INTERFACE_PACKET };
	}

}
