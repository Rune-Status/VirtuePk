package org.virtue.game.logic.events.impl;

import org.virtue.game.logic.events.CoordinateEvent;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.impl.Bank;
import org.virtue.game.logic.node.object.ObjectOption;
import org.virtue.game.logic.node.object.RS3Object;
import org.virtue.game.logic.region.Tile;

public class ObjectInteractEvent extends CoordinateEvent {
	
	private final RS3Object object;
	private final ObjectOption option;

	public ObjectInteractEvent(RS3Object object, ObjectOption option) {
		super(object.getTile(), object.getSizeX(), object.getSizeY());
		this.object = object;
		this.option = option;
	}

	@Override
	public void run(Player player) {
		player.getUpdateArchive().queueFaceDirection(object.getTile());
		object.interact(player, option);
	}

}
