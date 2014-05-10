package org.virtue.network.protocol.handlers.commands;

import org.virtue.cache.def.AnimationDefinitionLoader;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.network.protocol.messages.GameMessage;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class ShowAnimation implements Command {

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		if (!player.isInWorld()) {
			player.getPacketDispatcher().dispatchMessage("You must be logged into the game to run that command.", GameMessage.MessageOpcode.CONSOLE);
			return false;
		}
		int id = 0;
		if (args.length < 1) {
			player.getPacketDispatcher().dispatchMessage("Invalid animation command: requires 1 int paramater", GameMessage.MessageOpcode.CONSOLE);
			return false;
		}
		try {
			id = Integer.parseInt(args[0]);
		} catch (Exception ex) {
			player.getPacketDispatcher().dispatchMessage("Invalid animation command: requires 1 int paramater", GameMessage.MessageOpcode.CONSOLE);
			return false;
		}
		if (AnimationDefinitionLoader.forId(id) == null) {
			player.getPacketDispatcher().dispatchMessage("Animation "+id+" does not exist!", GameMessage.MessageOpcode.CONSOLE);
			return false;
		}
		player.getUpdateArchive().queueAnimation(id);
		player.getPacketDispatcher().dispatchMessage("Queueing animation "+id, GameMessage.MessageOpcode.CONSOLE);
		return true;
	}

	@Override
	public String[] getPossibleSyntaxes() {
		return new String[] { "emote", "anim", "animation" };
	}
	
}
