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
package org.virtue.game.logic.social.messages;

import org.virtue.game.logic.social.internal.Ignore;

/**
 * An {@link Object} that represents an update to the player's ignore list
 *
 * @author Sundays211
 */
public class IgnoresPacket {
	
	public static class Entry {
		
		public static Entry fromIgnoreObject (Ignore ignore) {
			return new Entry(ignore.getName(), ignore.getPreviousName(), ignore.isTemporary(), ignore.getNote());
		}
		
		public final String currentName;
		public final String previousName;
		public final boolean temporary;
		public final String note;
		
		public Entry (String currentName, String previousName, boolean temporary, String note) {
			this.currentName = currentName;
			this.previousName = previousName;
			this.temporary = temporary;
			this.note = note;
		}
	}
	
	private Entry[] ignores;
	
	private boolean isNameChange;
	private boolean fullUpdate;
	
	public IgnoresPacket (Entry[] ignores) {
		this.ignores = ignores;
		fullUpdate = true;
	}
	
	public IgnoresPacket (Entry ignore, boolean isNameChange) {
		ignores = new Entry[] { ignore };
		this.isNameChange = isNameChange;
	}
	
	public boolean isFullUpdate () {
		return fullUpdate;
	}
	
	public boolean isNameChange () {
		return isNameChange;
	}
	
	public Entry[] getIgnores () {
		return ignores;
	}
}
