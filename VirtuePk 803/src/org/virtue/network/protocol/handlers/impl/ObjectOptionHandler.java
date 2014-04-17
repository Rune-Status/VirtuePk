package org.virtue.network.protocol.handlers.impl;

import org.virtue.game.logic.node.object.ObjectOption;
import org.virtue.network.session.impl.WorldSession;

public class ObjectOptionHandler extends MovementHandler {

	@Override
	public void handle(WorldSession session) {		
		ObjectOption option = ObjectOption.getFromOpcode(getFlag("opcode", -1));
		if (option == null) {
			throw new RuntimeException("Invalid Object Opcode: "+getFlag("opcode", -1));			
		}
		if (option != ObjectOption.EXAMINE) {
			super.handle(session);//Handle the movement aspect
		}
		int objectID = getFlag("objectID", -1);
		int xCoord = getFlag("baseX", -1);
		int yCoord = getFlag("baseY", -1);
		System.out.println("Clicked object: objectID="+objectID+", xCoord="+xCoord+", yCoord="+yCoord+", optionID="+option.getID());
	}

}
