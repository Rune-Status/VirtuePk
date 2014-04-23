package org.virtue.game.logic.node.entity.player.update.blocks;

import org.virtue.game.logic.node.entity.Entity;
import org.virtue.game.logic.node.entity.player.update.UpdateBlock;
import org.virtue.game.logic.region.Tile;
import org.virtue.network.protocol.packet.RS3PacketBuilder;

/**
 * @author Taylor
 * @version 1.0
 */
public class FaceDirectionBlock extends UpdateBlock {

	@Override
	public int getMask() {
		return 0x20;
	}

	@Override
	public int getBlockPosition() {
		return 17;
	}

	@Override
	public int appendToUpdateBlock(RS3PacketBuilder buffer, Entity player) {
		Tile dir = player.getUpdateArchive().getFaceDirection();
		int direction = ((int) (Math.atan2(player.getTile().getX() - dir.getX(), player.getTile().getY() - dir.getY()) * 2607.5945876176133)) & 0x3fff;
		buffer.putLEShort(direction);
		return 2;
	}

}
