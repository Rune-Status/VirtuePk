/*
 * This file is part of RS3Emulator.
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
package org.virtue.game.logic.social;

/**
 * An {@link Enum} that represents a permission within a friends chat channel
 *
 * @author Sundays211
 *
 **/
public enum ChannelPermission {
	JOIN(2, -1), TALK(3, -1), KICK(4, 2), LOOTSHARE(5, 0);
	
	private final int settingsID;
	private final int minRank;
	
	ChannelPermission(int settingsID, int minRank) {
		this.settingsID = settingsID;
		this.minRank = minRank;
	}
	
	public int getSettingsID () {
		return settingsID;
	}
	
	public int getMinRank () {
		return minRank;
	}
	
	public static ChannelPermission forComponent (int compID) {
		if ((compID-2) >= 0 && (compID-2) < ChannelPermission.values().length) {
			ChannelPermission p = ChannelPermission.values()[compID-2];
			if (p.settingsID == compID) {
				return p;
			}
		}
		for (ChannelPermission p : ChannelPermission.values()) {
			if (p.settingsID == compID) {
				return p;
			}
		}
		return null;
	}
}