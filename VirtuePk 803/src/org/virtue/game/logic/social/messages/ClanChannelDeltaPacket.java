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

import org.virtue.game.logic.social.clans.ccdelta.ClanChannelDelta;

/**
 * An {@link Object} that represents an update to a clan chat channel
 *
 * @author Sundays211
 */
public class ClanChannelDeltaPacket {
	
	private final long clanHash;
	
	private final long updateNum;
	
	private final boolean isGuestCc;
	
	private final ClanChannelDelta[] deltaNodes;
	
	public ClanChannelDeltaPacket (boolean guestCc, long clanHash, long updateNum, ClanChannelDelta... deltaNodes) {
		this.isGuestCc = guestCc;
		this.clanHash = clanHash;
		this.updateNum = updateNum;
		this.deltaNodes = deltaNodes;
	}
	
	/**
	 * Returns whether or not this is an update for a guest clan chat
	 * @return	True if this is a guest clan channel update, false if it's a main clan channel update
	 */
	public boolean isGuestUpdate () {
		return isGuestCc;
	}
	
	/**
	 * Returns the unique hash code for the clan
	 * @return	The clan hash
	 */
	public long getClanHash () {
		return clanHash;
	}
	
	/**
	 * Returns the current channel update number
	 * @return	The update number
	 */
	public long getUpdateNumber () {
		return updateNum;
	}
	
	/**
	 * Returns the nodes for this delta update
	 * @return	The delta nodes
	 */
	public ClanChannelDelta[] getDeltaNodes () {
		return deltaNodes;
	}
}
