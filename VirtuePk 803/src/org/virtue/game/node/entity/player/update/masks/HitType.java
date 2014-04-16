package org.virtue.game.node.entity.player.update.masks;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 16, 2014
 */
public enum HitType {
	MISSED(8), REGULAR_DAMAGE(3), MELEE_DAMAGE(48), RANGE_DAMAGE(49), MAGIC_DAMAGE(50), REFLECTED_DAMAGE(4), ABSORB_DAMAGE(5), POISON_DAMAGE(6), DESEASE_DAMAGE(7), HEALED_DAMAGE(9), CANNON_DAMAGE(13), INSTANT_KILL(54), DOUBLE_HIT(32767), HIDDEN_HIT(32766);
	private int hitMask;

	/**
	 * Constructs a new {@code HitType} instance.
	 * 
	 * @param hitMask
	 *            The hit mask id.
	 */
	private HitType(int hitMask) {
		this.hitMask = hitMask;
	}

	/**
	 * Gets the hit mask id.
	 * 
	 * @return The hit mask id.
	 */
	public int getCode() {
		return hitMask;
	}
}
