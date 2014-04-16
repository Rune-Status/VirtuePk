package org.virtue.game.node.entity.player.update;

import java.util.List;

import org.virtue.game.node.entity.Entity;
import org.virtue.game.node.entity.player.Player;
import org.virtue.game.node.entity.player.update.movement.Movement;
import org.virtue.game.node.entity.player.update.ref.Appearance;

/**
 * @author Taylor
 * @version 1.0
 */
public class UpdateBlockArchive {
	
	/**
	 * Represents a {@link List} of live blocks awaiting to be encoded.
	 */
	private UpdateBlock[] LIVE_BLOCKS = (UpdateBlock[]) new UpdateBlock[15];
	
	/**
	 * Represents the appearance.
	 */
	private Appearance appearance;
	
	/**
	 * Represents the movement flags.
	 */
	private Movement movement;
	
	/**
	 * Represents the player.
	 */
	private Entity entity;

	/**
	 * Constructs a new {@code UpdateBlockArchive.java}.
	 * @param entity The entity.
	 */
	public UpdateBlockArchive(Entity entity) {
		this.entity = entity;
		if (entity instanceof Player) {
			appearance = new Appearance((Player) entity);
		}
		movement = new Movement(entity);
	}
	
	/**
	 * Queues an {@link UpdateBlock} to this {@link UpdateBlockArchive}.
	 * @param clazz The reference class.
	 */
	public void queue(Class<?> clazz) {
		UpdateBlock block = UpdateBlockProvider.forObject(clazz);
		synchronized (this) {
			LIVE_BLOCKS[block.getBlockPosition()] = block;
		}
	}

	/**
	 * @return The appearance
	 */
	public Appearance getAppearance() {
		return appearance;
	}

	/**
	 * @param appearance The appearance to set
	 */
	public void setAppearance(Appearance appearance) {
		this.appearance = appearance;
	}

	/**
	 * @return If the blocks are flagged.
	 */
	public boolean flagged() {
		for (int block = 0; block < LIVE_BLOCKS.length; block++) {
			if (LIVE_BLOCKS[block] != null) {
				return true;
			} else {
				continue;
			}
		}
		return false;
	}

	/**
	 * @return The movement
	 */
	public Movement getMovement() {
		return movement;
	}

	/**
	 * @param movement The movement to set
	 */
	public void setMovement(Movement movement) {
		this.movement = movement;
	}

	/**
	 * @return The blocks.
	 */
	public UpdateBlock[] getLiveBlocks() {
		return LIVE_BLOCKS;
	}

	/**
	 * Resets the block masks.
	 */
	public void reset() {
		for (int block = 0; block < LIVE_BLOCKS.length; block++) {
			LIVE_BLOCKS[block] = null;
		}
	}

	/**
	 * @return The player
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * @param player The player to set
	 */
	public void setEntity(Player player) {
		this.entity = player;
	}
}
