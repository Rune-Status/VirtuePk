package org.virtue.game.logic.events.impl;

import org.virtue.game.logic.events.CoordinateEvent;
import org.virtue.game.logic.item.Item;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.object.RS3Object;
import org.virtue.game.logic.region.Tile;

/**
 * 
 * @author Virtue Development Team 2014 (c).
 */
public class ItemOnObjectEvent extends CoordinateEvent {
	
	private final RS3Object object;
	private final Item item;

	public ItemOnObjectEvent(RS3Object object, Item item) {
		super(object.getTile(), object.getSizeX(), object.getSizeY());
		this.object = object;
		this.item = item;
	}

	@Override
	public void run(Player player) {
		player.getUpdateArchive().queueFaceDirection(object.getTile());
		object.useItem(player, item);
	}

}
