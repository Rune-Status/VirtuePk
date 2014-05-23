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
 * @author Virtue Development Team 2014 (c).
 * @since Apr 17, 2014
 */
public enum ChatType {
	
	PUBLIC(0),
	FRIENDS_CHAT(1),
	CLAN(2),
	GUEST_CLAN(3),
	PRIVATE(4);
	
	private int code;
	
	ChatType(int code) {
		this.code = code;
	}
	
	public int getCode () {
		return code;
	}
	
	public static ChatType forCode (int code) {
		if (code < ChatType.values().length) {
			ChatType type = ChatType.values()[code];
			if (type.code == code) {
				return type;
			}
		}
		for (ChatType t : ChatType.values()) {
			if (t.code == code) {
				return t;
			}
		}
		return null;
	}
}
