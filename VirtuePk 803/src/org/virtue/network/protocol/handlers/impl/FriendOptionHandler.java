package org.virtue.network.protocol.handlers.impl;

import org.virtue.Constants;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.game.logic.node.interfaces.impl.FriendsList;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.protocol.messages.ClientScriptVar;
import org.virtue.network.session.impl.WorldSession;

/**
 * 
 * @author Virtue Development Team 2014 (c).
 */
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
		if (friendName.isEmpty()) {
			return;//No need to handle an empty name
		}
		Player player = session.getPlayer();
		switch (button) {
		case SEVEN:
			player.getPacketDispatcher().dispatchInterface(FriendsList.noteEdit);
			player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(8178));
			player.getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(9206, slot2, 0, 29556758, 29556742, 29556743));
			//System.out.println("Friend option one: name="+friendName+", slot1="+slot1+", slot2="+slot2);
			break;
		default:
			System.out.println("Received friend option: friend="+friendName+", slot1="+slot1+", slot2="+slot2+", button="+button.getID());
			break;
		}		
	}

}
