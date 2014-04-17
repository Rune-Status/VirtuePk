package org.virtue.game.logic.content.combat.ability;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 17, 2014
 */
public class AbilityShortcut implements Shortcut {
	private int id;
	
	public AbilityShortcut(int id) {
		this.id = id;
	}
	
	@Override
	public int id() {
		return id;
	}

	@Override
	public int itemId() {
		return -1;
	}
}
