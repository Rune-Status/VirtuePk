package org.virtue.network.protocol.handlers.impl;

import org.virtue.game.logic.World;
import org.virtue.game.logic.events.impl.ItemOnObjectEvent;
import org.virtue.game.logic.item.Item;
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.game.logic.node.object.RS3Object;
import org.virtue.game.logic.region.Tile;
import org.virtue.network.session.impl.WorldSession;

public class ItemOnObjectHandler extends MovementHandler {

	@Override
	public void handle(WorldSession session) {
		int interfaceHash = getFlag("interface", -1);
		int itemID = getFlag("item", -1);
		int slotID = getFlag("slot", -1);
		int interfaceID = interfaceHash >> 16;
		int component = interfaceHash & 0xffff;
		AbstractInterface iFace = session.getPlayer().getInterfaces().getInterface(interfaceID);
		if (iFace == null) {
			return;
		}
		Item item = iFace.getItem(component, itemID, slotID);
		if (item == null) {
			return;
		}
		int objectID = getFlag("objectID", -1);
		int xCoord = getFlag("baseX", -1);
		int yCoord = getFlag("baseY", -1);
		Tile location = new Tile(xCoord, yCoord, session.getPlayer().getTile().getPlane());
		
		RS3Object object = World.getWorld().getRegionManager().getRegionByID(location.getRegionID()).getObject(objectID, location);
		if (object == null) {
			System.err.println("Item "+item.getId()+" used with object "+objectID+" at x="+xCoord+", y="+yCoord+" which does not exist!");
			return;
		} else {
			putFlag("sizeX", object.getSizeX());
			putFlag("sizeY", object.getSizeY());
			super.handle(session);//Handle the movement aspect
			session.getPlayer().setCoordinateEvent(new ItemOnObjectEvent(object, item));
		}
		
	}

}
