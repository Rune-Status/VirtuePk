package org.virtue.network.protocol.handlers.commands;

import java.io.IOException;

import org.virtue.cache.def.ItemDefinition;
import org.virtue.cache.def.ItemDefinitionLoader;
import org.virtue.game.logic.item.Item;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.network.protocol.messages.GameMessage;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 15, 2014
 */
public class SendItem implements Command {

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		int id, amount;
		if (syntax.equalsIgnoreCase("itemname")) {
			try {
				String fullName = "";
				for (String arg : args) {
					fullName+= " "+arg;
				}
				ItemDefinition item = ItemDefinitionLoader.forName(fullName.trim());
				if (item == null) {
					player.getPacketDispatcher().dispatchMessage("The item '"+fullName.trim()+"' was not found.", MessageOpcode.CONSOLE);
					return false;
				}
				id = item.getID();
			} catch (IOException ex) {
				return false;
			}
			amount = 1;
		} else {
			try {
				id = Integer.parseInt(args[0]);
				amount = Integer.parseInt(args[1]);
			} catch (Exception ex) {
				return false;
			}
		}
		Item item = Item.create(id, amount);
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
            return new String[] { "pickup", "item", "itemname" };
	}
	
	

}
