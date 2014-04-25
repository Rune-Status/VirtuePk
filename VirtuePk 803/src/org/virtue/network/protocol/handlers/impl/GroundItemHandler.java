package org.virtue.network.protocol.handlers.impl;

import org.virtue.game.logic.World;
import org.virtue.game.logic.item.GroundItem;
import org.virtue.game.logic.item.GroundItemOption;
import org.virtue.game.logic.region.Region;
import org.virtue.game.logic.region.Tile;
import org.virtue.network.session.impl.WorldSession;

public class GroundItemHandler extends MovementHandler {

	@Override
	public void handle(WorldSession session) {		
		GroundItemOption option = GroundItemOption.getFromOpcode(getFlag("opcode", -1));
		if (option == null) {
			throw new RuntimeException("Invalid ground item opcode: "+getFlag("opcode", -1));			
		}
		int itemID = getFlag("itemID", -1);
		int xCoord = getFlag("baseX", -1);
		int yCoord = getFlag("baseY", -1);
		Tile position = new Tile(xCoord, yCoord, session.getPlayer().getTile().getPlane());
		Region region = World.getWorld().getRegionManager().getRegionByID(position.getRegionID());
		if (region == null) {
			return;//Region does not exist.
		}
		GroundItem item = region.getItem(itemID);
		if (item == null) {
			return;//Item does not exist
		}
		if (!option.equals(GroundItemOption.EXAMINE)) {
			super.handle(session);//Handle the movement aspect
		}
		System.out.println("Clicked ground item: itemID="+itemID+", xCoord="+xCoord+", yCoord="+yCoord+", optionID="+option.getID());
	}
}
