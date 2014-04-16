package org.virtue.network.protocol.handlers.commands;

import org.virtue.Constants;
import org.virtue.game.node.entity.player.Player;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 15, 2014
 */
public class SendWalk implements Command {

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		if (syntax.equalsIgnoreCase("reset")) {
			player.getUpdateArchive().getMovement().teleport(Constants.DEFAULT_LOCATION);
			player.getUpdateArchive().getMovement().setNextWalkDirection(-1);
		}
		int direction;
		if (syntax.equalsIgnoreCase("run")){
			try {
				direction = Integer.parseInt(args[0]);
			} catch (Exception ex) {
				return false;
			}
			if (direction > 15 || direction < -1) {
				player.getPacketDispatcher().dispatchMessage("Invalid run command: must be between -1 and 15", MessageOpcode.CONSOLE);
				return false;
			}
			player.getUpdateArchive().getMovement().setNextRunDirection(direction);
			player.getUpdateArchive().getMovement().setNextWalkDirection(direction);
			return true;
		}
		try {
			direction = Integer.parseInt(args[0]);
		} catch (Exception ex) {
			return false;
		}
		if (direction > 7 || direction < -1) {
			player.getPacketDispatcher().dispatchMessage("Invalid walk command: must be between -1 and 7", MessageOpcode.CONSOLE);
			return false;
		}
		player.getUpdateArchive().getMovement().setNextWalkDirection(direction);
		return true;
	}

	@Override
	public String[] getPossibleSyntaxes() {
		return new String[] { "walk", "move", "reset", "run" };
	}

}
