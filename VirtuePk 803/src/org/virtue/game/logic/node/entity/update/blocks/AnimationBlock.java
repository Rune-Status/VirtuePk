package org.virtue.game.logic.node.entity.update.blocks;

import org.virtue.game.config.UpdateMasks;
import org.virtue.game.logic.node.entity.Entity;
import org.virtue.game.logic.node.entity.update.UpdateBlock;
import org.virtue.game.logic.node.entity.update.ref.Animation;
import org.virtue.network.protocol.packet.RS3PacketBuilder;

public class AnimationBlock extends UpdateBlock {

	@Override
	public int getMask() {
		return UpdateMasks.EMOTE;
	}

	@Override
	public int getBlockPosition() {
		return 9;
	}

	@Override
	public int appendToUpdateBlock(RS3PacketBuilder buffer, Entity player) {
		Animation anim = player.getUpdateArchive().getAnimation();
		int start = buffer.getPosition();
		if (anim == null) {
			return 0;
		}
		for (int i = 0; i < 4; i++) {
			buffer.putBigSmart(anim.getId());
		}
		buffer.put(anim.getDelay());
		return buffer.getPosition() - start;
	}

}
