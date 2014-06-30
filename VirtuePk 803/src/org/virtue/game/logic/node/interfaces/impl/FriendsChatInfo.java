package org.virtue.game.logic.node.interfaces.impl;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.game.logic.node.interfaces.RS3Interface;
import org.virtue.network.protocol.messages.ClientScriptVar;

public class FriendsChatInfo extends AbstractInterface {

	public FriendsChatInfo(Player p) {
		super(RS3Interface.FRIENDS_CHAT_INFO, p);
	}

	@Override
	public void postSend() {
		//Sends the last friends chat the player joined:// getPlayer().getAccount().getUsername().getName()
		sendInterfaceSettings(23, 0, 600, 1024);//IfaceSettings: 93519895, 600, 0, 1024
	}

	@Override
	public void handleActionButton(int component, int slot1, int slot2, ActionButton button) {
		switch (component) {
		case 29://Lootshare
			//TODO: Handle lootshare
			break;
		case 31://Kick/ban
			getPlayer().getPacketDispatcher().dispatchInterface(RS3Interface.DIALOG_BOX);
			getPlayer().getPacketDispatcher().dispatchInterface(RS3Interface.INPUT_DIALOG);
			getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(8178));
			getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(2688));			
			break;
		case 33://Join friends chat
			if (getPlayer().getChatManager().getCurrentChannelOwner() != null) {
				getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(194, 1));
				return;
			}		
			getPlayer().getPacketDispatcher().dispatchInterface(RS3Interface.DIALOG_BOX);
			getPlayer().getPacketDispatcher().dispatchInterface(RS3Interface.INPUT_DIALOG);
			getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(8178));
			getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(8537));
			getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(194, 1));
			break;
		case 35://Friends chat settings
			getPlayer().getInterfaces().setTopInterface(new FriendsChatSettings(getPlayer()));
			//getPlayer().getPacketDispatcher().dispatchInterface(new InterfaceMessage(RSInterface.FRIENDS_CHAT_SETTINGS, 236, RSInterface.GAME_SCREEN, false));
			//System.out.println("Unhandled request to open friends chat settings.");
			break;
		default:
			System.out.println("Unhandled Friends chat info button: component="+component+", button="+button+", slot1="+slot1+", slot2="+slot2);
			break;
		}		
	}

	@Override
	public int getTabID() {
		return 15;
	}

}
