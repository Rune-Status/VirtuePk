package org.virtue.game.logic.node.interfaces.toplevel;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.game.logic.node.interfaces.RSInterface;
import org.virtue.network.protocol.messages.ClientScriptVar;

public class Polls extends AbstractInterface {

	public Polls(Player p) {
		super(RSInterface.POLLS, p);
	}

	@Override
	public void postSend() {
		//Interface: id=1029, clipped=1, parentID=1448, compID=3
		getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(9602, 23327130, 0, 67436629));//Runscript: [9602, 67436629, 0, 23327130]
		getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(9602, 23317035, 1, 67436630));//Runscript: [9602, 67436630, 1, 23317035]
		sendInterfaceSettings(29, 0, 5, 2);//IfaceSettings: hash:67436573, to:0, from:5, settings:2 ifaceID=1029, compID=29
		sendInterfaceSettings(22, 0, 5, 2);//IfaceSettings: hash:67436566, to:0, from:5, settings:2 ifaceID=1029, compID=22
		setComponentText(95, "Power to the Players");//Interface string 3: hash=67436639, string=Power to the Players
		setComponentText(100, "Click on one of the active polls below to have your say.<br>Remember to check each week for new polls!");//Interface string 3: hash=67436644, string=Click on one of the active polls below to have your say.<br>Remember to check each week for new polls!
		setComponentText(49, "Next Distraction and Diversion");//Interface string 3: hash=67436593, string=Next Distraction and Diversion
		setComponentText(56, "Halloween 2014: What happens next? #3");//Interface string 3: hash=67436600, string=Halloween 2014: What happens next? #3
		setComponentText(66, "");//Interface string 3: hash=67436610, string=
		setComponentText(66, "Closed");//Interface string 3: hash=67436610, string=Closed
		setComponentText(63, "Elf City Slayer");//Interface string 3: hash=67436607, string=Elf City Slayer
		getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(9598, 0));//Runscript: [9598, 0]
	}

	@Override
	public void handleActionButton(int component, int slot1, int slot2, ActionButton button) {
		System.out.println("Unhandled community interface button: component="+component+", button="+button.getID()+", slot1="+slot1+", slot2="+slot2);
	}

	@Override
	public int getTabID() {
		return -1;
	}

}
