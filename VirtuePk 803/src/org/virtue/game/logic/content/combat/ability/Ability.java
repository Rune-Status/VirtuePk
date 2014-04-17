package org.virtue.game.logic.content.combat.ability;

import org.virtue.game.logic.node.entity.player.Player;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 17, 2014
 */
public interface Ability {
	
	public static enum AbilityType {
		
		BASIC(0, 0),
		THRESHOLD(50, 15),
		ULTIMATE(100, 100);
		
	public final int adrenalineRequirement;
	public final int adrenalineUsage;
		
	private AbilityType(int adrenalineRequirement, int adrenalineUsage) {
			this.adrenalineRequirement = adrenalineRequirement;
			this.adrenalineUsage = adrenalineUsage;
		}
	}
	
	AbilityType type();
	
	int interval();
	
	Shortcut shortcut();
	
	void activate(Player player);
}
