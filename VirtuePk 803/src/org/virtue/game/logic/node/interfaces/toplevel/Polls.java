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
		System.out.println("Opening polls...");
		//Interface: id=1029, clipped=1, parentID=1448, compID=3
		getPlayer().getInterfaces().sendHideIcomponent(1477, 4, true);//Received component hidden: hash=94896132, hidden=1
		getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(9602, 23327130, 0, 67436629));//Runscript: [9602, 67436629, 0, 23327130]
		getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(9602, 23317035, 1, 67436630));//Runscript: [9602, 67436630, 1, 23317035]
		setComponentHidden(39, true);//Received component hidden: hash=67436583, hidden=1
		setComponentHidden(101, true);//Received component hidden: hash=67436645, hidden=1
		setComponentHidden(134, true);//Received component hidden: hash=67436678, hidden=1
		setComponentHidden(38, true);//Received component hidden: hash=67436582, hidden=1
		setComponentHidden(100, true);//Received component hidden: hash=67436644, hidden=1
		setComponentHidden(132, true);//Received component hidden: hash=67436676, hidden=1
		setComponentHidden(39, false);//Received component hidden: hash=67436583, hidden=0
		sendInterfaceSettings(29, 0, 5, 2);//IfaceSettings: hash:67436573, to:0, from:5, settings:2 ifaceID=1029, compID=29
		sendInterfaceSettings(22, 0, 5, 2);//IfaceSettings: hash:67436566, to:0, from:5, settings:2 ifaceID=1029, compID=22
		setComponentText(95, "Power to the Players");//Interface string 3: hash=67436639, string=Power to the Players
		setComponentText(100, "Click on one of the active polls below to have your say.<br>Remember to check each week for new polls!");//Interface string 3: hash=67436644, string=Click on one of the active polls below to have your say.<br>Remember to check each week for new polls!
		setComponentHidden(50, false);//Received component hidden: hash=67436594, hidden=0
		setComponentHidden(51, true);//Received component hidden: hash=67436595, hidden=1
		setComponentHidden(48, false);//Received component hidden: hash=67436592, hidden=0
		setComponentText(49, "Next Distraction and Diversion");//Interface string 3: hash=67436593, string=Next Distraction and Diversion
		setComponentHidden(52, true);//Received component hidden: hash=67436596, hidden=1
		setComponentHidden(53, false);//Received component hidden: hash=67436597, hidden=0
		setComponentHidden(57, false);//Received component hidden: hash=67436601, hidden=0
		setComponentHidden(55, true);//Received component hidden: hash=67436599, hidden=1
		setComponentHidden(44, false);//Received component hidden: hash=67436588, hidden=0
		setComponentText(56, "Impling Boost");//Interface string 3: hash=67436600, string=Impling Boost
		setComponentHidden(58, true);//Received component hidden: hash=67436602, hidden=1
		setComponentHidden(59, false);//Received component hidden: hash=67436603, hidden=0
		setComponentHidden(66, false);//Received component hidden: hash=67436610, hidden=0
		setComponentHidden(62, true);//Received component hidden: hash=67436606, hidden=1
		setComponentHidden(19, false);//Received component hidden: hash=67436563, hidden=0
		//setComponentText(66, "");//Interface string 3: hash=67436610, string=
		//setComponentText(66, "Closed");//Interface string 3: hash=67436610, string=Closed
		setComponentText(63, "Survey - Upcoming Updates Information");//Interface string 3: hash=67436607, string=Survey - Upcoming Updates Information
		setComponentHidden(67, true);//Received component hidden: hash=67436611, hidden=1
		setComponentHidden(68, false);//Received component hidden: hash=67436612, hidden=0
		setComponentHidden(37, false);//Received component hidden: hash=67436581, hidden=0
		setComponentHidden(95, false);//Received component hidden: hash=67436639, hidden=0
		setComponentHidden(100, false);//Received component hidden: hash=67436644, hidden=0
		setComponentHidden(96, false);//Received component hidden: hash=67436640, hidden=0
		setComponentHidden(97, true);//Received component hidden: hash=67436641, hidden=1
		getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(9598, 0));//Runscript: [9598, 0]
		getPlayer().getInterfaces().sendHideIcomponent(1448, 5, true);//Received component hidden: hash=94896133, hidden=1
		getPlayer().getInterfaces().sendHideIcomponent(1448, 6, true);//Received component hidden: hash=94896134, hidden=1
		getPlayer().getInterfaces().sendHideIcomponent(1448, 7, true);//Received component hidden: hash=94896135, hidden=1
		getPlayer().getInterfaces().sendHideIcomponent(1448, 8, true);//Received component hidden: hash=94896136, hidden=1
		getPlayer().getInterfaces().sendHideIcomponent(1448, 9, true);//Received component hidden: hash=94896137, hidden=1
		getPlayer().getInterfaces().sendHideIcomponent(1448, 10, true);//Received component hidden: hash=94896138, hidden=1
		getPlayer().getInterfaces().sendHideIcomponent(1448, 11, true);//Received component hidden: hash=94896139, hidden=1
		getPlayer().getInterfaces().sendHideIcomponent(1448, 12, true);//Received component hidden: hash=94896140, hidden=1
		getPlayer().getInterfaces().sendHideIcomponent(1448, 1, true);//Received component hidden: hash=94896129, hidden=1
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
