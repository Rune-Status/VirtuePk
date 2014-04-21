package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.logic.item.Item;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.network.protocol.messages.GameMessage;

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
                        Item item = new Item(id, amount);
                        if (item.getDefinition() == null) {
                            player.getPacketDispatcher().dispatchMessage("Item "+id+" does not exist!", GameMessage.MessageOpcode.CONSOLE);
                            return false;
                        }
                        player.getPacketDispatcher().dispatchMessage("Spawned item: "+item.getDefinition().getName(), GameMessage.MessageOpcode.CONSOLE);
			player.getInventory().add(item);
		return true;
	}

	@Override
	public String[] getPossibleSyntaxes() {
            return new String[] { "pickup", "item" };
	}
	
	

}
