package org.virtue.game.logic.node.interfaces.impl;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.game.logic.node.interfaces.RS3Interface;
import org.virtue.network.protocol.messages.ClientScriptVar;
import org.virtue.network.protocol.messages.InterfaceMessage;

public class FriendsList extends AbstractInterface {
	
	public static InterfaceMessage noteEdit = new InterfaceMessage(451, 234, RS3Interface.GAME_SCREEN, false);
		
	public FriendsList(Player p) {
		super(RS3Interface.FRIENDS_LIST, p);
	}

	@Override
	public void postSend() {
		sendInterfaceSettings(25, 0, 500, 510);//IfaceSettings: 36044825, 500, 0, 510
		sendInterfaceSettings(23, 0, 500, 6);//IfaceSettings: 36044823, 500, 0, 6
	}

	@Override
	public void handleActionButton(int component, int slot1, int slot2, ActionButton button) {
		switch (component) {
		case 23://Ignore option
			if (button.equals(ActionButton.ONE)) {
				getPlayer().getPacketDispatcher().dispatchInterface(noteEdit);
				getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(8178));
				getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(9206, slot1/2, 1, 29556758, 29556742, 29556743));
			}
			break;
		case 42://Show "add friend" dialog
			getPlayer().getPacketDispatcher().dispatchInterface(RS3Interface.DIALOG_BOX);
			getPlayer().getPacketDispatcher().dispatchInterface(RS3Interface.INPUT_DIALOG);
			getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(8178));
			getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(103));
			break;
		case 44://Show "remove friend" dialog
			getPlayer().getPacketDispatcher().dispatchInterface(RS3Interface.DIALOG_BOX);
			getPlayer().getPacketDispatcher().dispatchInterface(RS3Interface.INPUT_DIALOG);
			getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(8178));
			getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(104));
			break;
		case 71://Show "add ignore" dialog
			getPlayer().getPacketDispatcher().dispatchInterface(RS3Interface.DIALOG_BOX);
			getPlayer().getPacketDispatcher().dispatchInterface(RS3Interface.INPUT_DIALOG);
			getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(8178));
			getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(105));			
			break;
		case 54://Show "remove ignore" dialog
			getPlayer().getPacketDispatcher().dispatchInterface(RS3Interface.DIALOG_BOX);
			getPlayer().getPacketDispatcher().dispatchInterface(RS3Interface.INPUT_DIALOG);
			getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(8178));
			getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(1419));			
			break;
		default:
			System.out.println("Unhandled friends list button: component="+component+", button="+button+", slot1="+slot1+", slot2="+slot2);
			break;
		}
		
	}

	@Override
	public int getTabID() {
		return 14;
	}

}
