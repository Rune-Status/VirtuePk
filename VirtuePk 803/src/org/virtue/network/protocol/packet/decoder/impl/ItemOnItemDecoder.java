package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.game.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.impl.ItemOnItemHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class ItemOnItemDecoder implements PacketDecoder<ItemOnItemHandler> {

    @Override
    public ItemOnItemHandler decodePacket(RS3PacketReader packet, Session session, int opcode) {
        ItemOnItemHandler handler = new ItemOnItemHandler();
        handler.putFlag("prevItem", packet.getLEShortA());
        handler.putFlag("prevCompHash", packet.getLEInt());
        handler.putFlag("compHash", packet.getIntV1());
        handler.putFlag("prevSlot", packet.getLEShort());
        handler.putFlag("slot", packet.getLEShort());
        handler.putFlag("item", packet.getShortA());        
        return handler;
    }

    @Override
    public int[] getPossiblePackets() {
        return new int[] { IncommingOpcodes.ITEM_ON_ITEM_PACKET };
    }
    
}
