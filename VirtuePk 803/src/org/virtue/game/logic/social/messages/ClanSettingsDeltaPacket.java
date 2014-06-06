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
package org.virtue.game.logic.social.messages;

import org.virtue.game.logic.social.clans.csdelta.ClanSettingsDelta;

/**
 * An {@link Object} that represents an update to clan settings
 *
 * @author Sundays211
 */
public class ClanSettingsDeltaPacket {
	
	private final int updateNum;
	
	private final boolean isGuestCs;
	
	private final ClanSettingsDelta[] deltaNodes;
	
	public ClanSettingsDeltaPacket (boolean guestCs, int updateNum, ClanSettingsDelta... deltaNodes) {
		this.isGuestCs = guestCs;
		this.updateNum = updateNum;
		this.deltaNodes = deltaNodes;
	}
	
	/**
	 * Returns whether or not this is an update for a guest clan settings
	 * @return	True if this is a guest clan settings update, false if it's a main clan settings update
	 */
	public boolean isGuestUpdate () {
		return isGuestCs;
	}
	
	/**
	 * Returns the current settings update number
	 * @return	The update number
	 */
	public int getUpdateNumber () {
		return updateNum;
	}
	
	/**
	 * Returns the nodes for this delta update
	 * @return	The delta nodes
	 */
	public ClanSettingsDelta[] getDeltaNodes () {
		return deltaNodes;
	}

}
