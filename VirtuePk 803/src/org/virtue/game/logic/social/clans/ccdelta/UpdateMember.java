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
 * An update which which changes the details of a user in the clan channel
 *
 * @author Sundays211
 */
public class UpdateMember implements ClanChannelDelta {
	
	private final int memberIndex;
	private final String displayName;
	private final int nodeID;
	private final ClanRank rank;
	
	/**
	 * Constructs a new {@code UpdateMember} delta object
	 * @param memberIndex	The index of the member to update within the clan channel
	 * @param displayName	The display name to change to
	 * @param rank			The rank to change to
	 * @param nodeID		The world node ID to change to
	 */
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
