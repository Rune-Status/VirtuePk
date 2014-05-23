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

import org.virtue.game.logic.social.ChannelRank;
import org.virtue.game.logic.social.internal.Friend;

/**
 * An {@link Object} that represents an update to the player's friends list
 *
 * @author Sundays211
 */
public class FriendsPacket {
	
	public static class Entry {
		
		public static Entry fromFriendObject (Friend friend) {
			return new Entry(friend.getName(), friend.getPrevName(), friend.isReferred(), 
					friend.getWorldNodeID(), friend.getWorldName(), friend.getWorldFlags(), 
					friend.getFcRank(), friend.getNote());
		}
		
		public final String currentName;
		public final String previousName;
		public final boolean isReferred;
		public final String worldName;
		public final int worldNodeID;
		public final int worldFlags;
		public final ChannelRank friendsChatRank;
		public final String note;
		
		public Entry (String currentName, String previousName, boolean referred, int worldNodeID, 
				String worldName, int worldFlags, ChannelRank fcRank, String note) {
			this.currentName = currentName;
			this.previousName = previousName;
			this.isReferred = referred;
			this.worldNodeID = worldNodeID;
			this.worldName = worldName;
			this.worldFlags = worldFlags;
			this.friendsChatRank = fcRank;
			this.note = note;
		}
	}

	private Entry[] friends;
	
	private boolean isNameChange;
	private boolean fullUpdate;
	
	public FriendsPacket (Entry[] friends) {
		this.friends = friends;
		fullUpdate = true;
	}
	
	public FriendsPacket (Entry friend, boolean isNameChange) {
		this.isNameChange = isNameChange;
		friends = new Entry[] { friend };
	}
	
	public boolean isFullUpdate () {
		return fullUpdate;
	}
	
	public boolean isNameChange () {
		return isNameChange;
	}
	
	public Entry[] getFriends () {
		return friends;
	}
}
