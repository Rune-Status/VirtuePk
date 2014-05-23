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
package org.virtue.game.logic.social.internal;

/**
 * An {@link Object} that represents an entry on a player's ignore list
 *
 * @author Sundays211
 *
 */
public class Ignore {
	public final String username;
	private String currentName = "";
	private String previousName = "";
	private final boolean temporary;
	private String note;
	
	public Ignore (String username, boolean temporary) {
		this(username, temporary, "");
	}
	
	public Ignore (String username, boolean temporary, String note) {
		this.username = username;
		this.note = note;
		this.temporary = temporary;
	}
	
	public void setDisplayNames (String current, String previous) {
		this.currentName = current;
		this.previousName = previous;
	}	
	
	public String getName () {
		return currentName;
	}
	
	public String getPreviousName () {
		return previousName;
	}
	
	protected void setNote (String note) {
		this.note = note;
	}
	
	public String getNote () {
		return note;
	}
	
	public boolean isTemporary () {
		return temporary;
	}
	
	@Override
	public Ignore clone () {
		Ignore i = new Ignore(username, temporary);
		i.currentName = this.currentName;
		i.previousName = this.previousName;
		i.note = this.note;
		return i;
	}
}
