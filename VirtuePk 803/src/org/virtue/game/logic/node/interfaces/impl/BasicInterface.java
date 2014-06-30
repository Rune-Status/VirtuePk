package org.virtue.game.logic.node.interfaces.impl;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.game.logic.node.interfaces.ActionButton;

public class BasicInterface extends AbstractInterface {

	public BasicInterface(int id, Player p) {
		super(id, p);
	}

	@Override
	public void postSend() {
		
	}

	@Override
	public void handleActionButton(int component, int slot1, int slot2, ActionButton button) {
		
	}

	@Override
	public int getTabID() {
		return -1;
	}

}
