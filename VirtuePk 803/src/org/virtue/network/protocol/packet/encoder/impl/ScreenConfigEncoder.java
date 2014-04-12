package org.virtue.network.protocol.packet.encoder.impl;

import org.virtue.game.node.entity.player.screen.ClientScreen;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

public class ScreenConfigEncoder implements PacketEncoder<ClientScreen> {

	@Override
	public RS3PacketBuilder buildPacket(ClientScreen node) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacketVarShort(2);
		buffer.put(1);//Whether there is another screen config packet
		int[] configVals = node.getNisInit();
		for (int i=0;i<configVals.length;i++) {
			if (configVals[i] != 0) {
				buffer.putShort(i);
				buffer.putInt(configVals[i]);
			}
		}
		buffer.endPacketVarShort();
		return buffer;
	}

}
