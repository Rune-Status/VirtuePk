package org.virtue.network.protocol.handlers.impl;

import org.virtue.Constants;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.session.impl.WorldSession;

public class FriendOptionHandler extends PacketHandler<WorldSession> {

	@Override
	public void handle(WorldSession session) {
		ActionButton button = ActionButton.forID(getFlag("button", -1));
		if (button == null) {
			if (Constants.DEVELOPER_MODE) {
				throw new RuntimeException("Invalid Button: "+getFlag("button", -1));
			} else {
				return;
			}
		}
		int slot1 = getFlag("slot1", -1);
		int slot2 = getFlag("slot2", -1);
		String friendName = getFlag("friend", "");
		
		System.out.println("Received friend option: friend="+friendName+", slot1="+slot1+", slot2="+slot2+", button="+button.getID());
	}

}
