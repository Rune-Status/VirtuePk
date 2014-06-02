package org.virtue.game.logic.node.interfaces.management;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.game.logic.node.interfaces.ActionTab;
import org.virtue.game.logic.node.interfaces.ManagementInterface.Type;
import org.virtue.game.logic.node.interfaces.RSInterface;

public class Minigames extends ActionTab {

	public Minigames(Player p) {
		super(RSInterface.MINIGAMES, p);
	}

	@Override
	public boolean isCompHidden(int tabID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getUnknownInt() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTab() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void postSend() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleActionButton(int component, int slot1, int slot2, ActionButton button) {
		System.out.println("Unhandled minigames interface button: component="+component+", button="+button.getID()+", slot1="+slot1+", slot2="+slot2);
	}

	@Override
	public Type getType() {
		return Type.ADVENTURE;
	}

}
