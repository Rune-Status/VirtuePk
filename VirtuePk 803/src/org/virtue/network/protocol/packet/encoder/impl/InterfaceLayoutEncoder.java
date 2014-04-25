package org.virtue.network.protocol.packet.encoder.impl;

import java.util.Map.Entry;

import org.virtue.game.logic.node.entity.player.screen.ClientScreen;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

public class InterfaceLayoutEncoder implements PacketEncoder<ClientScreen> {

	@Override
	public RS3PacketBuilder buildPacket(ClientScreen node) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacketVarShort(2);
		buffer.put(1);//If 1, this is the only layout config packet
		for (Entry<Integer, Object> layoutConfig : node.getLayoutInfo().entrySet()) {
			buffer.putShort(layoutConfig.getKey());
			buffer.putInt((Integer) layoutConfig.getValue());
			//System.out.println("key: "+layoutConfig.getKey()+", value="+layoutConfig.getValue());
		}
		/*int[] configVals = node.getNisInit();
		for (int i=0;i<configVals.length;i++) {
			if (configVals[i] != 0) {
				buffer.putShort(i);
				buffer.putInt(configVals[i]);
			}
		}*/
		buffer.endPacketVarShort();
		return buffer;
	}

}
