package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.game.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.impl.ItemOnObjectHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

/**
 * 
 * @author Virtue Development Team 2014 (c).
 */
public class ItemOnObjectDecoder implements PacketDecoder<ItemOnObjectHandler> {

	@Override
	public ItemOnObjectHandler decodePacket(RS3PacketReader packet, Session session, int opcode) {
		ItemOnObjectHandler handler = new ItemOnObjectHandler();
		int itemID = packet.getShortA() & 0xFFFF;
		if(itemID == 65535) {
			itemID = -1;
		}
		handler.putFlag("item", itemID);
		int slotID = packet.getLEShortA() & 0xFFFF;
		if(slotID == 65535) {
			slotID = -1;
		}
		handler.putFlag("slot", slotID);
		handler.putFlag("forceRun", (packet.getByteA() == 1));
		handler.putFlag("objectID", packet.getIntV2());
		handler.putFlag("baseX", packet.getShort());
		handler.putFlag("baseY", packet.getLEShort());
		handler.putFlag("interface", packet.getInt());
		return handler;
	}

	@Override
	public int[] getPossiblePackets() {
		return new int[] { IncommingOpcodes.ITEM_ON_OBJECT_PACKET };
	}

}
