package org.virtue.network.protocol.render.update;

import java.util.ArrayList;
import java.util.List;

import org.virtue.network.protocol.render.update.blocks.AppearanceBlock;

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
	}
	
	/**
	 * Returns the {@link UpdateBlock} corresponding to a given {@link Class} {@code Object}.
	 * @param clazz The class object of the block to get.
	 * @return
	 */
	public static UpdateBlock forObject(Class<?> clazz) {
		for (UpdateBlock block : BLOCKS)
			if (block.getClass().equals(clazz))
				return (UpdateBlock) block;
			else
				continue;
		return null;
	}
}
