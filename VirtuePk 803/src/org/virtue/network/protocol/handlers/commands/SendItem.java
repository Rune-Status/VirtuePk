package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.logic.item.Item;
import org.virtue.game.logic.node.entity.player.Player;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 15, 2014
 */
public class SendItem implements Command {

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		  int id, amount;
			try {
				id = Integer.parseInt(args[0]);
				amount = Integer.parseInt(args[1]);
			} catch (Exception ex) {
				return false;
			}
			player.getInventory().add(new Item(id, amount));
		return true;
	}

	@Override
	public String[] getPossibleSyntaxes() {
        return new String[] { "pickup", "item" };
	}
	
	

}
