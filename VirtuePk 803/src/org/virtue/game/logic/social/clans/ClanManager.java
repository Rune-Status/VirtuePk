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
import java.util.Map.Entry;

import org.virtue.network.io.channel.ClanIndexParser;
import org.virtue.network.io.channel.ClanSettingsParser;
import org.virtue.utility.StringUtils;
import org.virtue.utility.StringUtils.FormatType;

/**
 * The top-level object which handles everything to do with clans
 *
 * @author Sundays211
 */
public class ClanManager {
	
	private final ClanIndexParser CLAN_INDEX_PARSER = new ClanIndexParser();
	
	private final ClanSettingsParser CLAN_DATA_PARSER = new ClanSettingsParser();
	
	private Map<String, Long> clanIndex;
	
	private Map<Long, ClanSettings> clanDataCache = Collections.synchronizedMap(new HashMap<Long, ClanSettings>());
	
	private ClanChannelManager clanChatManager = new ClanChannelManager();

	private long lastClanIndex = 100L;
	
	public ClanManager () {
		loadIndex();
	}
		
	public void loadIndex () {
		try {
			clanIndex = Collections.synchronizedMap(CLAN_INDEX_PARSER.load());
		} catch (FileNotFoundException e) {
			System.err.println("No clan index file was found. ");
			clanIndex = Collections.synchronizedMap(new HashMap<String, Long>());
		}
	}
	
	public void saveIndex () {
		CLAN_INDEX_PARSER.save(clanIndex);
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
	
	/**
	 * Retrieves the clan hash for the clan with the specified name
	 * @param name	The name of the clan to find the hash of
	 * @return		The clan hash, or 0L if no hash was found
	 */
	public long resolveClan (String name) {
		String protocol = StringUtils.format(name, FormatType.PROTOCOL);
		if (!clanIndex.containsKey(protocol)) {
			return 0L;
		} else {
			return clanIndex.get(protocol);
		}
	}
	
	/**
	 * Creates a new clan and places the data into the index
	 * @param name	The desired name of the clan
	 * @return		A {@link ClanSettings} object containing the new clan data, or null if a clan already exists with the specified name
	 */
	public ClanSettings createClan (String name) {
		String protocol = StringUtils.format(name, FormatType.PROTOCOL);
		if (clanIndex.containsKey(protocol)) {
			return null;
		} else {
			long clanHash = lastClanIndex++;
			clanIndex.put(protocol, clanHash);
			ClanSettings settings = new ClanSettings(clanHash, 100, name);
			clanDataCache.put(clanHash, settings);
			saveIndex();//TODO: Do we want to re-save the index every time a new clan is created or renamed?
			return settings;
		}
	}
	
	public void renameClan (long hash, String oldName, String newName) {
		String protocolOld = StringUtils.format(oldName, FormatType.PROTOCOL);
		String protocolNew = StringUtils.format(newName, FormatType.PROTOCOL);
		if (clanIndex.containsKey(protocolOld)) {
			clanIndex.remove(protocolOld);
		} else {
			removeAllWithHash(hash);			
		}
		clanIndex.put(protocolNew, hash);
		saveIndex();
	}
	
	private String removeAllWithHash (long hash) {
		for (Entry<String, Long> entry : clanIndex.entrySet()) {
			if (entry.getValue() == hash) {
				clanIndex.remove(entry);
			}
		}
		return null;
	}
}
