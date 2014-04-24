package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.game.config.IncommingOpcodes;
import org.virtue.game.logic.node.interfaces.RSInterface;
import org.virtue.network.protocol.handlers.impl.SwitchInterfaceHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

public class SwitchInterfaceDecoder implements PacketDecoder<SwitchInterfaceHandler> {

	@Override
	public SwitchInterfaceHandler decodePacket(RS3PacketReader packet, Session session, int opcode) {
		SwitchInterfaceHandler handler = new SwitchInterfaceHandler();
		handler.putFlag("newSlot", packet.getLEShort());
		handler.putFlag("oldHash", packet.getInt());
		handler.putFlag("newHash", packet.getInt());
		handler.putFlag("oldSlot", packet.getShortA());
		handler.putFlag("newComponent", packet.getShort());
		handler.putFlag("oldComponent", packet.getLEShort());
		
		return handler;
	}

	@Override
	public int[] getPossiblePackets() {
		return new int[] { IncommingOpcodes.INTERFACE_CHANGE_PACKET };
	}

}
