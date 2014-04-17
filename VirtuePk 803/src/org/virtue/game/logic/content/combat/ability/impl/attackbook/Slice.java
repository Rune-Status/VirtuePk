package org.virtue.game.logic.content.combat.ability.impl.attackbook;

import org.virtue.game.logic.content.combat.ability.Ability;
import org.virtue.game.logic.content.combat.ability.AbilityShortcut;
import org.virtue.game.logic.content.combat.ability.Shortcut;
import org.virtue.game.logic.node.entity.player.Player;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 17, 2014
 */
public class Slice implements Ability {

	@Override
	public AbilityType type() {
		return AbilityType.BASIC;
	}

	@Override
	public int interval() {
		return 3;
	}

	@Override
	public Shortcut shortcut() {
		return new AbilityShortcut(17);
	}

	@Override
	public void activate(Player player) {
	}

}
