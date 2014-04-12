/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.impl.ClientInfoHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

/**
 *
 * @author Francis
 */
public class ClientInfoDecoder implements PacketDecoder<ClientInfoHandler> {

    @Override
    public ClientInfoHandler decodePacket(RS3PacketReader packet, Session session, int opcode) {
        //Does nothing...
        return null;
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int[] getPossiblePackets() {
        return new int[] { IncommingOpcodes.MOUSE_MOVEMENT_PACKET, IncommingOpcodes.WINDOW_FOCUS_PACKET,
        		IncommingOpcodes.KEY_TYPED_PACKET, IncommingOpcodes.CAMERA_MOVEMENT_PACKET,
        		IncommingOpcodes.CLICK_PACKET };
    }
    
}
