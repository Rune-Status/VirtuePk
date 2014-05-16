package org.virtue.network.protocol.handlers.commands;

import org.virtue.Constants;
import org.virtue.game.logic.World;
import org.virtue.game.logic.content.skills.Skill;
import org.virtue.game.logic.item.GroundItem;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.object.RS3Object;
import org.virtue.game.logic.region.Region;
import org.virtue.game.logic.region.Tile;
import org.virtue.network.protocol.messages.GroundItemMessage;
import org.virtue.network.protocol.messages.GroundItemMessage.GroundItemType;
import org.virtue.network.protocol.messages.ObjectMessage;
import org.virtue.network.protocol.messages.ObjectMessage.ObjectUpdateType;
import org.virtue.network.protocol.packet.encoder.impl.GroundItemEncoder;
import org.virtue.network.protocol.packet.encoder.impl.ObjectUpdateEncoder;

/**
 * @author Taylor Moon
 * @since Jan 26, 2014
 */
public class TestCommand implements Command {

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		Tile target = Constants.DEFAULT_LOCATION;
		player.getUpdateArchive().getMovement().reset();
		player.getUpdateArchive().getMovement().teleport(target);
		System.out.println("Teleporting to region: "+target.getRegionID());
		//player.loadMapRegion();
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
