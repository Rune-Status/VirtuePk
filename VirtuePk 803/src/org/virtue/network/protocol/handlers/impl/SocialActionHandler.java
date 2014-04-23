package org.virtue.network.protocol.handlers.impl;

import org.virtue.game.config.IncommingOpcodes;
import org.virtue.game.logic.social.FriendManager;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.session.impl.WorldSession;

public class SocialActionHandler extends PacketHandler<WorldSession> {

	@Override
	public void handle(WorldSession session) {
		FriendManager friendManager = session.getPlayer().getChatManager().getFriendManager();
		String name = getFlag("name", "");
		switch(getFlag("opcode", -1)) {
		case IncommingOpcodes.ADD_FRIEND_PACKET:
			friendManager.addFriend(name);
			break;
		case IncommingOpcodes.REMOVE_FRIEND_PACKET:
			friendManager.removeFriend(name);
			break;
		case IncommingOpcodes.ADD_IGNORE_PACKET:
			friendManager.addIgnore(name, getFlag("tillLogout", false));
			break;
		case IncommingOpcodes.REMOVE_IGNORE_PACKET:
			friendManager.removeIgnore(name);
			break;
		case IncommingOpcodes.JOIN_FRIEND_CHAT_PACKET:
			System.out.println("Unhandled request to join/leave friends chat: "+name);
			break;
		case IncommingOpcodes.IGNORE_NOTE_PACKET:
			friendManager.setNote(name, getFlag("note", ""), false);
			break;
		case IncommingOpcodes.FRIEND_NOTE_PACKET:
			friendManager.setNote(name, getFlag("note", ""), true);
			break;
		}
	}

}
