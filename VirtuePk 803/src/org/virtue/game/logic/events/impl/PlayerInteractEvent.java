package org.virtue.game.logic.events.impl;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.PlayerOption;

/**
*
* @author Virtue Development Team 2014 (c).
*/
public class PlayerInteractEvent extends EntityInteractEvent {
	
	private final Player target;
	private final PlayerOption option;

	public PlayerInteractEvent(Player player, PlayerOption option) {
		super(player);
		this.target = player;
		this.option = option;
	}

	@Override
	public void run(Player player) {
		target.interact(player, option);
	}

}
