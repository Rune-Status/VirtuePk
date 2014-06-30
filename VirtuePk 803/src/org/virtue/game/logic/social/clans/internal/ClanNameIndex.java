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
package org.virtue.game.logic.social.clans.internal;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.virtue.network.io.channel.ClanIndexParser;
import org.virtue.utility.StringUtils;
import org.virtue.utility.StringUtils.FormatType;

/**
 * An index which allows the matching of clan names with clan hashes. Contains all the clans which exist on the server.
 *
 * @author Sundays211
 */
public class ClanNameIndex {
	
	private final ClanIndexParser CLAN_INDEX_PARSER = new ClanIndexParser();
	
	private Map<String, Long> clanIndex;
	
	private long lastClanIndex = 100L;
	
	private boolean indexNeedsUpdate = false;
	
	public ClanNameIndex () {
		try {
			System.out.println("Loading clan index...");
			clanIndex = Collections.synchronizedMap(CLAN_INDEX_PARSER.load());
			for (long hash : clanIndex.values()) {
				if (hash > lastClanIndex) {
					lastClanIndex = hash;
				}
			}
			lastClanIndex++;
		} catch (FileNotFoundException e) {
			System.err.println("No clan index file was found. ");
			clanIndex = Collections.synchronizedMap(new HashMap<String, Long>());
		}
	}
	
	public boolean needsUpdate () {
		return indexNeedsUpdate;
	}
	
	/**
	 * Saves the clan index to a file
	 */
	public void saveIndex () {
		CLAN_INDEX_PARSER.save(clanIndex);
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
	 * Checks whether a clan of the specified name exists
	 * @param name	The name to check
	 * @return		True if a clan exists, false otherwise
	 */
	public boolean clanExists (String name) {
		String protocol = StringUtils.format(name, FormatType.PROTOCOL);
		return clanIndex.containsKey(protocol);
	}
	
	/**
	 * Adds a clan to the index. Note that this will add the clan regardless of whether it is already in the index, so this should be checked beforehand
	 * @param name	The name of the clan
	 * @return		The unique hash code for the clan
	 */
	protected long addClan(String name) {
		String protocol = StringUtils.format(name, FormatType.PROTOCOL);
		long clanHash = lastClanIndex++;
		clanIndex.put(protocol, clanHash);
		indexNeedsUpdate = true;
		return clanHash;
	}
	
	protected void renameClan (long hash, String oldName, String newName) {
		String protocolOld = StringUtils.format(oldName, FormatType.PROTOCOL);
		String protocolNew = StringUtils.format(newName, FormatType.PROTOCOL);
		if (clanIndex.containsKey(protocolOld)) {
			clanIndex.remove(protocolOld);
		} else {
			removeAllWithHash(hash);			
		}
		clanIndex.put(protocolNew, hash);
		indexNeedsUpdate = true;
	}
	
	protected void removeAllWithHash (long hash) {
		for (Entry<String, Long> entry : clanIndex.entrySet()) {
			if (entry.getValue() == hash) {
				clanIndex.remove(entry);
			}
		}
	}
	
}
