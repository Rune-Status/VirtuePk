package org.virtue.game.logic.node.object;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.region.Tile;

public class Portal extends RS3Object {
	
	private Tile destination;

	public Portal(int id, int rotation, int type, Tile tile, Tile destination) {
		super(id, rotation, type, tile);
		this.destination = destination;
	}
	
	public Portal(RS3Object object, Tile destination) {
		super(object.getId(), object.getRotation(), object.getType(), object.getTile());
		this.destination = destination;
	}

	@Override
	public void interact(Player player, ObjectOption option) {
		if (option.equals(ObjectOption.ONE) && getDefinition().getOption(option).equalsIgnoreCase("Enter")) {
			player.teleport(destination);
		}
	}
}
