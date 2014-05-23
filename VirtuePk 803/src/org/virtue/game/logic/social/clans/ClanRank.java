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
package org.virtue.game.logic.social.clans;

/**
 * Represents a player's rank within a clan
 *
 * @author Sundays211
 */
public enum ClanRank {
	GUEST(-1, "Guest"), RECRUIT(0, "Recruit"), CORPORAL(1, "Corporal"), 
	SERGEANT(2, "Sergeant"), LIEUTENANT(3, "Lieutenant"), CAPTAIN(4, "Captain"), 
	GENERAL(5, "General"), ADMIN(100, "Admin"), ORGANISER(101, "Organiser"),
	COORDINATOR(102, "Coordinator"), OVERSEER(103, "Overseer"), DEPUTY_OWNER(125,"Deputy Owner"), 
	OWNER(126, "Owner"), JMOD(127, "Jagex Moderator");
	
	private final int id;
	private final String name;
	
	ClanRank (int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getID () {
		return id;
	}
	
	public String getName () {
		return name;
	}
	
	public static ClanRank forID (int id) {
		if (id <= ClanRank.values().length && id > -2) {
			ClanRank rank = ClanRank.values()[id+1];
			if (rank.id == id) {
				return rank;
			}
		}
		for (ClanRank r : ClanRank.values()) {
			if (r.id == id) {
				return r;
			}
		}
		return null;
	}
}
