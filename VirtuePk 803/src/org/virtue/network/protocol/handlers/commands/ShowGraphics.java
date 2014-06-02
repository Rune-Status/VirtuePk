package org.virtue.network.protocol.handlers.commands;

import org.virtue.cache.def.GraphicsDefinitionLoader;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.update.masks.Graphics;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class ShowGraphics implements Command {

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		if (!player.isInWorld()) {
			player.getPacketDispatcher().dispatchMessage("You must be logged into the game to run that command.", MessageOpcode.CONSOLE);
			return false;
		}
		int id = 0;
		if (args.length < 1) {
			player.getPacketDispatcher().dispatchMessage("Invalid graphics command: requires 1 int paramater", MessageOpcode.CONSOLE);
			return false;
		}
		try {
			id = Integer.parseInt(args[0]);
		} catch (Exception ex) {
			player.getPacketDispatcher().dispatchMessage("Invalid graphics command: requires 1 int paramater", MessageOpcode.CONSOLE);
			return false;
		}
		if (GraphicsDefinitionLoader.forId(id) == null) {
			player.getPacketDispatcher().dispatchMessage("Graphics "+id+" does not exist!", MessageOpcode.CONSOLE);
			return false;
		}
		player.getUpdateArchive().queueGraphic(new Graphics(id));
		player.getPacketDispatcher().dispatchMessage("Queueing graphics "+id, MessageOpcode.CONSOLE);
		return true;
	}

	@Override
	public String[] getPossibleSyntaxes() {
		return new String[] { "gfx", "graphics" };
	}
	
}
