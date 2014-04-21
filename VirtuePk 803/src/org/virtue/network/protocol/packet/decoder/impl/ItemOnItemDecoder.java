package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.game.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.impl.ItemUseHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class ItemOnItemDecoder implements PacketDecoder<ItemUseHandler> {

    @Override
    public ItemUseHandler decodePacket(RS3PacketReader packet, Session session, int opcode) {
        ItemUseHandler handler = new ItemUseHandler();
        
        return handler;
    }

    @Override
    public int[] getPossiblePackets() {
        return new int[] { IncommingOpcodes.ITEM_ON_ITEM_PACKET };
    }
    
}
