package org.virtue.game.logic.node.interfaces.impl;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.game.logic.node.interfaces.ActionTab;
import org.virtue.game.logic.node.interfaces.ManagementInterface;
import org.virtue.game.logic.node.interfaces.RSInterface;
import org.virtue.network.protocol.messages.ClientScriptVar;
import org.virtue.network.protocol.messages.VarMessage;

public class RibbonInterface extends AbstractInterface {

	public RibbonInterface(Player p) {
		super(RSInterface.RIBBON, p);
	}

	@Override
	public void postSend() {
		
	}

	@Override
	public void handleActionButton(int component, int slot1, int slot2, ActionButton button) {
		switch (component) {
		case 5://Gear
			player.getInterfaces().getManagementInterface().openInterface(ManagementInterface.Type.GEAR);
			break;
		case 6://Adventure
			player.getInterfaces().getManagementInterface().openInterface(ManagementInterface.Type.ADVENTURE);
			break;
		case 8://Community
			player.getInterfaces().getManagementInterface().openInterface(ManagementInterface.Type.COMMUNITY);
			//player.getInterfaces().setInterface(socialInterface, 3, true, 1448);
			break;
		default:
			System.out.println("Unhandled ribbon button: component="+component+", button="+button.getID()+", slot1="+slot1+", slot2="+slot2);
			break;
		}		
	}
	
	public void openTab (ActionTab tab) {
		player.getPacketDispatcher().dispatchVar(new VarMessage(3708, 40633348));//Received VarPlayer: key=3708, value=42729988
		player.getPacketDispatcher().dispatchVar(new VarMessage(1758, 0));//Received VarPlayer: key=1758, value=0
		player.getPacketDispatcher().dispatchVar(new VarMessage(1765, -1));//Received VarPlayer: key=1765, value=-1
		player.getPacketDispatcher().dispatchVar(new VarMessage(4041, 0));//Received VarPlayer: key=4041, value=0
		player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(8194, 1, 4));//Received Cs2:[8194, 4, 1]
		player.getInterfaces().sendHideComponent(1477, 77, false);//Received component hidden: interface=1477, component=77, hidden=0
		player.getInterfaces().sendInterfaceSettings(1477, 75, 24, 0, 2);//IfaceSettings: hash:96796747, to:0, from:24, settings:2 ifaceID=1477, compID=75
		player.getInterfaces().sendInterfaceSettings(1477, 77, 1, 1, 2);//IfaceSettings: hash:96796749, to:1, from:1, settings:2 ifaceID=1477, compID=77
		player.getPacketDispatcher().dispatchVar(VarMessage.varc(2911, 4));//Received VarClient: key=2911, value=4
		player.getInterfaces().sendHideComponent(1448, 3, false);//Received component hidden: interface=1448, component=3, hidden=0
		player.getInterfaces().setInterface(tab, 3, true, 1448);//Interface: id=1029, clipped=1, parentID=1448, compID=3
	}

	@Override
	public int getTabID() {
		return -1;
	}

}
