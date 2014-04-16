package org.virtue.network.protocol.packet.encoder.impl;

import org.virtue.config.OutgoingOpcodes;
import org.virtue.game.node.entity.player.Viewport;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

public class MapSceneEncoder implements PacketEncoder<Viewport> {

	@Override
	public RS3PacketBuilder buildPacket(Viewport node) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacketVarShort(OutgoingOpcodes.STATIC_MAP_REGION_PACKET);
		boolean forceSend = true;
		if (node.isSendGPI()) {
			forceSend = true;
			node.loadGlobalPlayers(buffer);
			node.setSendGPI(false);
		}
		buffer.putByteS(0);//Map size
		buffer.put(9);//Region count
		buffer.putShort(node.getPlayer().getTile().getChunkY());
		buffer.putLEShortA(node.getPlayer().getTile().getChunkX());
		buffer.putByteC(forceSend ? 1 : 0);
		buffer.endPacketVarShort();
		return buffer;
	}
}
