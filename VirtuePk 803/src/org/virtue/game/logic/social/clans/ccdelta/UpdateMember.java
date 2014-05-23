package org.virtue.game.logic.social.clans.ccdelta;

import org.virtue.game.logic.social.clans.ClanRank;
import org.virtue.network.protocol.packet.RS3PacketBuilder;

public class UpdateMember implements ClanChannelDelta {
	
	private final int memberIndex;
	private final String displayName;
	private final int nodeID;
	private final ClanRank rank;
	
	public UpdateMember (int memberIndex, String displayName, ClanRank rank, int nodeID) {
		this.memberIndex = memberIndex;
		this.displayName = displayName;
		this.rank = rank;
		this.nodeID = nodeID;
	}

	@Override
	public void packDelta(RS3PacketBuilder buffer) {
		buffer.put(0);//Unused
		buffer.putShort(memberIndex);
		buffer.put(rank.getID());
		buffer.putShort(nodeID);
		buffer.putLong(0L);//Unused
		buffer.putString(displayName);
		buffer.put(0);//Unused
	}

	@Override
	public int getTypeID() {
		return 5;
	}

}
