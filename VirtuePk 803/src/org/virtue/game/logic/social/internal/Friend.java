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

import org.virtue.game.logic.WorldHub;
import org.virtue.game.logic.social.ChannelRank;
import org.virtue.utility.StringUtils;
import org.virtue.utility.StringUtils.FormatType;

/**
 * An {@link Object} that represents a player's friend
 *
 * @author Sundays211
 */
public class Friend {
	public final String username;
	private String currentName;
	private String previousName;
	private WorldHub currentWorld = null;
	private String worldName = "";
	private int worldNodeID = 0;
	private int worldFlags = 0;
	private ChannelRank friendsChatRank;
	private boolean isReferred = false;
	private String note = "";
	
	public Friend (String username, boolean referred) {
		this(username, referred, ChannelRank.FRIEND, "");
	}
	
	public Friend (String username, boolean referred, ChannelRank fcRank, String note) {
		this.username = username;
		this.currentName = StringUtils.format(username, FormatType.NAME);
		this.isReferred = referred;
		this.friendsChatRank = fcRank;
		this.note = note;
	}
	
	public void setDisplayNames (String current, String previous) {
		this.currentName = current;
		this.previousName = previous;
	}
	
	public void setWorld (WorldHub world) {
		this.currentWorld = world;
		if (world == null) {//TODO: Handle lobby in a better way
			this.worldNodeID = 0;
			this.worldName = "";
			this.worldFlags = 0;
		} else {
			this.worldNodeID = world.getNodeID();
			this.worldName = world.getName();
			this.worldFlags = world.getFlags();
		}
	}
	
	public void setWorld (int nodeID, String name, int flags) {
		this.worldNodeID = nodeID;
		this.worldName = name;
		this.worldFlags = flags;
	}
	
	protected void setFcRank (ChannelRank rank) {
		this.friendsChatRank = rank;
	}
	
	protected void setNote (String note) {
		this.note = note;
	}
	
	public String getName () {
		return currentName;
	}
	
	public String getPrevName () {
		return previousName;
	}
	
	public WorldHub getWorld () {
		return currentWorld;
	}
	
	public int getWorldNodeID () {
		return worldNodeID;
	}
	
	public String getWorldName () {
		return worldName;
	}
	
	public int getWorldFlags () {
		return worldFlags;
	}
	
	public ChannelRank getFcRank () {
		return friendsChatRank;
	}
	
	public boolean isReferred () {
		return isReferred;
	}
	
	public String getNote () {
		return note;
	}
	
	@Override
	public Friend clone () {
		Friend f = new Friend(username, isReferred);
		f.currentName = this.currentName;
		f.previousName = this.previousName;
		f.friendsChatRank = this.friendsChatRank;
		f.note = this.note;
		f.worldName = this.worldName;
		f.worldNodeID = this.worldNodeID;
		f.worldFlags = this.worldFlags;
		return f;
	}
}
