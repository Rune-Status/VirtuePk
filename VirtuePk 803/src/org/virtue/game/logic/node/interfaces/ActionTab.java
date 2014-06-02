package org.virtue.game.logic.node.interfaces;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.ManagementInterface.Type;

public abstract class ActionTab extends AbstractInterface {

	public ActionTab(int id, Player p) {
		super(id, p);
	}
	
	@Override
	public void close () {
		super.close();
	}
	
	public abstract boolean isCompHidden (int tabID);
	
	public abstract int getUnknownInt ();
	
	public abstract int getTab ();
	
	public abstract Type getType ();
	
	public int getComponentID () {
		return 3;
	}
	
	@Override
	public int getTabID() {
		return -1;
	}

}
