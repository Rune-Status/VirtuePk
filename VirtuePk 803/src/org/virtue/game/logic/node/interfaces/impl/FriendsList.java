package org.virtue.game.logic.node.interfaces.impl;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.game.logic.node.interfaces.RSInterface;

public class FriendsList extends AbstractInterface {

	public FriendsList(Player p) {
		super(RSInterface.FRIENDS_LIST, p);
	}

	@Override
	public void postSend() {
		sendInterfaceSettings(25, 0, 500, 510);//IfaceSettings: 36044825, 500, 0, 510
		sendInterfaceSettings(23, 0, 500, 6);//IfaceSettings: 36044823, 500, 0, 6
	}

	@Override
	public void handleActionButton(int componentID, int slot1, int slot2, ActionButton button) {
		System.out.println("Friends list button pressed: component="+componentID+", button="+button+", slot1="+slot1+", slot2="+slot2);
	}

	@Override
	public int getTabID() {
		return 14;
	}

}
