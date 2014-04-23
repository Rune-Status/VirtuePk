package org.virtue.game.logic.node.interfaces.impl;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.game.logic.node.interfaces.RSInterface;
import org.virtue.network.protocol.messages.ClientScriptVar;
import org.virtue.network.protocol.messages.InterfaceMessage;

public class FriendsList extends AbstractInterface {
	
	public static InterfaceMessage dialog = new InterfaceMessage(1418, 233, RSInterface.GAME_SCREEN, true);
	public static InterfaceMessage input = new InterfaceMessage(1469, 0, 1418, true);

	public FriendsList(Player p) {
		super(RSInterface.FRIENDS_LIST, p);
	}

	@Override
	public void postSend() {
		sendInterfaceSettings(25, 0, 500, 510);//IfaceSettings: 36044825, 500, 0, 510
		sendInterfaceSettings(23, 0, 500, 6);//IfaceSettings: 36044823, 500, 0, 6
	}

	@Override
	public void handleActionButton(int component, int slot1, int slot2, ActionButton button) {
		switch (component) {
		case 42://Show "add friend" dialog
			getPlayer().getPacketDispatcher().dispatchInterface(dialog);
			getPlayer().getPacketDispatcher().dispatchInterface(input);
			getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(8178));
			getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(103));
			break;
		case 44://Show "remove friend" dialog
			getPlayer().getPacketDispatcher().dispatchInterface(dialog);
			getPlayer().getPacketDispatcher().dispatchInterface(input);
			getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(8178));
			getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(104));
			break;
		case 71://Show "add ignore" dialog
			getPlayer().getPacketDispatcher().dispatchInterface(dialog);
			getPlayer().getPacketDispatcher().dispatchInterface(input);
			getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(8178));
			getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(105));			
			break;
		case 54://Show "remove ignore" dialog
			getPlayer().getPacketDispatcher().dispatchInterface(dialog);
			getPlayer().getPacketDispatcher().dispatchInterface(input);
			getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(8178));
			getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(1419));			
			break;
		default:
			System.out.println("Friends list button pressed: component="+component+", button="+button+", slot1="+slot1+", slot2="+slot2);
			break;
		}
		
	}

	@Override
	public int getTabID() {
		return 14;
	}

}
