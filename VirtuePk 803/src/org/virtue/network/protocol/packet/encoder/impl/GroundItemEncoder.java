package org.virtue.network.protocol.packet.encoder.impl;

import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.network.protocol.messages.GroundItemMessage;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

public class GroundItemEncoder implements PacketEncoder<GroundItemMessage> {

	@Override
	public RS3PacketBuilder buildPacket(GroundItemMessage node) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacket(OutgoingOpcodes.WORLD_TILE_PACKET);
		int localX = (node.getItem().getTile().getLocalX(node.getLastRegionTile()));
		int localY = (node.getItem().getTile().getLocalY(node.getLastRegionTile()));
		buffer.put(localX >> 3);
		buffer.putByteA(node.getItem().getTile().getPlane());
		buffer.putByteA(localY >> 3);
		int offsetX = localX % 8;
		int offsetY = localY % 8;
		//System.out.println("offsetX="+offsetX+", offsetY="+offsetY+", localX="+localX+", localY="+localY);
		//System.out.println("regionX="+(node.getItem().getTile().getRegionX() << 6)+", regionY="+(node.getItem().getTile().getRegionY() << 6));
		switch (node.getType()) {
		case CREATE:
			buffer.putPacket(OutgoingOpcodes.ADD_GROUND_ITEM);
			buffer.putLEShort(node.getItem().getId());
			buffer.putShortA(node.getItem().getAmount());			
			buffer.put((offsetX & 0x7) << 4 | offsetY & 0x7);
			//System.out.println("Adding item "+node.getItem().getId()+" at position x="+node.getItem().getTile().getX()+", y="+node.getItem().getTile().getY());
			break;
		case DESTROY:
			buffer.putPacket(OutgoingOpcodes.REMOVE_GROUND_ITEM_PACKET);
			buffer.putLEShortA(node.getItem().getId());
			buffer.putByteS((offsetX & 0x7) << 4 | offsetY & 0x7);
			//System.out.println("Removing item "+node.getItem().getId()+" at position x="+node.getItem().getTile().getX()+", y="+node.getItem().getTile().getY());
			break;
		}
		return buffer;
	}

}
