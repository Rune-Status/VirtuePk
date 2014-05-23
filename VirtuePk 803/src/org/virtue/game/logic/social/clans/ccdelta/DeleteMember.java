package org.virtue.game.logic.social.clans.ccdelta;

import org.virtue.network.protocol.packet.RS3PacketBuilder;

public class DeleteMember implements ClanChannelDelta {
	
	private final int memberIndex;
	
	public DeleteMember (int memberIndex) {
		this.memberIndex = memberIndex;
	}

	@Override
	public void packDelta(RS3PacketBuilder buffer) {
		buffer.putShort(memberIndex);
		buffer.put(0);//Not used
		buffer.put(255);//Do not include user hash
	}

	@Override
	public int getTypeID() {
		return 2;
	}

}
