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
 * Represents a clan settings variable
 *
 * @author Sundays211
 * @since May 30, 2014
 */
public class VarClanSettings {
	public static enum Type {INTEGER, STRING, LONG}
	
	private Type type;
	
	private Object value;
	
	private int key;
	
	public VarClanSettings (int key, Type type) {
		this.type = type;
		this.key = key;
		switch (type) {
		case INTEGER:
			value = 0;
		case STRING:
			value = "";
		case LONG:
			value = 0L;
		}
	}
	
	public VarClanSettings (int key, Type type, Object value) {
		this.type = type;
		this.key = key;
		this.value = value;
	}
	
	/**
	 * Returns the type of this clan settings variable
	 * @return	The type
	 */
	public Type getType () {
		return type;
	}
	
	/**
	 * Returns the key for the variable
	 * @return	The key
	 */
	public int getKey () {
		return key;
	}
	
	/**
	 * Returns the value for this variable. The type should match the type specified by getType()
	 * @return	The value
	 */
	public Object getValue () {
		return value;
	}
}
