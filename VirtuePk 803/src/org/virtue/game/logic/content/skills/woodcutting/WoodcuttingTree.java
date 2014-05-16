package org.virtue.game.logic.content.skills.woodcutting;

import org.virtue.Launcher;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.identity.Rank;
import org.virtue.game.logic.node.object.ObjectOption;
import org.virtue.game.logic.node.object.RS3Object;
import org.virtue.game.logic.node.object.TemporaryObject;
import org.virtue.game.logic.region.Tile;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class WoodcuttingTree extends TemporaryObject {
	
	public static int getStumpID (RS3Object object) {
		return 11855;//TODO: Finish implementing this
	}
	
	private final Log log;

	public WoodcuttingTree(int id, int rotation, int type, Tile tile, int replacementID, Log log) {
		super(id, rotation, type, tile, replacementID, replacementID);//TODO: Finish this
		this.log = log;
	}
	
	public WoodcuttingTree(RS3Object object, Log log) {
		super(object.getId(), object.getRotation(), object.getType(), object.getTile(), getStumpID(object), log.getRespawnTime());
		this.log = log;
	}
	
	@Override
	public void deplete () {
		if (log.getRandomLifeProbability() == 0 || Launcher.getRandom().nextInt(log.getRandomLifeProbability()) == 0) {
			super.deplete();
			if (getTile().getPlane() < 3) {
				
			}
		}
	}
	
	public Log getLog () {
		return log;
	}	
	
	@Override
	public void interact (Player player, ObjectOption option) {
		String message = "Clicked tree: id="+getId()+", rotation="+getRotation()+", xCoord="+getTile().getX()+", yCoord="+getTile().getX()+", option="+getDefinition().getOption(option)+" ("+option.getID()+")";
		System.out.println(message);
		if (option.equals(ObjectOption.ONE)) {
			player.setActionEvent(new WoodcuttingAction(this));
		}
		if (player.getAccount().getRank().equals(Rank.ADMINISTRATOR)) {
			player.getPacketDispatcher().dispatchMessage(message);
		}
	}
}
