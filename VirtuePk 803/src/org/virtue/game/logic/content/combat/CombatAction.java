package org.virtue.game.logic.content.combat;

import org.virtue.game.logic.node.entity.Entity;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 17, 2014
 */
public abstract class CombatAction {

	public final Entity attacker;
	public final Entity victim;
	
	public CombatAction(Entity attacker, Entity victim) {
		this.attacker = attacker;
		this.victim = victim;
	}
	
	public abstract long getDelay();
	
	public abstract int getMaximumHit();
	
	public abstract long onAction();

}
