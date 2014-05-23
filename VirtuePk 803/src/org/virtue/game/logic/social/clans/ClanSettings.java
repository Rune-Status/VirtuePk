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
import java.util.List;

import org.virtue.game.logic.social.clans.ccdelta.UpdateDetails;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Represents the underlying settings data for a clan
 *
 * @author Sundays211
 */
public class ClanSettings {
	
	private final long clanHash;
	
	private String clanName;
	
	private ClanRank minTalkRank = ClanRank.RECRUIT;
	
	private ClanRank minKickRank = ClanRank.RECRUIT;
	
	private ClanRank minLootShareRank = ClanRank.RECRUIT;
	
	private boolean allowNonMembers = true;
	
	private transient ClanChannel linkedChannel;
	
	private int updateNumber = 0;
	
	private List<ClanMember> members = new ArrayList<ClanMember>();
	
	
	public ClanSettings (long clanHash) {
		this.clanHash = clanHash;
	}
	
	public ClanSettings (long clanHash, int updateNumber, String clanName) {
		this.clanHash = clanHash;
		this.updateNumber = updateNumber;
		this.clanName = clanName;
	}
	
	public void deserialise (JsonObject clanData, int version) {
		allowNonMembers = clanData.get("allowNonMembers").getAsBoolean();
		minTalkRank = ClanRank.forID(clanData.get("minTalkRank").getAsByte());
		if (minTalkRank == null) {
			minTalkRank = ClanRank.RECRUIT;
		}
		minKickRank = ClanRank.forID(clanData.get("minKickRank").getAsByte());
		if (minKickRank == null) {
			minKickRank = ClanRank.RECRUIT;
		}
		minLootShareRank = ClanRank.forID(clanData.get("minLootShareRank").getAsByte());
		if (minLootShareRank == null) {
			minLootShareRank = ClanRank.RECRUIT;
		}
		JsonArray membersData = clanData.get("members").getAsJsonArray();	
		for (JsonElement member : membersData) {
			JsonObject memberData = member.getAsJsonObject();
			String protocolName = memberData.get("username").getAsString();
			ClanRank rank = ClanRank.forID(memberData.get("rank").getAsByte());
			if (rank == null) {
				rank = ClanRank.RECRUIT;
			}
			members.add(new ClanMember(protocolName, rank));
		}
	}
	
	public JsonObject serialise () {
		JsonObject clanData = new JsonObject();
		clanData.addProperty("clanHash", clanHash);
		clanData.addProperty("name", clanName);
		clanData.addProperty("updateNum", updateNumber);
		
		//Primitive permissions
		clanData.addProperty("allowNonMembers", allowNonMembers);
		clanData.addProperty("minTalkRank", minTalkRank.getID());
		clanData.addProperty("minKickRank", minKickRank.getID());
		clanData.addProperty("minLootShareRank", minLootShareRank.getID());
		
		//Clan member data
		JsonArray memberData = new JsonArray();
		for (ClanMember member : members) {
			JsonObject data = new JsonObject();
			data.addProperty("username", member.getProtocolName());
			data.addProperty("rank", member.getRank().getID());
			memberData.add(data);
		}
		clanData.add("members", memberData);
		
		//Clan ban data
		
		return clanData;
	}
	
	public void linkChannel (ClanChannel channel) {
		this.linkedChannel = channel;
	}	
	
	public void setName (String name) {
		this.clanName = name;
		updateChannelDetails();
	}
	
	public void setAllowNonMembers (boolean allowNonMembers) {
		this.allowNonMembers = allowNonMembers;
	}
	
	public void setMinTalkRank (ClanRank minTalkRank) {
		this.minTalkRank = minTalkRank;
		updateChannelDetails();
	}
	
	public void setMinKickRank (ClanRank minKickRank) {
		this.minKickRank = minKickRank;
		updateChannelDetails();
	}
	
	private void updateChannelDetails () {
		if (linkedChannel != null) {
			linkedChannel.queueUpdate(new UpdateDetails(clanName, minKickRank, minKickRank));
		}
	}
	
	/**
	 * Returns the unique hash code for the clan
	 * @return	The clan hash
	 */
	public long getClanHash () {
		return clanHash;
	}
	
	/**
	 * Returns the name of the clan
	 * @return	The clan name
	 */
	public String getClanName () {
		return clanName;
	}
	
	/**
	 * Returns the minimum rank needed to talk in the channel
	 * @return	The minimum talk rank
	 */
	public ClanRank getMinTalk () {
		return minTalkRank;
	}
	
	/**
	 * Returns the minimum rank needed to kick guests from the channel
	 * @return	The minimum kick rank
	 */
	public ClanRank getMinKick () {
		return minKickRank;
	}
	
	public boolean allowNonMembers () {
		return allowNonMembers;
	}
}
