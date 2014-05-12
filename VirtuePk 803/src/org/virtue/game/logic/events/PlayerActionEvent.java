package org.virtue.game.logic.events;

import org.virtue.game.logic.node.entity.player.Player;

public abstract class PlayerActionEvent {

	public abstract boolean start(Player player);
	
	public abstract boolean process(Player player);
	
	public abstract void stop(Player player);
}
