package org.virtue.game.logic.node.entity.player.update;

import java.util.List;

import org.virtue.game.logic.node.entity.Entity;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.update.blocks.AnimationBlock;
import org.virtue.game.logic.node.entity.player.update.blocks.GraphicsBlock;
import org.virtue.game.logic.node.entity.player.update.masks.Graphics;
import org.virtue.game.logic.node.entity.player.update.movement.Movement;
import org.virtue.game.logic.node.entity.player.update.ref.Animation;
import org.virtue.game.logic.node.entity.player.update.ref.Appearance;

/**
 * @author Taylor
 * @version 1.0
 */
public class UpdateBlockArchive {
	
	/**
	 * Represents a {@link List} of live blocks awaiting to be encoded.
	 */
	private UpdateBlock[] LIVE_BLOCKS = (UpdateBlock[]) new UpdateBlock[21];
	
	/**
	 * Represents the appearance.
	 */
	private Appearance appearance;	

	/**
	 * Represents the current animation.
	 */
	private Animation animation;
	
	/**
	 * Represents the graphics mask.
	 */
	private Graphics[] graphics = new Graphics[4];
	
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
		queue(clazz, -1);
	}
	
	public void queue(Class<?> clazz, int type) {
		UpdateBlock block = UpdateBlockProvider.forObject(clazz, type);
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
	 * @return The animation
	 */
	public Animation getAnimation() {
		return animation;
	}
	
	/**
	 * @param animation The animation to set
	 */
	private void setAnimation(Animation animation) {
		this.animation = animation;
	}
	
	/**
	 * Queues an animation.
	 * @param id The ID to queue.
	 * @param delay The delay.
	 */
	public void queueAnimation(int id, int delay) {
		setAnimation(new Animation(id, delay));		
		queue(AnimationBlock.class);
	}
	
	/**
	 * Queues an animation.
	 * @param id The ID to queue.
	 */
	public void queueAnimation(int id) {
		setAnimation(new Animation(id));
		queue(AnimationBlock.class);
	}
	
	public void queueGraphic (Graphics newGraphics) {
		int type = 4;
		if (graphics[0] == null) {
			type = 1;
		} else if (graphics[1] == null) {
			type = 2;
		} else if (graphics[2] == null) {
			type = 3;
		}
		queueGraphic(newGraphics, type);
	}

	/**
	 * Queues a graphics block.
	 * @param graphics The graphics.
	 */
	public void queueGraphic(Graphics graphics, int type) {
		setGraphics(type, graphics);
		queue(GraphicsBlock.class, type);
	}

	/**
	 * @return The graphics
	 */
	public Graphics[] getGraphics() {
		return graphics;
	}

	/**
	 * @param graphics The graphics to set
	 */
	private void setGraphics(int type, Graphics graphics) {
		this.graphics[type-1] = graphics;
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
