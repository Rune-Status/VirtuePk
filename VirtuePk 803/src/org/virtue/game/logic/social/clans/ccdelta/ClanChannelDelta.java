package org.virtue.game.logic.social.clans.ccdelta;

import org.virtue.network.protocol.packet.RS3PacketBuilder;

public interface ClanChannelDelta {
	
	public void packDelta (RS3PacketBuilder buffer);
	
	public int getTypeID ();
}
