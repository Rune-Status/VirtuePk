package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.logic.World;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.object.RS3Object;
import org.virtue.game.logic.region.Region;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;

public class SpawnObject implements Command {

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		int objectID;
		if (args.length < 1) {
			player.getPacketDispatcher().dispatchMessage("Invalid object spawn command: requires 1 int paramater", MessageOpcode.CONSOLE);
			return false;
		}
		try {
			objectID = Integer.parseInt(args[0]);
		} catch (NumberFormatException ex) {
			player.getPacketDispatcher().dispatchMessage("Invalid object spawn command: requires 1 int paramater", MessageOpcode.CONSOLE);
			return false;
		}
		RS3Object object = new RS3Object(objectID, 0, 10, player.getTile());
		if (object.getDefinition() == null) {
			player.getPacketDispatcher().dispatchMessage("Invalid object spawn command: object does not exist!", MessageOpcode.CONSOLE);
			return false;
		}
		Region region = World.getWorld().getRegionManager().getRegionByID(player.getTile().getRegionID());
		region.spawnObject(object);
		return true;
	}

	@Override
	public String[] getPossibleSyntaxes() {
		return new String[] { "object", "obj" };
	}

}
