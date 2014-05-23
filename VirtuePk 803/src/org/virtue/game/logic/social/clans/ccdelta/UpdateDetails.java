package org.virtue.game.logic.social.clans.ccdelta;

import org.virtue.game.logic.social.clans.ClanRank;
import org.virtue.network.protocol.packet.RS3PacketBuilder;

public class UpdateDetails implements ClanChannelDelta {
	
	private final String channelName;
	private final ClanRank minTalk;
	private final ClanRank minKick;
	
	public UpdateDetails (String channelName) {
		this(channelName, ClanRank.OWNER, ClanRank.OWNER);
	}
	
	public UpdateDetails (String channelName, ClanRank minTalk, ClanRank minKick) {
		this.channelName = (channelName == null ? "" : channelName);
		this.minTalk = minTalk;
		this.minKick = minKick;
	}

	@Override
	public void packDelta(RS3PacketBuilder buffer) {
		buffer.putString(channelName);
		if (!channelName.isEmpty()) {
			buffer.put(0);//Unused
			buffer.put(minTalk.getID());
			buffer.put(minKick.getID());
		}
	}

	@Override
	public int getTypeID() {
		return 4;
	}

}
