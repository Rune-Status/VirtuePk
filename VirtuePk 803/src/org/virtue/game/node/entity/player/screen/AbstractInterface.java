package org.virtue.game.node.entity.player.screen;

import org.virtue.game.node.entity.player.Player;
import org.virtue.network.messages.ClientScriptVar;

public abstract class AbstractInterface {
	
	public static final int UNLOCK_SCRIPT = 8862;
	
	final int interfaceID;
	final Player player;
	
	AbstractInterface (int id, Player p) {
		this.interfaceID = id;
		this.player = p;
	}
	
	public abstract void handleActionButton (int componentID, int buttonID1, int buttonID2);
	
	public void setLock (boolean isLocked) {
		player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(UNLOCK_SCRIPT, getInterfaceOrdial(), (isLocked ? 0 : 1)));
	}
	
	public abstract int getInterfaceOrdial ();
}
