package org.virtue.game.logic.content.skills.woodcutting;

import org.virtue.Launcher;
import org.virtue.game.logic.World;
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
	
	private final Log log;
	private RS3Object top;

	public WoodcuttingTree(int id, int rotation, int type, Tile tile, int replacementID, Log log) {
		super(id, rotation, type, tile, replacementID, log.getRespawnTime());
		this.log = log;		
	}
	
	private RS3Object findTop () {
		if (top != null) {
			return top;
		}
		if (getTile().getPlane() < 3) {
			Tile topTile = new Tile(getTile().getX() - 1, getTile().getY() - 1, getTile().getPlane() + 1);
			RS3Object[] objects = World.getWorld().getRegionManager().getRegionByID(topTile.getRegionID()).getObjects(topTile);
			if (objects == null) {
				topTile = topTile.manipulate(+1, 0, 0);
				objects = World.getWorld().getRegionManager().getRegionByID(topTile.getRegionID()).getObjects(topTile);
				if (objects == null) {
					topTile = topTile.manipulate(-1, +1, 0);
					objects = World.getWorld().getRegionManager().getRegionByID(topTile.getRegionID()).getObjects(topTile);
					if (objects == null) {
						topTile = topTile.manipulate(+1, 0, 0);
						objects = World.getWorld().getRegionManager().getRegionByID(topTile.getRegionID()).getObjects(topTile);
					}
				}
			}
			if (objects != null) {
				top = objects[0];
			}
		}
		//System.out.println("Top is "+(top == null ? "null" : top.getId()));
		return top;
	}
	
	public WoodcuttingTree(RS3Object object, Log log) {
		super(object.getId(), object.getRotation(), object.getType(), object.getTile(), log.getStumpID(), log.getRespawnTime());
		this.log = log;
	}
	
	@Override
	public void deplete () {
		if (log.equals(Log.ELDER)) {
			return;//TODO: Implement depletion handling for elder trees
		}
		if (log.getRandomLifeProbability() == 0 || Launcher.getRandom().nextInt(log.getRandomLifeProbability()) == 0) {
			super.deplete();
			if (findTop() != null) {
				World.getWorld().getRegionManager().getRegionByID(top.getTile().getRegionID()).destroyObject(top);
			}
		}
	}
	
	@Override
	public void respawn () {
		super.respawn();
		if (findTop() != null) {
			World.getWorld().getRegionManager().getRegionByID(top.getTile().getRegionID()).spawnObject(top);
		}
	}
	
	public Log getLog () {
		return log;
	}	
	
	@Override
	public void interact (Player player, ObjectOption option) {
		String message = "Clicked tree: id="+getId()+", rotation="+getRotation()+", xCoord="+getTile().getX()+", yCoord="+getTile().getX()+", option="+getDefinition().getOption(option)+" ("+option.getID()+")";
		System.out.println(message);
		if (option.equals(ObjectOption.ONE) && getDefinition().getOption(option).equalsIgnoreCase("Chop down")) {
			player.setActionEvent(new WoodcuttingAction(this));
		}
		if (player.getAccount().getRank().equals(Rank.ADMINISTRATOR)) {
			player.getPacketDispatcher().dispatchMessage(message);
		}
	}
}
