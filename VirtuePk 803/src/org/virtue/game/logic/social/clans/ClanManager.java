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

import org.virtue.Launcher;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.social.SocialUser;
import org.virtue.game.logic.social.internal.InternalSocialUser;
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
	
	private final InternalClanChannelManager clanChatManager;

	
	public ClanManager () {
		clanChatManager = new InternalClanChannelManager(this);
		clanIndex = new ClanNameIndex();
		Launcher.getEngine().getLogicProcessor().registerEvent(new ClanUpdateEvent(this));
	}
	
	/**
	 * Runs the update tasks for a clan, such as dispatching any delta updates to the players in the clan
	 */
	protected void runUpdateTasks () {
		for (ClanSettings clanData : clanDataCache.values()) {
			if (clanData.needsUpdate()) {
				clanData.dispatchUpdates();
			}
		}
	}
	
	/**
	 * Runs tasks related to saving clan data. This should be run less frequently than runUpdateTasks, but should be run fairly regularly to avoid clan data being lost due to a server crash.
	 */
	protected void runSaveTasks () {
		if (clanIndex.needsUpdate()) {
			clanIndex.saveIndex();
		}
		//Sends clan channel updates to the players
		clanChatManager.runUpdateTasks();
		for (ClanSettings clanData : clanDataCache.values()) {
			if (clanData.needsSave()) {
				saveClanData(clanData);
				clanData.onSaved();
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
		//TODO: Make this "protected" once testing is completed
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
	
	public boolean joinClan (InternalSocialUser recruiter, InternalSocialUser joiner) {
		if (joiner.getMyClanHash() != 0L) {
			return false;//Already in clan
		}
		long clanHash = recruiter.getMyClanHash();
		ClanSettings clan = getClanData (clanHash);
		if (clan == null) {
			return false;
		}
		//TODO: Make sure the recruiter is allowed to recruit and the joiner isn't banned from the channel
		clan.addMember(joiner);
		joiner.setMyClanHash(clanHash);
		if (joiner.getGuestClanHash() == clanHash) {
			clanChatManager.leaveChannel(joiner, true, false);
		}
		clanChatManager.joinMyChannel(joiner);
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
			InternalSocialUser ownerObject = new InternalSocialUser(owner);
			settings.addMember(new InternalSocialUser(owner));
			settings.setRank(ownerObject.getProtocolName(), ClanRank.OWNER);
			for (Player founder : founders) {
				settings.addMember(new InternalSocialUser(founder));
			}
			saveClanData(settings);
			return settings;
		}
	}
}
