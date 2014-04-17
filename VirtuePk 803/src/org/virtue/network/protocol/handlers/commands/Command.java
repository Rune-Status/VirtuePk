package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.logic.node.entity.player.Player;

/**
 * @author Taylor Moon
 * @since Jan 26, 2014
 */
public interface Command {
	
	/**
	 * Called when this command is executed.
	 * @param syntax The syntax of the command.
	 * @param player The player.
	 * @param args The arguments
	 * @return
	 */
	boolean handle(String syntax, Player player, boolean clientCommand, String... args);
	
	/**
	 * Returns a string array of possible command names that could trigger this event.
	 * @return The possible array of syntaxes.
	 */
	String[] getPossibleSyntaxes();
}
