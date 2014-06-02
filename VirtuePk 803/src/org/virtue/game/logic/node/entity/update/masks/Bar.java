package org.virtue.game.logic.node.entity.update.masks;

import org.virtue.game.logic.node.entity.Entity;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 16, 2014
 */
public enum Bar {


	/**
	 * The Default Hitpoints bar.
	 */
	HITPOINTS(0),

	/**
	 * The Adrenaline bar type.
	 */
	ADRENALINE(7);

	private int type;

	/**
	 * Constructs a new {@code Bar} instance.
	 * 
	 * @param mask
	 */
	private Bar(int mask) {
		this.type = mask;
	}

	/**
	 * @return the mask
	 */
	public int getType() {
		return type;
	}

	/**
	 * Gets the percentage of a specified bar.
	 * 
	 * @param p
	 *            The player to get for.
	 * @return The percentage of the bar.
	 */
	public int getPercentage(Entity e) {
		boolean adrenalineBar = this.equals(Bar.ADRENALINE);
		int percentage = 0;
		if (adrenalineBar) {
			percentage = e.getProperties().getAdrenaline() * 255 / 100;
		} else {
			int hitpoints = e.getHitpoints();
			int maxHp = e.getMaxHitpoints();
			percentage = maxHp == 0 ? 0 : (hitpoints * 255) / maxHp;
		}
		return percentage;
	}

	/**
	 * Gets the max percentage of the bar.
	 * 
	 * @return The max percent.
	 */
	public int getMaxPercentage() {
		return 255;
	}

	/**
	 * If we should display the current bar.
	 * 
	 * @param p
	 *            The player to check for.
	 * @return If we should display the bar.
	 */
	public boolean shouldDisplay(Entity e) {
		if (this.equals(Bar.ADRENALINE)) {
			return e.getProperties().getAdrenaline() > 0;
		}
		return true;
	}
	
}
