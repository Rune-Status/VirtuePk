package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.game.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.impl.ClientInfoHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 20, 2014
 */
public class ClientInfoDecoder implements PacketDecoder<ClientInfoHandler> {

    @Override
    public ClientInfoHandler decodePacket(RS3PacketReader packet, Session session, int opcode) {
        if (opcode == IncommingOpcodes.CLIENT_SPECS_PACKET) {
        	int var1 = packet.getByteA();
        	int ping = packet.getShortA();
        	int fps = packet.getByteC();
        	//System.out.println("Client specs: var1="+var1+", ping="+ping+", fps="+fps);
        }
        return null;
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int[] getPossiblePackets() {
        return new int[] { IncommingOpcodes.MOUSE_MOVEMENT_PACKET, IncommingOpcodes.WINDOW_FOCUS_PACKET,
        		IncommingOpcodes.KEY_TYPED_PACKET, IncommingOpcodes.CAMERA_MOVEMENT_PACKET,
        		IncommingOpcodes.CLICK_PACKET, IncommingOpcodes.CLIENT_SPECS_PACKET };
    }
    
}
