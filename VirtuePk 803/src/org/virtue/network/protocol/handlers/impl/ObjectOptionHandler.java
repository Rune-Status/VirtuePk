package org.virtue.network.protocol.handlers.impl;

import org.virtue.cache.def.ObjectDefinition;
import org.virtue.cache.def.ObjectDefinitionLoader;
import org.virtue.game.logic.node.object.ObjectOption;
import org.virtue.network.session.impl.WorldSession;

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
		
		ObjectDefinition object = ObjectDefinitionLoader.forId(objectID);
		if (object == null) {
			throw new RuntimeException("Invalid Object ID: "+objectID);
		}
		if (option != ObjectOption.EXAMINE) {
			putFlag("sizeX", object.getSize()[0]);
			putFlag("sizeY", object.getSize()[1]);
			super.handle(session);//Handle the movement aspect
		}
		System.out.println("Clicked object: objectID="+objectID+", xCoord="+xCoord+", yCoord="+yCoord+", optionID="+option.getID());
	}

}
