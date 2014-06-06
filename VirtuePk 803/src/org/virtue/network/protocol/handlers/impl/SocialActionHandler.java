package org.virtue.network.protocol.handlers.impl;

import org.virtue.game.config.IncommingOpcodes;
import org.virtue.game.logic.social.ChannelRank;
import org.virtue.game.logic.social.FriendManager;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.session.impl.WorldSession;

/**
 * 
 * @author Virtue Development Team 2014 (c).
 */
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
			session.getPlayer().getChatManager().handleFriendsChatJoin(name);
			break;
		case IncommingOpcodes.FRIENDS_CHAT_KICK_PACKET:
			session.getPlayer().getChatManager().handleFriendsChatKick(name);
			break;
		case IncommingOpcodes.IGNORE_NOTE_PACKET:
			friendManager.setNote(name, getFlag("note", ""), false);
			break;
		case IncommingOpcodes.FRIEND_NOTE_PACKET:
			friendManager.setNote(name, getFlag("note", ""), true);
			break;
		case IncommingOpcodes.CHANGE_FRIEND_RANK_PACKET:
			friendManager.setFriendRank(name, ChannelRank.forID(getFlag("rank", -1)));
			break;
		}
	}

}
