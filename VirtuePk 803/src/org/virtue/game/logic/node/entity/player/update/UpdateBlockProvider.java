package org.virtue.game.logic.node.entity.player.update;

import java.util.ArrayList;
import java.util.List;

import org.virtue.game.logic.node.entity.player.update.blocks.AnimationBlock;
import org.virtue.game.logic.node.entity.player.update.blocks.AppearanceBlock;
import org.virtue.game.logic.node.entity.player.update.blocks.FaceDirectionBlock;
import org.virtue.game.logic.node.entity.player.update.blocks.FaceEntityBlock;
import org.virtue.game.logic.node.entity.player.update.blocks.GraphicsBlock;

/**
 * @author Taylor
 * @version 1.0
 */
public class UpdateBlockProvider {

	/**
	 * Represents a {@link List} of update blocks to be stored.
	 */
	private static final List<UpdateBlock> BLOCKS = new ArrayList<>();

	static {
		BLOCKS.add(new AppearanceBlock());
		BLOCKS.add(new AnimationBlock());
		BLOCKS.add(new GraphicsBlock(1));
		BLOCKS.add(new GraphicsBlock(2));
		BLOCKS.add(new GraphicsBlock(3));
		BLOCKS.add(new GraphicsBlock(4));
		BLOCKS.add(new GraphicsBlock(5));
		BLOCKS.add(new FaceEntityBlock());
		BLOCKS.add(new FaceDirectionBlock());
	}

	
	/**
	 * Returns the {@link UpdateBlock} corresponding to a given {@link Class} {@code Object}.
	 * @param clazz The class object of the block to get.
	 * @return
	 */	
	public static UpdateBlock forObject(Class<?> clazz) {
		return forObject(clazz, -1);
	}
	
	public static UpdateBlock forObject(Class<?> clazz, int type) {
		for (UpdateBlock block : BLOCKS) {
			if (block.getClass().equals(clazz)) {
				if (type == -1 || type == 1) {
					return (UpdateBlock) block;
				} else {
					type--;
				}
			} else {
				continue;
			}
		}
		return null;
	}
}
