package org.virtue.network.protocol.handlers.impl;

import org.virtue.game.logic.node.interfaces.RSInterface;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.session.impl.WorldSession;

/**
 * 
 * @author Virtue Development Team 2014 (c).
 */
public class InterfaceDragHandler extends PacketHandler<WorldSession> {

	@Override
	public void handle(WorldSession session) {
		int oldHash = getFlag("oldHash", -1);
		int newHash = getFlag("newHash", -1);
		int fromSlot = getFlag("oldSlot", -1);
		int toSlot = getFlag("newSlot", -1);
		int oldInterfaceID = oldHash >> 16;
		
		if (oldHash == newHash && oldInterfaceID == RSInterface.INVENTORY) {
			session.getPlayer().getInventory().switchItem(fromSlot, toSlot);
			return;
		} else if (oldHash == newHash && oldInterfaceID == RSInterface.BANK) {
			session.getPlayer().getBank().switchItems(fromSlot, oldHash & 0xffff, toSlot, newHash & 0xffff);
			return;
		}
		
		System.out.println("Dragged interface: fromSlot="+fromSlot+" oldID: "+oldInterfaceID+", oldComp: "+(oldHash & 0xffff)
				+", toSlot="+toSlot+", newID: "+(newHash >> 16)+", newComp: "+(newHash & 0xffff));
	}

}
