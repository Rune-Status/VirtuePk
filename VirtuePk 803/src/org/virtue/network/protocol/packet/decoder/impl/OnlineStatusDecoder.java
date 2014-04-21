package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.game.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.impl.OnlineStatusHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 20, 2014
 */
public class OnlineStatusDecoder implements PacketDecoder<OnlineStatusHandler> {

    @Override
    public OnlineStatusHandler decodePacket(RS3PacketReader packet, Session session, int opcode) {
        OnlineStatusHandler handler = new OnlineStatusHandler();
        handler.putFlag("publicStatus", packet.get());
        handler.putFlag("onlineStatus", packet.get());
        handler.putFlag("tradeStatus", packet.get());
        return handler;
    }

    @Override
    public int[] getPossiblePackets() {
        return new int[] { IncommingOpcodes.ONLINE_STATUS_PACKET };
    }
    
}
