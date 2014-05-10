package org.virtue.network.protocol.handlers.impl;

import org.virtue.game.logic.World;
import org.virtue.game.logic.events.impl.ObjectInteractEvent;
import org.virtue.game.logic.node.object.ObjectOption;
import org.virtue.game.logic.node.object.RS3Object;
import org.virtue.game.logic.region.Tile;
import org.virtue.network.session.impl.WorldSession;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 20, 2014
 */
public class ObjectOptionHandler extends MovementHandler {

	@Override
	public void handle(WorldSession session) {		
		ObjectOption option = ObjectOption.getFromOpcode(getFlag("opcode", -1));
		if (option == null) {
			throw new RuntimeException("Invalid Object Opcode: "+getFlag("opcode", -1));			
		}
		int objectID = getFlag("objectID", -1);
		int xCoord = getFlag("baseX", -1);
		int yCoord = getFlag("baseY", -1);
		Tile location = new Tile(xCoord, yCoord, session.getPlayer().getTile().getPlane());
		
		RS3Object object = World.getWorld().getRegionManager().getRegionByID(location.getRegionID()).getObject(objectID, location);
		
		//ObjectDefinition object = ObjectDefinitionLoader.forId(objectID);
		if (object == null) {
			System.err.println("Object "+objectID+" clicked at x="+xCoord+", y="+yCoord+" does not exist!");
			return;
			//throw new RuntimeException("Invalid Object ID: "+objectID);
		}
		
		if (object.isDistanceOption(option)) {
			object.interact(session.getPlayer(), option);
		} else {
			putFlag("sizeX", object.getSizeX());
			putFlag("sizeY", object.getSizeY());
			super.handle(session);//Handle the movement aspect
			session.getPlayer().setCoordinateEvent(new ObjectInteractEvent(object, option));
		}
		//TODO: Implement checks to make sure the object actually exists at the expected location on the map.
		
		//System.out.println("Clicked object: objectID="+objectID+", xCoord="+xCoord+", yCoord="+yCoord+", optionID="+option.getID());
	}

}
