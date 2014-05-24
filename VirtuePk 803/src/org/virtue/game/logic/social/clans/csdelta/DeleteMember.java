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
package org.virtue.game.logic.social.clans.csdelta;

import org.virtue.network.protocol.packet.RS3PacketBuilder;

/**
 * An update which removes the specified member from the clan list
 *
 * @author Sundays211
 */
public class DeleteMember implements ClanSettingsDelta {

	private final int memberIndex;
	
	/**
	 * Creates a new {@code DeleteMember} delta object
	 * @param memberIndex	The index of the member to remove from the clan list
	 */
	public DeleteMember (int memberIndex) {
		this.memberIndex = memberIndex;
	}
	
	@Override
	public void packDelta(RS3PacketBuilder buffer) {
		buffer.putShort(memberIndex);
	}

	@Override
	public int getTypeID() {
		return 5;
	}

}
