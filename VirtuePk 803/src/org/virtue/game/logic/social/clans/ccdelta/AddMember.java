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
 * An update which adds the specified member to the clan channel
 *
 * @author Sundays211
 */
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
