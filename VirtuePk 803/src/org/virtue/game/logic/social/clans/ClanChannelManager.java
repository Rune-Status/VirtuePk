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

import java.util.HashMap;

import org.virtue.Launcher;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.social.internal.SocialUser;
import org.virtue.utility.GameClock;

/**
 * Contains the tools necessary for managing clans.
 *
 * @author Sundays211
 * @since May 22, 2014
 */
public class ClanChannelManager {
	
	private HashMap<Long, ClanChannel> clanCache = new HashMap<Long, ClanChannel>();
	
	//private HashMap<String, Long> clanNameResolver = new HashMap<String, Long>();
	
	public ClanChannelManager () {
		System.out.println("Initialising clan channel manager.");
		Launcher.getEngine().registerLogicEvent(new Runnable() {
			@Override
			public void run() {
				synchronized (clanCache) {
					//System.out.println("Updating channels.");
					for (ClanChannel channel : clanCache.values()) {
						channel.update();//Run through any pending tick events for the channel
					}
				}
			}			
		}, GameClock.ONE_TICK, GameClock.ONE_TICK);
	}
	
	private void loadChannel (long clanHash) {
		ClanSettings settings = new ClanSettings(clanHash);
		settings.setName("Test Clan");
		clanCache.put(clanHash, new ClanChannel(settings));
	}
	
	public void joinChannel(Player player, long clanHash) {
		//System.out.println("Joining channel...");
		if (!clanCache.containsKey(clanHash)) {
			loadChannel(clanHash);
		}
		ClanChannel channel = clanCache.get(clanHash);
		if (channel == null) {
			//TODO: Find clan chat system message opcode
			player.getPacketDispatcher().dispatchMessage("Could not find a clan named [x]. Please check the name and try again.");
			return;//Clan does not exist
		}
		channel.join(new SocialUser(player), false);
	}
	
	public void joinGuestChannel (Player player, String clanName) {
		System.out.println("That clan only allows clanmates to join their clan channel.");
	}
}
