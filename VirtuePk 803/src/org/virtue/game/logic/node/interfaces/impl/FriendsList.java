package org.virtue.game.logic.node.interfaces.impl;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.game.logic.node.interfaces.ActionButton;

public class FriendsList extends AbstractInterface {
	
	public static final int FRIENDS_LIST_INTERFACE = 550;

	public FriendsList(Player p) {
		super(FRIENDS_LIST_INTERFACE, p);
	}

	@Override
	public void postSend() {
		sendInterfaceSettings(25, 0, 500, 510);//IfaceSettings: 36044825, 500, 0, 510
		sendInterfaceSettings(23, 0, 500, 6);//IfaceSettings: 36044823, 500, 0, 6
	}

	@Override
	public void handleActionButton(int componentID, int slotID1, int slotID2, ActionButton button) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getTabID() {
		return 14;
	}

}
