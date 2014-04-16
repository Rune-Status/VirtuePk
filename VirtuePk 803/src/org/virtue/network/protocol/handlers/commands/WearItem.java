package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.item.Item;
import org.virtue.game.node.entity.player.Player;
import org.virtue.game.node.interfaces.impl.Equipment;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 16, 2014
 */
public class WearItem implements Command {

	public Equipment equipment;
	
	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		 int id;
			try {
				id = Integer.parseInt(args[0]);
			} catch (Exception ex) {
				return false;
			} 
			Item item = new Item(id, 1);
			player.getEquipment().add(item.getEquipId(), item);
			equipment = new Equipment(player);
		return true;
	}

	@Override
	public String[] getPossibleSyntaxes() {
     return new String[] { "wear", "equip" };
	}

}
