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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.virtue.game.logic.social.clans.ccdelta.AddMember;
import org.virtue.game.logic.social.clans.ccdelta.ClanChannelDelta;
import org.virtue.game.logic.social.clans.ccdelta.DeleteMember;
import org.virtue.game.logic.social.clans.ccdelta.UpdateMember;
import org.virtue.game.logic.social.internal.SocialUser;
import org.virtue.game.logic.social.messages.ClanChannelPacket;

/**
 * Represents a clan chat channel
 *
 * @author Sundays211
 */
public class ClanChannel {
	
	private final ClanSettings clanData;
	
	private final Queue<ClanChannelDelta> updates = new LinkedList<ClanChannelDelta>();
	
	private final Queue<SocialUser> initQueue = new LinkedList<SocialUser>();
	
	private final List<SocialUser> users = new ArrayList<SocialUser>();
	
	private long updateNumber;
	
	public ClanChannel (ClanSettings data) {
		this.clanData = data;
		clanData.linkChannel(this);
		updateNumber = 1L;
	}
	
	/**
	 * Returns the underlying data for the clan.
	 * @return	The clan data
	 */
	protected ClanSettings getSettings () {
		return clanData;
	}
	
	public boolean join (SocialUser player, boolean isGuest) {
		//TODO: Check whether the channel is full
		synchronized (users) {
			if (initQueue.contains(player) || getUser(player.getProtocolName()) != null) {
				return false;//Join already in progress
			}
			queueUpdate(new AddMember(player.getDisplayName(), clanData.getRank(player.getProtocolName()), player.getWorldID()));
			initQueue.offer(player);
		}
		return true;
	}
	
	/**
	 * Removes the player from the clan chat channel
	 * @param player	The player to remove
	 * @param isGuest	Whether the user is leaving a guest clan channel
	 * @return			True if the player was the last user in the channel (and therefore is empty), false otherwise
	 */
	public boolean leave (SocialUser player, boolean isGuest) {
		removeUser(player);
		player.sendLeaveClanChannel(isGuest);
		return users.isEmpty();
	}
	
	private void removeUser (SocialUser user) {
		int index;
		synchronized (users) {
			index = users.indexOf(user);
			if (index == -1) {
				return;
			}
			users.remove(index);
		}		
		queueUpdate(new DeleteMember(index));
	}
	
	public SocialUser getUser (String protocolName) {
		for (SocialUser u : users) {
			if (u.getProtocolName().equalsIgnoreCase(protocolName)) {
				return u;
			}
		}
		return null;
	}
	
	public boolean canTalk (String protocolName) {
		return clanData.getMinTalk().getID() <= clanData.getRank(protocolName).getID();
	}
	
	/**
	 * Queues an update to the clan channel which will be sent on the next tick
	 * @param node	The update node to queue
	 */
	protected void queueUpdate (ClanChannelDelta node) {
		synchronized (updates) {
			updates.offer(node);
		}
	}
	
	/**
	 * Queues an update packet for the user with the specified name within the channel. If the user is not in the channel, no update is sent
	 * @param protocolName	The name of the user to update
	 */
	protected void updateUser (String protocolName) {
		//int index;
		SocialUser member = null;
		synchronized (users) {
			for (SocialUser u : users) {
				//SocialUser u = users.get(index);
				if (u.getProtocolName().equalsIgnoreCase(protocolName)) {
					member = u;
					break;
				}
			}
		}
		if (member != null) {
			synchronized (updates) {
				int index = users.indexOf(member);
				queueUpdate(new UpdateMember(index, member.getDisplayName(), clanData.getRank(protocolName), member.getWorldID()));
			}
		}
	}
	
	/**
	 * Sends the clan channel delta updates to everyone currently in the channel
	 */
	protected void dispatchUpdates () {
		if (updates.isEmpty()) {
			sendInitPackets();
			return;
		}
		ClanChannelDelta[] deltaNodes;
		long thisUpdate = updateNumber;
		synchronized (updates) {
			deltaNodes = new ClanChannelDelta[updates.size()];
			updates.toArray(deltaNodes);
			updates.clear();
			updateNumber++;
		}
		synchronized (users) {
			for (SocialUser u : users) {
				u.sendClanChannelDelta(!u.isMyClan(clanData.getClanHash()), clanData.getClanHash(), thisUpdate, deltaNodes);
			}
			sendInitPackets();
		}
	}
	
	/**
	 * Sends any queued initialisation packets
	 */
	private void sendInitPackets () {
		if (initQueue.isEmpty()) {
			return;
		}
		ClanChannelPacket.User[] entries;
		synchronized (users) {
			for (SocialUser u : initQueue) {
				users.add(u);
			}
			entries = new ClanChannelPacket.User[users.size()];
			for (int i=0;i<users.size();i++) {
				SocialUser u = users.get(i);
				entries[i] = new ClanChannelPacket.User(u.getDisplayName(), clanData.getRank(u.getProtocolName()), u.getWorldID());
			}
		}
		SocialUser u = null;
		while ((u = initQueue.poll()) != null) {
			sendInitPacket(u, entries, !u.isMyClan(clanData.getClanHash()));
		}
	}
	
	/**
	 * Sends the channel initialisation packet to the specified user
	 * @param user		The user to send the init packet to
	 * @param entires	An array of users currently in the channel
	 * @param isGuest	Whether the user is a guest in this channel
	 */
	private void sendInitPacket (SocialUser user, ClanChannelPacket.User[] entries, boolean isGuest) {
		ClanChannelPacket packet = new ClanChannelPacket(isGuest, entries, clanData.getClanHash(),
				updateNumber, clanData.getClanName(), clanData.getMinTalk(), clanData.getMinKick());
		user.sendClanChannelFull(packet);
	}
	
	@Override
	public boolean equals (Object anObject) {
		if (this == anObject) {
            return true;
        }
		if (anObject instanceof ClanChannel) {
			ClanChannel anotherChannel = (ClanChannel) anObject;
			return anotherChannel.clanData.getClanHash() == this.clanData.getClanHash();
		}
		return false;
		
	}
}
