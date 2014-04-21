package org.virtue.game.logic.node.entity.player.update.blocks;

import org.virtue.game.logic.node.entity.Entity;
import org.virtue.game.logic.node.entity.player.update.UpdateBlock;
import org.virtue.network.protocol.packet.RS3PacketBuilder;

/**
 * @author Taylor
 * @version 1.0
 */
public class MovementTypeBlock extends UpdateBlock {

	@Override
	public int getMask() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBlockPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int appendToUpdateBlock(RS3PacketBuilder buffer, Entity player) {
		int movementType = 0;
		if (player.getUpdateArchive().getMovement().getNextWalkDirection() != -1) {
			movementType = 1;
		} else if (player.getUpdateArchive().getMovement().getNextRunDirection() != -1) {
			movementType = 2;
		}
		return 1;
	}

}
