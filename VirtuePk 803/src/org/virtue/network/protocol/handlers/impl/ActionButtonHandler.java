package org.virtue.network.protocol.handlers.impl;

import org.virtue.game.node.interfaces.ActionButton;
import org.virtue.game.node.interfaces.InterfaceManager;
import org.virtue.game.node.interfaces.TabInterface;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.session.impl.WorldSession;

public class ActionButtonHandler extends PacketHandler<WorldSession> {


	
	/**
	 * Represents the interface manager.
	 */
	private InterfaceManager interfaceManager;
	
	@Override
	public void handle(WorldSession session) {
		int interfaceHash = getFlag("interface", -1);
		int slotID = getFlag("slot1", -1);
		int slotID2 = getFlag("slot2", -1);
		ActionButton button = ActionButton.getFromOpcode(getFlag("opcode", -1));
		if (button == null) {
			throw new RuntimeException("InvalidOpcode");			
		}
		int interfaceID = interfaceHash >> 16;
		int component = interfaceHash & 0xffff;
		
		TabInterface iFace = session.getPlayer().getInterfaces().getInterface(interfaceID);
		if (iFace != null) {
			iFace.handleActionButton(component, slotID, slotID2, button);
			return;
		}
		
		switch (interfaceID) {
		case 1433:
			if (component == 65) {//Logout button
				session.getPlayer().sendLogout(false);
				return;
			} else if (component == 57) {//Lobby logout button
				session.getPlayer().sendLogout(true);
				return;
			}
			break;
		case 1431:
			if (component == 7) {
				interfaceManager.sendMeleePowersTab();
			}
			break;
		}
		System.out.println("InterfaceID: " + interfaceID + " ComponentID: "+ component + " slotID: " + slotID + " slotID2: " + slotID2);
	}

}
