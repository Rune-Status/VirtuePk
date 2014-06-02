package org.virtue.network.protocol.handlers.commands;

import org.virtue.Constants;
import org.virtue.game.logic.World;
import org.virtue.game.logic.node.entity.npc.NPC;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.region.Tile;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;

public class SpawnNpc implements Command {

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		if (args.length != 3) {
			player.getPacketDispatcher().dispatchMessage("Invalid spawn command: requires 3 int paramaters", MessageOpcode.CONSOLE);
			return false;
		}
		int id, x, y;
		try {
			id = Integer.parseInt(args[0]);
			x = Integer.parseInt(args[1]);
			y = Integer.parseInt(args[2]);
		} catch (Exception ex) {
			player.getPacketDispatcher().dispatchMessage("Invalid spawn command: requires 3 int paramaters", MessageOpcode.CONSOLE);
			return false;
		}
		Tile spawnPosition = new Tile(x, y, 0);
		NPC npc = NPC.create(id, spawnPosition);
		World.getWorld().getNpcs().add(npc);
		Tile target = Tile.edit(spawnPosition, (byte) -10, (byte) 10, 0);
		npc.getUpdateArchive().getMovement().calculateWalkStepsInteract(target, Constants.MAX_WALK_STEPS, npc.getSize());
		return true;
	}

	@Override
	public String[] getPossibleSyntaxes() {
		return new String[] { "spawnnpc", "addnpc", "spawn", "npc" };
	}

}
