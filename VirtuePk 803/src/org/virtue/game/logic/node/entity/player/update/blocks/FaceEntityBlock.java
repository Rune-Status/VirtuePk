package org.virtue.game.logic.node.entity.player.update.blocks;

import org.virtue.game.logic.node.entity.Entity;
import org.virtue.game.logic.node.entity.npc.NPC;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.update.UpdateBlock;
import org.virtue.network.protocol.packet.RS3PacketBuilder;

/**
 * @author Taylor
 * @version 1.0
 */
public class FaceEntityBlock extends UpdateBlock {

	@Override
	public int getMask() {
		return 0x2;
	}

	@Override
	public int getBlockPosition() {
		return 4;
	}

	@Override
	public int appendToUpdateBlock(RS3PacketBuilder buffer, Entity player) {
		int index = -1;
		if (player.getUpdateArchive().getFaceEntity() instanceof Player) {
			index = player.getUpdateArchive().getFaceEntity().getIndex() + 32768;
		} else if (player.getUpdateArchive().getFaceEntity() instanceof NPC) {
			index = player.getUpdateArchive().getFaceEntity().getIndex();
		}
		//int index = player.getUpdateArchive().getFaceEntity() == null ? -1 : player.getUpdateArchive().getFaceEntity().getIndex() + 32768;
		buffer.putLEShort(index);
		return 2;
	}

}
