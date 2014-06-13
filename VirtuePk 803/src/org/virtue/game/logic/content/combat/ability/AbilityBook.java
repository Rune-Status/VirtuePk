package org.virtue.game.logic.content.combat.ability;

import org.virtue.game.logic.content.combat.ability.impl.attackbook.Slice;
import org.virtue.game.logic.node.entity.player.Player;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 17, 2014
 */
public class AbilityBook {
	
	public static final Ability[] ATTACK_BOOK = new Ability[12];
	public static final Ability[] STRENGTH_BOOK = new Ability[12];
	public static final Ability[] RANGED_BOOK = new Ability[12];
	public static final Ability[] MAGE_BOOK = new Ability[12];
	public static final Ability[] DEFENCE_BOOK = new Ability[12];
	public static final Ability[] CONSTITUTION_BOOK = new Ability[4];

	
	static {
		ATTACK_BOOK[0] = new Slice();
	}
	
	public static final Ability[][] ABILITY_BOOK = new Ability[][] { ATTACK_BOOK, STRENGTH_BOOK, RANGED_BOOK, MAGE_BOOK, DEFENCE_BOOK, CONSTITUTION_BOOK };
	
	private Player player;
	
	public int book = 3; // TODO Change
	
	public AbilityBook(Player player) {
		this.player = player;
	}
	
	public Ability getAbility(int id) {
		return ABILITY_BOOK[book][id];
	}
}
