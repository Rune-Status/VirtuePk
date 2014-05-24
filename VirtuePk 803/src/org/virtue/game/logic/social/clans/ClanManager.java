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

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.social.internal.SocialUser;
import org.virtue.network.io.channel.ClanSettingsParser;

/**
 * The top-level object which handles everything to do with clans
 *
 * @author Sundays211
 */
public class ClanManager {
	
	
	private final ClanSettingsParser CLAN_DATA_PARSER = new ClanSettingsParser();
	
	private final ClanNameIndex clanIndex;
	
	private final Map<Long, ClanSettings> clanDataCache = Collections.synchronizedMap(new HashMap<Long, ClanSettings>());
	
	private final ClanChannelManager clanChatManager;

	
	public ClanManager () {
		clanChatManager = new ClanChannelManager(this);
		clanIndex = new ClanNameIndex();
	}
	
	public void autoSaveClanData () {
		if (clanIndex.needsUpdate()) {
			clanIndex.saveIndex();
		}
		for (ClanSettings clanData : clanDataCache.values()) {
			if (clanData.needsUpdate()) {
				saveClanData(clanData);
			}
		}
	}
	
	/**
	 * Saves the data for the specified clan to a file
	 * @param clanData	The clan data to save
	 */
	public void saveClanData(ClanSettings clanData) {
		CLAN_DATA_PARSER.save(clanData.getClanHash(), clanData);
	}
	
	/**
	 * Retrieves the settings for a clan
	 * @param clanHash	The hash of the clan of which to retrieve data
	 * @return	A {@link ClanSettings} object containing the clan data, or null if the data was not found
	 */
	public ClanSettings getClanData (long clanHash) {
		ClanSettings settings = clanDataCache.get(clanHash);
		if (settings != null) {
			return settings;
		}
		try {
			settings = CLAN_DATA_PARSER.load(clanHash);
		} catch (FileNotFoundException e) {
			return null;
		}
		if (settings != null) {
			clanDataCache.put(clanHash, settings);
		}
		return settings;
	}
	
	public ClanChannelManager getChannelManager () {
		return clanChatManager;
	}
	
	public ClanNameIndex getClanIndex () {
		return clanIndex;
	}
	
	public boolean joinClan (Player recruiter, Player joiner) {
		long clanHash = recruiter.getChatManager().getMyClanHash();
		ClanSettings clan = getClanData (clanHash);
		if (clan == null) {
			return false;
		}
		//TODO: Make sure the recruiter is allowed to recruit and the joiner isn't banned from the channel
		clan.addMember(new SocialUser(joiner));
		joiner.getChatManager().setMyClanHash(clanHash);
		if (joiner.getChatManager().getGuestClanHash() == clanHash) {
			clanChatManager.leaveChannel(joiner, true);
		}
		clanChatManager.joinChannel(joiner, clanHash);
		return true;
	}
	
	/**
	 * Creates a new clan and places the data into the index
	 * @param name	The desired name of the clan
	 * @return		A {@link ClanSettings} object containing the new clan data, or null if a clan already exists with the specified name
	 */
	public ClanSettings createClan (String name, Player owner, Player... founders) {
		if (clanIndex.clanExists(name)) {
			return null;
		} else {
			long clanHash = clanIndex.addClan(name);
			ClanSettings settings = new ClanSettings(clanHash, 100, name);
			clanDataCache.put(clanHash, settings);
			SocialUser ownerObject = new SocialUser(owner);
			settings.addMember(new SocialUser(owner));
			settings.setRank(ownerObject.getProtocolName(), ClanRank.OWNER);
			for (Player founder : founders) {
				settings.addMember(new SocialUser(founder));
			}
			saveClanData(settings);
			return settings;
		}
	}
}
