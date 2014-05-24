/*
 * This file is part of the RS3Emulator social module.
 *
 * RS3Emulator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RS3Emulator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RS3Emulator.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.virtue.game.logic.social.clans.ccdelta;

import org.virtue.game.logic.social.clans.ClanRank;
import org.virtue.network.protocol.packet.RS3PacketBuilder;

/**
 * An update which changes the details of a clan channel
 *
 * @author Sundays211
 */
public class UpdateDetails implements ClanChannelDelta {
	
	private final String channelName;
	private final ClanRank minTalk;
	private final ClanRank minKick;
	
	public UpdateDetails (String channelName) {
		this(channelName, ClanRank.OWNER, ClanRank.OWNER);
	}
	
	/**
	 * Constructs a new {@code UpdateDetails} delta object
	 * @param channelName	The name of the clan channel
	 * @param minTalk		The minimum rank needed to send messages
	 * @param minKick		The minimum rank needed to remove other members
	 */
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
