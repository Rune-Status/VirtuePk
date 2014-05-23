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
        	int heap = packet.getByteA();
        	int ping = packet.getShortA();
        	int fps = packet.getByteC();
        	//System.out.println("Client specs: var1="+var1+", ping="+ping+", fps="+fps);
        }
        return null;
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int[] getPossiblePackets() {
        return new int[] { IncommingOpcodes.EVENT_MOUSE_MOVE, IncommingOpcodes.EVENT_APPLET_FOCUS,
        		IncommingOpcodes.EVENT_KEYBOARD, IncommingOpcodes.EVENT_CAMERA_POSITION,
        		IncommingOpcodes.EVENT_MOUSE_CLICK, IncommingOpcodes.CLIENT_SPECS_PACKET, 
				IncommingOpcodes.PACKET_RECEIVE_COUNT, IncommingOpcodes.ANOTHER_MOUSE_PACKET,
				IncommingOpcodes.ANOTHER_CLICK_PACKET, IncommingOpcodes.REGION_LOADING_COMPLETED_PACKET };
    }
    
}
