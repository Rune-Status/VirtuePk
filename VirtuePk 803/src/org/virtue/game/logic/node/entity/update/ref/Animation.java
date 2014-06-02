package org.virtue.game.logic.node.entity.update.ref;

/**
 * @author Taylor
 * @version 1.0
 */
public class Animation {

	/**
	 * Represents the animation ID.
	 */
	private int id;
	
	/**
	 * Represents the animation delay.
	 */
	private int delay;

	/**
	 * Constructs a new {@code Animation.java}.
	 * @param id The ID.
	 * @param delay The delay.
	 */
	public Animation(int id, int delay) {
		this.id = id;
		this.delay = delay;
	}

	/**
	 * Constructs a new {@code Animation.java}.
	 * @param id The ID.
	 */
	public Animation(int id) {
		this(id, 0);
	}

	/**
	 * @return The id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id The id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return The delay
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * @param delay The delay to set
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}
}