package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.logic.World;
import org.virtue.game.logic.content.skills.Skill;
import org.virtue.game.logic.item.GroundItem;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.object.RS3Object;
import org.virtue.game.logic.region.Region;
import org.virtue.network.protocol.messages.GroundItemMessage;
import org.virtue.network.protocol.messages.GroundItemMessage.GroundItemType;
import org.virtue.network.protocol.messages.ObjectMessage;
import org.virtue.network.protocol.messages.ObjectMessage.ObjectType;
import org.virtue.network.protocol.packet.encoder.impl.GroundItemEncoder;
import org.virtue.network.protocol.packet.encoder.impl.ObjectUpdateEncoder;

/**
 * @author Taylor Moon
 * @since Jan 26, 2014
 */
public class TestCommand implements Command {

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		int objectID = 19001;
		if (args.length > 0) {
			try {
				objectID = Integer.parseInt(args[0]);
			} catch (NumberFormatException ex) {
				//Do nothing, as we will just use the default value
			}
		}
		RS3Object object = new RS3Object(objectID, 0, 10, player.getTile());
		System.out.println("Spawning object: "+object.getDefinition().getName()+" at x="+object.getTile().getX()+", y="+object.getTile().getY());
		Region region = World.getWorld().getRegionManager().getRegionByID(player.getTile().getRegionID());
		region.addObject(object, player.getTile().getPlane(), player.getTile().getXInRegion(), player.getTile().getYInRegion());
		//region.getObject(id, location);
		player.getAccount().getSession().getTransmitter().send(ObjectUpdateEncoder.class, new ObjectMessage(ObjectType.CREATE, object, player.getViewport().getLastLoadedTile()));
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
