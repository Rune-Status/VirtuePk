package org.virtue.game.logic.social.clans.ccdelta;

import org.virtue.game.logic.social.clans.ClanRank;
import org.virtue.network.protocol.packet.RS3PacketBuilder;

public class AddMember implements ClanChannelDelta {
	
	private final ClanRank rank;
	private final String displayName;
	private final int nodeID;
	
	public AddMember (String displayName, ClanRank rank, int nodeID) {
		this.displayName = displayName;
		this.rank = rank;
		this.nodeID = nodeID;
	}

	@Override
	public void packDelta(RS3PacketBuilder buffer) {
		buffer.put(255);//Do not include user hash
		buffer.putString(displayName);
		buffer.putShort(nodeID);
		buffer.put(rank.getID());
		buffer.putLong(0L);//Currently unused
	}

	@Override
	public int getTypeID() {
		return 1;
	}

}
