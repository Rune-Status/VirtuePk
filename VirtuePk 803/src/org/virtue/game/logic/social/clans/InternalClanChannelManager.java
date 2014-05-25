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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.virtue.game.logic.social.SocialUser;
import org.virtue.game.logic.social.messages.ClanChannelMessage;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;

/**
 * Contains the tools necessary for managing clan chat channels.
 *
 * @author Sundays211
 * @since May 22, 2014
 */
public class InternalClanChannelManager implements ClanChannelManager {
	
	private Map<Long, ClanChannel> clanCache = Collections.synchronizedMap(new HashMap<Long, ClanChannel>());
	
	private final ClanManager clanManager;
	
	public InternalClanChannelManager (ClanManager clanManager) {
		this.clanManager = clanManager;
		System.out.println("Initialising clan channel manager.");
	}
	
	/**
	 * Runs the update tasks for a clan, such as dispatching any delta updates to the players in the clan channel
	 */
	protected void runUpdateTasks () {
		synchronized (clanCache) {
			//System.out.println("Updating channels.");
			for (ClanChannel channel : clanCache.values()) {
				channel.dispatchUpdates();//Run through any pending tick events for the channel
			}
		}
	}
	
	private ClanChannel getClanChannel (long clanHash) {
		if (clanCache.containsKey(clanHash)) {
			return clanCache.get(clanHash);
		}	
		ClanSettings settings = clanManager.getClanData(clanHash);
		if (settings == null) {
			return null;//Clan does not exist
		}
		clanCache.put(clanHash, new ClanChannel(settings));
		return clanCache.get(clanHash);
	}
	
	@Override
	public void joinMyChannel(SocialUser player) {
		long clanHash = player.getMyClanHash();
		//System.out.println("Joining channel "+clanHash);
		ClanChannel channel = getClanChannel(clanHash);
		if (channel == null) {
			//TODO: Find clan chat system message opcode
			player.sendSystemMessage("Error loading clan. Please try again later.", MessageOpcode.CLAN_SYSTEM);
			return;//Clan does not exist
		}
		if (!channel.getSettings().inClan(player.getProtocolName())) {
			//TODO: Is a message needed here?
			return;
		}
		channel.join(player, false);
		player.setInClanChannel(true);
	}
	
	@Override
	public void joinGuestChannel (SocialUser player, String clanName) {
		long clanHash = clanManager.getClanIndex().resolveClan(clanName);
		if (clanHash == 0L) {
			player.sendSystemMessage("Could not find a clan named "+clanName+". Please check the name and try again.", MessageOpcode.CLAN_SYSTEM);
			return;
		}
		ClanChannel channel = getClanChannel(clanHash);
		if (channel == null) {
			player.sendSystemMessage("Could not find a clan named "+clanName+". Please check the name and try again.", MessageOpcode.CLAN_SYSTEM);
			return;
		}
		if (channel.getSettings().inClan(player.getProtocolName())) {
			player.sendSystemMessage("You cannot join your clan's chat channel as a guest.", MessageOpcode.CLAN_SYSTEM);
			return;
		}
		if (!channel.getSettings().allowNonMembers()) {
			player.sendSystemMessage("That clan only allows clanmates to join their clan channel.", MessageOpcode.CLAN_SYSTEM);
			return;
		}
		//TODO: Check whether the player is banned and whether the channel is full
		//System.out.println("Joining clan "+clanName+" of hash "+clanHash+" as a guest.");
		channel.join(player, true);
		player.setGuestClanHash(clanHash);
	}
	
	@Override
	public void leaveChannel (SocialUser player, boolean isGuest, boolean isLogout) {
		//InternalSocialUser user = new InternalSocialUser(player);
		long clanHash = isGuest ? player.getGuestClanHash() : player.getMyClanHash();
		if (clanHash == 0L) {
			return;
		}
		ClanChannel channel = getClanChannel(clanHash);
		if (channel == null) {
			if (!isLogout) {
				player.sendLeaveClanChannel(isGuest);
			}
			return;
		}
		channel.leave(player, isGuest);
		if (!isLogout) {
			player.sendLeaveClanChannel(isGuest);
		}		
	}
	
	@Override
	public void sendMessage (SocialUser user, String message, boolean isGuest) {
		//InternalSocialUser user = new InternalSocialUser(player);
		long clanHash = isGuest ? user.getGuestClanHash() : user.getMyClanHash();
		ClanChannel channel = getClanChannel(clanHash);
		if (channel == null) {
			user.sendSystemMessage("You aren't"+(isGuest?" a guest":"")+" in a"+(isGuest?" visited":"")+" Clan Chat channel.", MessageOpcode.CLAN_SYSTEM);
			return;
		}
		if (!channel.canTalk(user.getProtocolName())) {
			user.sendSystemMessage("Cannot talk.", MessageOpcode.CLAN_SYSTEM);//TODO: Fix message
			return;
		}
		ClanChannelMessage mainMessage = new ClanChannelMessage(false, message, user.getDisplayName(), user.getRights());
		ClanChannelMessage guestMessage = new ClanChannelMessage(mainMessage, true);
		channel.sendMessage(mainMessage, guestMessage);
	}
}
