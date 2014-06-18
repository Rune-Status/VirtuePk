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
import org.virtue.game.logic.social.SocialUserAPI;
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
	
	public void registerPlayer (SocialUserAPI user) {
		if (user.getMyClanHash() != 0L) {
			ClanSettings clanData = getClanData(user.getMyClanHash());
			if (clanData != null) {
				clanData.registerOnlineMember(user);
			}
		}
	}
	
	public void deregisterPlayer (SocialUserAPI user) {
		if (user.getMyClanHash() != 0L) {
			ClanSettings clanData = getClanData(user.getMyClanHash());
			if (clanData != null) {
				clanData.deregisterOnlineMember(user);
			}
		}
	}
	
	/**
	 * Runs the update tasks for a clan, such as dispatching any delta updates to the players in the clan
	 */
	protected void runUpdateTasks () {
		for (ClanSettings clanData : clanDataCache.values()) {
			clanData.dispatchUpdates();
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
	
	public ClanChannelAPI getChannelManager () {
		return clanChatManager;
	}
	
	public ClanNameIndex getClanIndex () {
		return clanIndex;
	}
	
	/**
	 * Recruits a player into the recruiter's clan
	 * @param recruiter		The user performing the invite
	 * @param joiner		The user who is joining the clan
	 * @return				True if the user successfully joined the clan, false otherwise
	 */
	public boolean joinClan (SocialUserAPI recruiter, SocialUserAPI joiner) {
		if (joiner.getMyClanHash() != 0L) {
			return false;//Already in clan
		}
		long clanHash = recruiter.getMyClanHash();
		ClanSettings clan = getClanData(clanHash);
		if (clan == null) {
			return false;
		}
		if (clan.isBanned(joiner.getProtocolName())) {
			return false;//TODO: Somehow send a message to the recruiter that the joiner is banned
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
	 * @param owner The player who will become the clan owner
	 * @param founders	The players who will be initially recruited into the clan
	 * @return		A {@link ClanSettings} object containing the new clan data, or null if a clan already exists with the specified name
	 */
	public ClanSettings createClan (String name, SocialUserAPI owner, SocialUserAPI... founders) {
		if (clanIndex.clanExists(name)) {
			return null;
		} else {
			long clanHash = clanIndex.addClan(name);
			ClanSettings settings = new ClanSettings(clanHash, 100, name);
			clanDataCache.put(clanHash, settings);
			owner.setMyClanHash(clanHash);
			settings.addMember(owner);
			settings.registerOnlineMember(owner);
			clanChatManager.joinMyChannel(owner);
			for (SocialUserAPI founder : founders) {
				founder.setMyClanHash(clanHash);
				settings.addMember(founder);
				settings.registerOnlineMember(founder);
				clanChatManager.joinMyChannel(founder);
			}
			
			//saveClanData(settings);
			return settings;
		}
	}
	
	/**
	 * Sets the rank of the specified player in the specified clan.
	 * Note that this method does allow server administrators (who would normally hold a rank of JMOD) to change the ranks of people in clans they don't belong to themselves.
	 * @param clanHash		The unique hash for the clan with the rank being edited
	 * @param player		The player performing the edit
	 * @param protocolName	The protocol displayname of the clan member who's rank is being changed
	 * @param rank			The rank to change to
	 * @return				True if the rank was changed successfully, false if there was an error.
	 */
	public boolean setRank (long clanHash, SocialUserAPI player, String protocolName, ClanRank rank) {
		ClanSettings clan = getClanData(clanHash);
		if (clan == null) {
			//player.getPacketDispatcher().dispatchMessage("You need to be in a clan to do that.", MessageOpcode.CLAN_SYSTEM);
			return false;
		}
		ClanRank playerRank = clan.getRank(player.getProtocolName());
		if (!clan.inClan(protocolName)) {
			//player.getPacketDispatcher().dispatchMessage("The specified player is not in your clan.", MessageOpcode.CLAN_SYSTEM);
			return false;
		}
		if (rank == null || rank.getID() > ClanRank.DEPUTY_OWNER.getID() || rank.getID() < ClanRank.RECRUIT.getID()) {
			//player.getPacketDispatcher().dispatchMessage("You have specified an invalid rank. Only ranks between deputy owner and recruit (inclusive) are valid.", MessageOpcode.CLAN_SYSTEM);
			return false;
		}
		ClanMember owner = clan.getOwner();
		if (owner.getProtocolName().equalsIgnoreCase(protocolName)) {
			if (playerRank == null || (!playerRank.equals(ClanRank.JMOD) && !playerRank.equals(ClanRank.OWNER))) {
				//player.getPacketDispatcher().dispatchMessage("You do not have the appropriate permissions to change the channel owner.", MessageOpcode.CLAN_SYSTEM);
				return false;
			}
			if (rank.equals(ClanRank.DEPUTY_OWNER)) {
				//player.getPacketDispatcher().dispatchMessage("You must set the owner's rank lower than deputy owner in order to change the clan owner.", MessageOpcode.CLAN_SYSTEM);
				return false;
			}
			clan.setOwnerRank(rank);
		} else {
			if (!playerRank.isAdmin() || playerRank.getID() <= clan.getRank(protocolName).getID()) {
				//player.getPacketDispatcher().dispatchMessage("You do not have the appropriate permissions to change this player's rank.", MessageOpcode.CLAN_SYSTEM);
				return false;
			}
			clan.setRank(protocolName, rank);
		}
		return true;
	}
	
	public boolean leaveMyClan (SocialUserAPI player) {
		long clanHash = player.getMyClanHash();
		if (clanHash == 0L) {
			clanChatManager.leaveChannel(player, false, false);
			return true;//Player is not in a clan
		}
		ClanSettings clan = getClanData(clanHash);
		if (clan == null) {
			player.setMyClanHash(0L);
			clanChatManager.leaveChannel(player, false, false);
			return true;//Clan does not exist anyway. Set clan hash to zero
		}
		if (!clan.inClan(player.getProtocolName())) {
			player.setMyClanHash(0L);
			clanChatManager.leaveChannel(player, false, false);
			return true;//Player isn't in the clan. Set clan hash to zero
		}
		//TODO: Verify that the player is not the clan owner. If they are, more checks will need to take place.
		clanChatManager.leaveChannel(player, false, false);
		clan.removeMember(player.getProtocolName());
		player.setMyClanHash(0L);
		return true;
	}
	
	public boolean kickClanMember (long clanHash, SocialUserAPI player, String protocolName) {
		ClanSettings clan = getClanData(clanHash);
		if (clan == null) {	
			System.out.println("Could not kick clan member - clan does not exist.");
			return false;
		}
		ClanRank playerRank = clan.getRank(player.getProtocolName());
		if (!clan.inClan(protocolName)) {
			System.out.println("Could not kick clan member - member is not in clan (name="+protocolName+").");
			return false;//Player not in clan
		}
		if (!playerRank.isAdmin() || playerRank.getID() <= clan.getRank(protocolName).getID()) {	
			System.out.println("Could not kick clan member - insufficient permissions.");		
			return false;//Lack appropriate permissions to kick player from clan
		}
		clan.removeMember(protocolName);
		return true;
	}
}
