package org.virtue.game.logic.node.entity.player.update.masks;

import org.virtue.utility.Utilities;


/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 16, 2014
 */
public class GlowColor {
	
	private int color;
	private int delay;
	private int duration;

	public GlowColor(int color, int duration) {
		this.color = color;
		this.delay = 0;
		this.duration = duration;
	}

	public GlowColor(int color, int delay, int duration) {
		this.color = color;
		this.delay = delay;
		this.duration = duration;
	}

	public static GlowColor generateRandom() {
		return new GlowColor(Utilities.random(254) + (Utilities.random(254) << 8) + (Utilities.random(254) << 16) + (Utilities.random(254) << 24), 100);
	}

	public int getColor() {
		return color;
	}

	public int getDuration() {
		return duration;
	}

	public int getDelay() {
		return delay;
	}
}
