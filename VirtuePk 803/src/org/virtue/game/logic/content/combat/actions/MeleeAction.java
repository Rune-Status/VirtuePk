package org.virtue.game.logic.content.combat.actions;

import org.virtue.game.logic.content.combat.CombatAction;
import org.virtue.game.logic.item.Item;
import org.virtue.game.logic.node.entity.Entity;
import org.virtue.game.logic.node.entity.player.Player;


/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 17, 2014
 */
public class MeleeAction extends CombatAction {
	private Player player;
	
	private final Item weapon;
	private final Item shield;
	
	public MeleeAction(Player player, Entity victim) {
		super(player, victim);
		this.player = player;
		weapon = null;
		shield = null;
	}

	@Override
	public long getDelay() {
		return 1200;
	}

	@Override
	public int getMaximumHit() {
		return 100;
	}
	
	public int getAnimation() {
		if(weapon != null) {
			return 0;//weapon.def().equipDef().animations.attackAnimations[0];
		} else {
			return 0;//EquipmentDefinition.DEFAULT_ANIMATIONS.attackAnimations[0];
		}
	}

	@Override
	public long onAction() {
		return 0;
	}

}
