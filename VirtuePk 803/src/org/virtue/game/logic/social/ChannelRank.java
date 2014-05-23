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
 * Represents a player's rank within a friends chat channel
 * 
 * @author Sundays211
 */
public enum ChannelRank {
	GUEST(-1, "Guest"), FRIEND(0, "Friend"), RECRUIT(1, "Recruit"), CORPORAL(2, "Corporal"), 
	SERGEANT(3, "Sergeant"), LIEUTENANT(4, "Lieutenant"), CAPTAIN(5, "Captain"), 
	GENERAL(6, "General"), OWNER(7, "Channel Owner"), JMOD(127, "Jagex Moderator");
	
	private final int id;
	private final String name;
	
	ChannelRank(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getID () {
		return id;
	}
	
	public String getName () {
		return name;
	}
	
	public static ChannelRank forID (int id) {
		if (id <= ChannelRank.values().length && id > -2) {
			ChannelRank rank = ChannelRank.values()[id+1];
			if (rank.id == id) {
				return rank;
			}
		}
		for (ChannelRank r : ChannelRank.values()) {
			if (r.id == id) {
				return r;
			}
		}
		return null;
	}
}
