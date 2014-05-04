package org.virtue.game.logic.node.interfaces.impl;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.game.logic.node.interfaces.RSInterface;
import org.virtue.game.logic.node.interfaces.toplevel.Polls;
import org.virtue.network.protocol.messages.ClientScriptVar;
import org.virtue.network.protocol.messages.InterfaceSettingsMessage;
import org.virtue.network.protocol.messages.VarpMessage;
import org.virtue.network.protocol.packet.encoder.impl.InterfaceSettingsEncoder;

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
		case 8://TODO: Find a better way of handling this
			getPlayer().getPacketDispatcher().dispatchVarp(new VarpMessage(3708, 40633348));
			//getPlayer().getPacketDispatcher().dispatchVarp(new VarpMessage(1758, 0));
			//getPlayer().getPacketDispatcher().dispatchVarp(new VarpMessage(1765, -1));
			getPlayer().getPacketDispatcher().dispatchVarp(new VarpMessage(4041, 0));
			getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(8194, 1, 4));//Runscript: [8194, 4, 1]			
			getPlayer().getInterfaces().sendInterfaceSettings(1477, 75, 24, 0, 2);//IfaceSettings: hash:96796747, to:0, from:24, settings:2 ifaceID=1477, compID=75
			getPlayer().getInterfaces().sendInterfaceSettings(1477, 77, 1, 1, 2);//IfaceSettings: hash:96796749, to:1, from:1, settings:2 ifaceID=1477, compID=77
			Polls socialInterface = new Polls(getPlayer());

			getPlayer().getInterfaces().sendHideIcomponent(1477, 77, false);//Received component hidden: hash=96796749(1447, 77), hidden=0
			getPlayer().getInterfaces().sendHideIcomponent(1477, 3, false);//Received component hidden: hash=94896131, hidden=0
			getPlayer().getInterfaces().setInterface(socialInterface, 3, true, 1448);
			//socialInterface.postSend();
			break;
		default:
			System.out.println("Unhandled ribbon button: component="+component+", button="+button.getID()+", slot1="+slot1+", slot2="+slot2);
			break;
		}		
	}

	@Override
	public int getTabID() {
		return -1;
	}

}
