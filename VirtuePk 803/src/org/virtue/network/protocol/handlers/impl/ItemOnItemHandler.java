package org.virtue.network.protocol.handlers.impl;

import org.virtue.game.logic.item.Item;
import org.virtue.game.logic.node.interfaces.RSInterface;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.session.impl.WorldSession;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class ItemOnItemHandler extends PacketHandler<WorldSession> {

    @Override
    public void handle(WorldSession session) {
    	int componentHash = getFlag("compHash", -1);
    	int slot = getFlag("slot", -1);
    	int itemID = getFlag("item", -1);
    	int interfaceID = componentHash >> 16;
    	int componentID = componentHash & 0xffff;
    	
    	Item item = null;
    	
    	int prevComponentHash = getFlag("prevCompHash", -1);
    	int prevSlot = getFlag("prevSlot", -1);
    	int prevItemID = getFlag("prevItem", -1);
    	int prevInterfaceID = prevComponentHash >> 16;
    	int prevComponentID = prevComponentHash & 0xffff;   
    	
    	if (componentHash == prevComponentHash && interfaceID == RSInterface.INVENTORY) {
    		session.getPlayer().getInventory().handleItemOnItem(componentID, itemID, slot, prevItemID, prevSlot);
    		return;
    	}
    	
    	System.out.println("Previous: iFace="+prevInterfaceID+", comp="+prevComponentID+", item="+prevItemID+", slot="+prevSlot
        		+", Current: iFace="+interfaceID+", comp="+componentID+", item="+itemID+", slot="+slot);
    }
    
}
