package org.virtue.game.logic.node.interfaces.impl;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.game.logic.node.interfaces.RSInterface;
import org.virtue.network.protocol.messages.ClientScriptVar;
import org.virtue.network.protocol.messages.VarpMessage;

public class ClanSettings extends AbstractInterface {

	public ClanSettings(Player p) {
		super(RSInterface.CLAN_SETTINGS, p);
	}

	@Override
	public void postSend() {
		sendInterfaceSettings(46, 0, 5, 2);//IfaceSettings: interface=1096, compID=46, fromSlot=0, toSlot=5, settings=2
		setComponentHidden(223, false);//Received component hidden: iFace=1096, compID=223, hidden=0
		setComponentHidden(208, false);//Received component hidden: iFace=1096, compID=208, hidden=0
		sendInterfaceSettings(282, 0, 126, 2);//IfaceSettings: interface=1096, compID=282, fromSlot=0, toSlot=126, settings=2
		sendInterfaceSettings(268, 0, 225, 2);//IfaceSettings: interface=1096, compID=268, fromSlot=0, toSlot=225, settings=2
		sendInterfaceSettings(372, 0, 127, 2);//IfaceSettings: interface=1096, compID=372, fromSlot=0, toSlot=127, settings=2
		sendInterfaceSettings(246, 0, 144, 2);//IfaceSettings: interface=1096, compID=246, fromSlot=0, toSlot=144, settings=2
		sendInterfaceSettings(296, 1, 201, 2);//IfaceSettings: interface=1096, compID=296, fromSlot=1, toSlot=201, settings=2
		sendInterfaceSettings(213, 0, 3, 2);//IfaceSettings: interface=1096, compID=213, fromSlot=0, toSlot=3, settings=2
		sendInterfaceSettings(228, 0, 4, 2);//IfaceSettings: interface=1096, compID=228, fromSlot=0, toSlot=4, settings=2
	}

	@Override
	public void handleActionButton(int component, int slot1, int slot2, ActionButton button) {
		switch (component) {
		case 46://Arrow beside member
			player.getPacketDispatcher().dispatchVarp(new VarpMessage(1845, 32899));
			player.getPacketDispatcher().dispatchVarp(new VarpMessage(1846, 0));
			//TODO: Send global string
			player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(4314));
		default:
			System.out.println("Unhandled clan settings button: component="+component+", slot1="+slot1+", slot2="+slot2+", button="+button.getID());
			break;
		}		
	}

	@Override
	public int getTabID() {
		return -1;
	}

}
