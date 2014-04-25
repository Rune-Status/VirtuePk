package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.logic.World;
import org.virtue.game.logic.content.skills.Skill;
import org.virtue.game.logic.item.GroundItem;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.region.Region;
import org.virtue.network.protocol.messages.GroundItemMessage;
import org.virtue.network.protocol.messages.GroundItemMessage.GroundItemType;
import org.virtue.network.protocol.packet.encoder.impl.GroundItemEncoder;

/**
 * @author Taylor Moon
 * @since Jan 26, 2014
 */
public class TestCommand implements Command {

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		int itemID = 1929;
		if (args.length > 0) {
			try {
				itemID = Integer.parseInt(args[0]);
			} catch (NumberFormatException ex) {
				//Do nothing, as we will just use the default value
			}
		}
		GroundItem item = new GroundItem(itemID, 1, player.getTile());
		//System.out.println("Spawning item: "+item.getDefinition().getName()+" at x="+item.getTile().getX()+", y="+item.getTile().getY());
		Region region = World.getWorld().getRegionManager().getRegionByID(player.getTile().getRegionID());
		region.addItem(item);
		//player.getAccount().getSession().getTransmitter().send(GroundItemEncoder.class, new GroundItemMessage(GroundItemType.CREATE, item, player.getViewport().getLastLoadedTile()));
//		player.getUpdateArchive().queueAnimation(918);
//		String text = player.requestInput("Enter Amount:");
//		player.getPacketDispatcher().dispatchMessage(text);
		return true;
	}

	@Override
	public String[] getPossibleSyntaxes() {
		return new String[] { "test" };
	}
}
