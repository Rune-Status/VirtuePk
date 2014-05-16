package org.virtue.network.protocol.packet.encoder.impl;

import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.network.protocol.messages.ObjectMessage;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

public class ObjectUpdateEncoder implements PacketEncoder<ObjectMessage> {

	@Override
	public RS3PacketBuilder buildPacket(ObjectMessage node) {
		//System.out.println("Encoding object update for "+node.getObject().getId());
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacket(OutgoingOpcodes.WORLD_TILE_PACKET);
		int localX = (node.getObject().getTile().getLocalX(node.getLastRegionTile()));
		int localY = (node.getObject().getTile().getLocalY(node.getLastRegionTile()));
		buffer.put(localX >> 3);
		buffer.putByteA(node.getObject().getTile().getPlane());
		buffer.putByteA(localY >> 3);
		int offsetX = localX % 8;
		int offsetY = localY % 8;
		switch (node.getType()) {
		case CREATE:
			buffer.putPacket(OutgoingOpcodes.SPAWN_OBJECT_PACKET);	
			buffer.putByteS((offsetX & 0x7) << 4 | offsetY & 0x7);
			buffer.putIntV1(node.getObject().getId());
			buffer.putByteC((node.getObject().getType() << 2) + (node.getObject().getRotation() & 0x3));		
			System.out.println("Adding object "+node.getObject().getId()+" of type "+node.getObject().getType()+" at position x="+node.getObject().getTile().getX()+", y="+node.getObject().getTile().getY());
			break;
		case DESTROY:
			buffer.putPacket(OutgoingOpcodes.DESTROY_OBJECT_PACKET);
			buffer.put((node.getObject().getType() << 2) + (node.getObject().getRotation() & 0x3));
			buffer.put((offsetX & 0x7) << 4 | offsetY & 0x7);
			System.out.println("Removing object "+node.getObject().getId()+" of type "+node.getObject().getType()+" at position x="+node.getObject().getTile().getX()+", y="+node.getObject().getTile().getY());
			break;
		}
		return buffer;
	}

}
