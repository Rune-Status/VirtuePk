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

import org.virtue.Launcher;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.social.internal.SocialUser;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;
import org.virtue.utility.GameClock;

/**
 * Contains the tools necessary for managing clans.
 *
 * @author Sundays211
 * @since May 22, 2014
 */
public class ClanChannelManager {
	
	private Map<Long, ClanChannel> clanCache = Collections.synchronizedMap(new HashMap<Long, ClanChannel>());
	
	private final ClanManager clanManager;
	
	//private HashMap<String, Long> clanNameResolver = new HashMap<String, Long>();
	
	public ClanChannelManager (ClanManager clanManager) {
		this.clanManager = clanManager;
		System.out.println("Initialising clan channel manager.");
		Launcher.getEngine().registerLogicEvent(new Runnable() {
			@Override
			public void run() {
				synchronized (clanCache) {
					//System.out.println("Updating channels.");
					for (ClanChannel channel : clanCache.values()) {
						channel.dispatchUpdates();//Run through any pending tick events for the channel
					}
				}
			}			
		}, GameClock.ONE_TICK, GameClock.ONE_TICK);
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
	
	public void joinChannel(Player player, long clanHash) {
		//System.out.println("Joining channel "+clanHash);
		ClanChannel channel = getClanChannel(clanHash);
		if (channel == null) {
			//TODO: Find clan chat system message opcode
			player.getPacketDispatcher().dispatchMessage("Error loading clan. Please try again later.");
			return;//Clan does not exist
		}
		if (!channel.getSettings().inClan(player.getAccount().getUsername().getAccountNameAsProtocol())) {
			//TODO: Is a message needed here?
			return;
		}
		channel.join(new SocialUser(player), false);
		player.getChatManager().setInClanChannel(true);
	}
	
	public void joinGuestChannel (Player player, String clanName) {
		long clanHash = clanManager.getClanIndex().resolveClan(clanName);
		if (clanHash == 0L) {
			player.getPacketDispatcher().dispatchMessage("Could not find a clan named "+clanName+". Please check the name and try again.");
			return;
		}
		ClanChannel channel = getClanChannel(clanHash);
		if (channel == null) {
			player.getPacketDispatcher().dispatchMessage("Could not find a clan named "+clanName+". Please check the name and try again.");
			return;
		}
		if (channel.getSettings().inClan(player.getAccount().getUsername().getAccountNameAsProtocol())) {
			player.getPacketDispatcher().dispatchMessage("You cannot join your clan's chat channel as a guest.");
			return;
		}
		if (!channel.getSettings().allowNonMembers()) {
			player.getPacketDispatcher().dispatchMessage("That clan only allows clanmates to join their clan channel.");
			return;
		}
		//TODO: Check whether the player is banned and whether the channel is full
		channel.join(new SocialUser(player), true);
		player.getChatManager().setGuestClanHash(clanHash);
	}
	
	public void leaveChannel (Player player, boolean isGuest) {
		long clanHash = isGuest ? player.getChatManager().getGuestClanHash() : player.getChatManager().getMyClanHash();
		ClanChannel channel = getClanChannel(clanHash);
		if (channel == null) {
			//Clan channel doesn't exist
			return;
		}
		channel.leave(new SocialUser(player), isGuest);
		if (isGuest) {
			player.getChatManager().setGuestClanHash(0L);
		} else {
			player.getChatManager().setInClanChannel(false);
		}
	}
	
	public void sendMessage (Player player, String message, boolean isGuest) {
		SocialUser user = new SocialUser(player);
		long clanHash = isGuest ? player.getChatManager().getGuestClanHash() : player.getChatManager().getMyClanHash();
		ClanChannel channel = getClanChannel(clanHash);
		if (channel == null) {
			user.sendGameMessage("Not in channel.", MessageOpcode.CLAN_SYSTEM);//TODO: Fix message
			return;
		}
		if (!channel.canTalk(user.getProtocolName())) {
			user.sendGameMessage("Cannot talk.", MessageOpcode.CLAN_SYSTEM);//TODO: Fix message
			return;
		}
	}
}
