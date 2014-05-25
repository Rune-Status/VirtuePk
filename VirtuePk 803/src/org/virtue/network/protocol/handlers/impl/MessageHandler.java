package org.virtue.network.protocol.handlers.impl;

import org.virtue.game.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.session.impl.WorldSession;

public class MessageHandler extends PacketHandler<WorldSession> {

	@Override
	public void handle(WorldSession session) {
		String message = getFlag("message", "");		
		if (message.isEmpty()) {
			return;//No point in handling an empty message
		}
		
		if (getFlag("opcode", -1) == IncommingOpcodes.PRIVATE_MESSAGE_PACKET) {
			String recipient = getFlag("recipient", "");
			if (recipient.isEmpty()) {
				return;//No recipient specified; don't bother handling it
			}
			session.getPlayer().getChatManager().getFriendManager().sendPrivateMessage(recipient, message);
		} else {			
			int colourEffect = getFlag("colourEffect", 0);
			int moveEffect = getFlag("moveEffect", 0);
			boolean isGuest = false;
			switch (session.getPlayer().getChatManager().getChatType()) {
			case PUBLIC:
				session.getPlayer().getChatManager().handlePublicMessage(message, colourEffect, moveEffect);
				break;
			case FRIENDS_CHAT:
				session.getPlayer().getChatManager().handleFriendsChatMessage(message);
				break;
			case GUEST_CLAN:
				isGuest = true;
			case CLAN:
				session.getPlayer().getChatManager().handleClanChannelMessage(message, isGuest);
				break;
			default:
				System.out.println("Unhandled message: message="+message+", type="+session.getPlayer().getChatManager().getChatType());
				break;	
			}
						
		}
		
	}

}
