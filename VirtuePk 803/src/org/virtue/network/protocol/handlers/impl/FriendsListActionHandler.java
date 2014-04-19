package org.virtue.network.protocol.handlers.impl;

import org.virtue.game.config.IncommingOpcodes;
import org.virtue.game.logic.social.FriendManager;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.session.impl.WorldSession;

public class FriendsListActionHandler extends PacketHandler<WorldSession> {

	@Override
	public void handle(WorldSession session) {
		FriendManager friendManager = session.getPlayer().getChatManager().getFriendManager();
		String displayName = getFlag("displayName", "");
		switch(getFlag("opcode", -1)) {
		case IncommingOpcodes.ADD_FRIEND_PACKET:
			friendManager.addFriend(displayName);
			break;
		case IncommingOpcodes.REMOVE_FRIEND_PACKET:
			friendManager.removeFriend(displayName);
			break;
		case IncommingOpcodes.ADD_IGNORE_PACKET:
			friendManager.addIgnore(displayName, getFlag("tillLogout", false));
			break;
		case IncommingOpcodes.REMOVE_IGNORE_PACKET:
			friendManager.removeIgnore(displayName);
			break;
		}
	}

}
