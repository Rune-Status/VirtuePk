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

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.virtue.cache.def.VarBitType;
import org.virtue.game.logic.social.SocialUserAPI;
import org.virtue.game.logic.social.clans.ClanBan;
import org.virtue.game.logic.social.clans.ClanRank;
import org.virtue.game.logic.social.clans.ccdelta.UpdateDetails;
import org.virtue.game.logic.social.clans.csdelta.AddMember;
import org.virtue.game.logic.social.clans.csdelta.ClanSettingsDelta;
import org.virtue.game.logic.social.clans.csdelta.DeleteMember;
import org.virtue.game.logic.social.clans.csdelta.SetClanSettingsVar;
import org.virtue.game.logic.social.clans.csdelta.SetClanSettingsVarBit;
import org.virtue.game.logic.social.clans.csdelta.UpdateRank;
import org.virtue.game.logic.social.messages.ClanSettingsDeltaPacket;
import org.virtue.game.logic.social.messages.ClanSettingsPacket;
import org.virtue.game.logic.vars.VarBitOverflowException;
import org.virtue.game.logic.vars.VarDomainType;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

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
	
	private int ownerSlot = -1;
	
	private int replacementOwnerSlot = -1;
	
	private final List<ClanMember> members = Collections.synchronizedList(new ArrayList<ClanMember>());
	
	private final List<ClanBan> bans = Collections.synchronizedList(new ArrayList<ClanBan>());
	
	private final List<SocialUserAPI> onlineMembers = Collections.synchronizedList(new ArrayList<SocialUserAPI>());
	
	private final Queue<SocialUserAPI> initQueue = new LinkedList<SocialUserAPI>();
	
	private final Queue<ClanSettingsDelta> updateQueue = new LinkedList<ClanSettingsDelta>();
	
	private final Map<Integer, Object> varClanSettings = Collections.synchronizedMap(new HashMap<Integer, Object>());
	
	private final EnumMap<ClanRank, EnumSet<ClanPermission>> permissions = new EnumMap<ClanRank, EnumSet<ClanPermission>>(ClanRank.class);
	
	private boolean needsSave;
	
	
	public ClanSettings (long clanHash) {
		this.clanHash = clanHash;
	}
	
	public ClanSettings (long clanHash, int updateNumber, String clanName) {
		this.clanHash = clanHash;
		this.updateNumber = updateNumber;
		this.clanName = clanName;
	}
	
	/**
	 * Queues an update to the clan settings which will be sent on the next tick
	 * @param node	The update node to queue
	 */
	protected void queueUpdate (ClanSettingsDelta node) {
		synchronized (updateQueue) {
			updateQueue.offer(node);
		}
		needsSave = true;
	}
	
	public boolean needsSave () {
		return needsSave;
	}
	
	public void onSaved () {
		needsSave = false;
	}
	
	/**
	 * Sends the clan settings delta updates to every clan member who is currently logged in
	 */
	protected void dispatchUpdates () {
		if (updateQueue.isEmpty()) {
			sendInitPackets();
			return;
		}
		ClanSettingsDelta[] deltaNodes;
		int thisUpdate = updateNumber;
		synchronized (updateQueue) {
			deltaNodes = new ClanSettingsDelta[updateQueue.size()];
			updateQueue.toArray(deltaNodes);
			updateQueue.clear();
			updateNumber++;
		}
		ClanSettingsDeltaPacket memberPacket = new ClanSettingsDeltaPacket(false, thisUpdate, deltaNodes);
		synchronized (onlineMembers) {
			for (SocialUserAPI u : onlineMembers) {
				u.sendClanSettingsDelta(memberPacket);
			}
		}
		sendInitPackets();
	}
	
	/**
	 * Sends any queued initialisation packets
	 */
	private void sendInitPackets () {
		if (initQueue.isEmpty()) {
			return;
		}
		ClanSettingsPacket.Member[] entries;
		String[] banEntries;
		ClanSettingsPacket.VarClanSetting[] varSettingsEntries;
		synchronized (onlineMembers) {			
			for (SocialUserAPI u : initQueue) {
				onlineMembers.add(u);
			}
		}
		synchronized (members) {
			entries = new ClanSettingsPacket.Member[members.size()];
			for (int i=0;i<members.size();i++) {
				ClanMember u = members.get(i);
				entries[i] = new ClanSettingsPacket.Member(u.getDisplayName(), u.getRank(), 0, u.getJoinDay());
			}
		}
		synchronized (bans) {
			banEntries = new String[bans.size()];
			for (int i=0;i<bans.size();i++) {
				banEntries[i] = bans.get(i).getDisplayName();
			}
		}
		synchronized (varClanSettings) {
			varSettingsEntries = new ClanSettingsPacket.VarClanSetting[varClanSettings.size()];
			int i=0;
			for (Map.Entry<Integer, Object> setting : varClanSettings.entrySet()) {
				varSettingsEntries[i++] = new ClanSettingsPacket.VarClanSetting(setting.getKey(), setting.getValue());
			}
		}
		ClanSettingsPacket packet = new ClanSettingsPacket(false, clanName, entries, banEntries,
					updateNumber, allowNonMembers, minTalkRank, minKickRank, varSettingsEntries);
		SocialUserAPI user;
		while ((user = initQueue.poll()) != null) {
			user.sendClanSettingsFull(packet);
		}
	}
	
	public void registerOnlineMember (SocialUserAPI user) {
		synchronized(initQueue) {
			if (!initQueue.contains(user)) {
				initQueue.offer(user);
			}
		}
	}
	
	public void deregisterOnlineMember (SocialUserAPI user) {
		synchronized(initQueue) {
			if (initQueue.contains(user)) {
				initQueue.remove(user);
			} else if (onlineMembers.contains(user)) {
				onlineMembers.remove(user);
			}
		}
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
		members.clear();		
		for (JsonElement memberElement : membersData) {
			JsonObject memberData = memberElement.getAsJsonObject();
			String protocolName = memberData.get("username").getAsString();
			ClanRank rank = ClanRank.forID(memberData.get("rank").getAsByte());
			if (rank == null) {
				rank = ClanRank.RECRUIT;
			}
			int varClanMember = memberData.get("varClanMember") == null ? 0 : memberData.get("varClanMember").getAsInt();
			int joinDay = memberData.get("joinDay") == null ? 0 : memberData.get("joinDay").getAsInt();
			members.add(new ClanMember(protocolName, rank, varClanMember, joinDay));
		}
		findClanOwner();//Finds the owner of the clan
		JsonArray banData = clanData.get("bans").getAsJsonArray();
		bans.clear();
		for (JsonElement ban : banData) {
			bans.add(new ClanBan(ban.getAsString()));
		}
		JsonArray clanSettingsNode = clanData.get("varClanSettings") == null ? null : clanData.get("varClanSettings").getAsJsonArray();
		if (clanSettingsNode != null) {
			varClanSettings.clear();
			for (JsonElement varSettingsElement : clanSettingsNode) {
				JsonObject settingsData = varSettingsElement.getAsJsonObject();
				int key = settingsData.get("key").getAsInt();
				char type = settingsData.get("type").getAsCharacter();
				Object value;
				switch (type) {
					case 's':
						value = settingsData.get("value").getAsString();
						break;
					case 'i':
						value = settingsData.get("value").getAsInt();
						break;
					case 'l':
						value = settingsData.get("value").getAsLong();
					default:
						throw new RuntimeException("Invalid varClanSettings type for key="+key+", type="+type);
				}
				varClanSettings.put(key, value);
			}
			loadPermissions();
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
			data.addProperty("varClanMember", member.getVarClanMember());
			data.addProperty("joinDay", member.getJoinDay());
			memberData.add(data);
		}
		clanData.add("members", memberData);
		
		//Clan ban data
		JsonArray banData = new JsonArray();
		for (ClanBan ban : bans) {
			banData.add(new JsonPrimitive(ban.getProtocolName()));
		}
		clanData.add("bans", banData);
		
		//VarClanSettings
		JsonArray settingsData = new JsonArray();
		for (Map.Entry<Integer, Object> varSetting : varClanSettings.entrySet()) {
			JsonObject data = new JsonObject();
			data.addProperty("key", varSetting.getKey());
			Object value = varSetting.getValue();
			if (value instanceof String) {
				data.addProperty("type", 's');
				data.addProperty("value", (String) value);
			} else if (value instanceof Long) {
				data.addProperty("type", 'l');
				data.addProperty("value", (Long) value);
			} else if (value instanceof Integer) {
				data.addProperty("type", 'i');
				data.addProperty("value", (Integer) value);
			} else {
				throw new RuntimeException("Invalid varClanSettings type for key "+varSetting.getKey());
			}
			settingsData.add(data);
		}
		clanData.add("varClanSettings", settingsData);
		return clanData;
	}
	
	private void loadPermissions () {
		for (ClanRank rank : ClanRank.values()) {
			if (rank.equals(ClanRank.JMOD) || rank.equals(ClanRank.OWNER) || rank.equals(ClanRank.GUEST)) {
				continue;
			}
			if (permissions.get(rank) == null) {
				permissions.put(rank, EnumSet.noneOf(ClanPermission.class));
			} else {
				permissions.get(rank).clear();
			}
			for (ClanPermission permission : ClanPermission.values()) {		
				VarBitType varBit = permission.getVarBit(rank);
				if (varBit != null) {
					if (getClanSettingsVarBit(varBit) == 1) {
						permissions.get(rank).add(permission);
					}
				}
			}
		}
	}
	
	private void findClanOwner () {
		ownerSlot = -1;
		replacementOwnerSlot = -1;
		int highestRankSlot = 0;
		synchronized (members) {
			if (members.isEmpty()) {
				return;
			}
			ClanRank highestRank = members.get(0).getRank();
			for (int index = 1;index<members.size();index++) {
				ClanRank rank = members.get(index).getRank();
				if (rank.getID() > highestRank.getID()) {
					if (highestRank.equals(ClanRank.DEPUTY_OWNER)) {
						replacementOwnerSlot = highestRankSlot;
					}
					highestRankSlot = index;
					highestRank = rank;
				} else if (rank.equals(ClanRank.DEPUTY_OWNER) && replacementOwnerSlot == -1) {
					replacementOwnerSlot = index;
				}
				
			}
			ownerSlot = highestRankSlot;
			if (highestRankSlot != -1) {
				members.get(ownerSlot).setRank(ClanRank.OWNER);
			}		
		}
	}
	
	protected void linkChannel (ClanChannel channel) {
		this.linkedChannel = channel;
	}	
	
	protected void setName (String name) {
		this.clanName = name;
		updateChannelDetails();
	}
	
	protected void setAllowNonMembers (boolean allowNonMembers) {
		this.allowNonMembers = allowNonMembers;
	}
	
	protected void setMinTalkRank (ClanRank minTalkRank) {
		this.minTalkRank = minTalkRank;
		updateChannelDetails();
	}
	
	protected void setMinKickRank (ClanRank minKickRank) {
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
	
	/**
	 * Returns whether guests are allowed to join the clan channel associated with this clan
	 * @return	True if guests are allowed to join, false otherwise
	 */
	public boolean allowNonMembers () {
		return allowNonMembers;
	}
	
	/**
	 * Returns whether the player of the specified name is a part of the clan.
	 * @param protocolName	The protocol name of the player to check
	 * @return				True if the player is a member of the clan, false otherwise
	 */
	public boolean inClan (String protocolName) {
		ClanMember member = getMember(protocolName);
		return member != null;
	}
	
	/**
	 * Returns the rank of a player in the clan
	 * @param protocolName	The protocol username of the player to check
	 * @return				The rank of the player
	 */
	public ClanRank getRank (String protocolName) {
		ClanMember member = getMember(protocolName);
		if (member != null) {
			return member.getRank();
		} else {
			return ClanRank.GUEST;
		}
	}
	
	public boolean isBanned (String protocolName) {
		if (getBan(protocolName) == null) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the clan member object for a specified player. 
	 * @param protocolName	The protocol name of the player to search for
	 * @return				The {@link ClanMember} object for the player, or null if the player is not in the clan.
	 */
	private ClanMember getMember (String protocolName) {
		//TODO: Find a more efficient way of doing this
		for (ClanMember member : members) {
			if (member.getProtocolName().equalsIgnoreCase(protocolName)) {
				return member;
			}
		}
		return null;
	}
	
	private ClanBan getBan (String protocolName) {
		for (ClanBan ban : bans) {
			if (ban.getProtocolName().equalsIgnoreCase(protocolName)) {
				return ban;
			}
		}
		return null;
	}
	
	public ClanMember getOwner () {
		if (ownerSlot == -1) {
			return null;
		}
		synchronized (members) {
			return members.get(ownerSlot);
		}
	}
	
	public void sendPermissionInfo (SocialUserAPI user, ClanRank group) {
		EnumSet<ClanPermission> groupPermissions = permissions.get(group);
		if (groupPermissions == null) {
			loadPermissions();
		} 
		if (groupPermissions != null) {
			//System.out.println("Permissions for group "+group+": "+groupPermissions);
			user.sendPermissionGroup(group, groupPermissions);
		} else {
			System.err.println("Permissions not found for group "+group);
		}
	}
	
	public void sendMemberInfo (SocialUserAPI user, int index) {
		user.sendClanMemberInfo(members.get(index));
	}
	
	/**
	 * Adds the provided player to the clan. 
	 * Note that setting the player's clan within the player data and sending the clan channel must be handled separately
	 * @param player The player to add to the clan
	 */
	 protected void addMember (SocialUserAPI player) {
		if (inClan(player.getProtocolName())) {
			return;
		}
		ClanMember newMember = new ClanMember(player.getProtocolName());
		synchronized (members) {
			members.add(newMember);
			findClanOwner();
		}
		queueUpdate(new AddMember(newMember.getDisplayName()));
		if (linkedChannel != null) {
			linkedChannel.updateUser(player.getProtocolName());
		}
	}
	
	/**
	 * Removes the member with the specified protocol name from the clan
	 * @param protocolName	The protocol username of the player to remove
	 * @throws NullPointerException	if the player is not in the clan.
	 */
	protected void removeMember (String protocolName) throws NullPointerException {
		ClanMember member = getMember(protocolName);
		if (member == null) {
			throw new NullPointerException(protocolName+" is not in "+clanName);
		}
		synchronized (members) {
			int index = members.indexOf(member);
			members.remove(member);
			findClanOwner();
			queueUpdate(new DeleteMember(index));
		}
		if (linkedChannel != null) {
			linkedChannel.updateUser(member.getProtocolName());
		}		
	}
	
	/**
	 * Sets the rank for the specified player in the clan. Note that permission checks must be done externally
	 * @param protocolName	The protocol username of the player to set the rank of
	 * @param rank			The desired rank
	 * @throws NullPointerException	if the player is not in the clan.
	 */
	protected void setRank (String protocolName, ClanRank rank) throws NullPointerException {
		ClanMember member = getMember(protocolName);
		if (member == null) {
			throw new NullPointerException(protocolName+" is not in "+clanName);
		}
		setRank(member, rank);
	}
	
	private void setRank (ClanMember member, ClanRank rank) {
		member.setRank(rank);
		synchronized (updateQueue) {
			int index = members.indexOf(member);
			queueUpdate(new UpdateRank(index, rank));
		}
		if (linkedChannel != null) {
			linkedChannel.updateUser(member.getProtocolName());
		}
		findClanOwner();
	}
	
	protected void setOwnerRank (ClanRank rank) throws NullPointerException {
		ClanMember member = getOwner();
		setRank(member, rank);
		ClanMember newOwner;
		synchronized (members) {
			newOwner = getOwner();
			synchronized (updateQueue) {
				queueUpdate(new UpdateRank(ownerSlot, rank));
			}
		}
		if (linkedChannel != null && newOwner != null) {
			linkedChannel.updateUser(newOwner.getProtocolName());
		}
		
	}
	
	protected boolean setClanSettingsVarBit (VarBitType varBit, int value) throws VarBitOverflowException {
		if (!varBit.getDomainType().equals(VarDomainType.CLAN_SETTINGS)) {
			return false;
		}
		return setClanSettingsVarBit(varBit.getBaseVarKey(), value, varBit.getStartBit(), varBit.getEndBit());
	}
	
	protected boolean setClanSettingsVarBit (int key, int value, int start, int end) throws VarBitOverflowException {
		int maxValue = (1 << (end - start + 1))-1;
		if (value > maxValue || value < 0) {
			throw new VarBitOverflowException("VarClanSettings "+key+"("+start+"-"+end+") value "+value+" is outside the allowed range of 0-"+maxValue);
		}
		int i_21_ = (1 << start) - 1;
		int i_22_ = 31 == end ? -1 : (1 << 1 + end) - 1;
		int mask = i_22_ ^ i_21_;
		value <<= start;
		value &= mask;
		synchronized (varClanSettings) {
			Object prevValue = varClanSettings.get(key);		
			if (prevValue == null) {
				varClanSettings.put(key, value);
			} else if (prevValue instanceof Integer) {
				int prevValueInt = (int) prevValue;
				if ((prevValueInt & mask) == value) {
					return true;//No updated needed as value hasn't changed
				}
				varClanSettings.remove(key);
				prevValueInt &= (mask ^ 0xffffffff);
				prevValueInt |= value;
				varClanSettings.put(key, prevValueInt);
			} else {
				return false;
			}
		}
		synchronized (updateQueue) {
			queueUpdate(new SetClanSettingsVarBit(key, value, start, end));
		}
		return true;
	}
	
	protected void setClanSettingsVar (int key, Object value) {
		SetClanSettingsVar deltaNode = new SetClanSettingsVar(key, value);
		if (deltaNode.getTypeID() == -1) {
			throw new RuntimeException("Invalid value type");
		}
		synchronized (varClanSettings) {
			if (varClanSettings.containsKey(key)) {
				Object oldValue = varClanSettings.get(key);
				if (oldValue.equals(value)) {
					return;
				}
				varClanSettings.remove(key);
			}
			varClanSettings.put(key, value);
		}
		synchronized (updateQueue) {
			queueUpdate(deltaNode);
		}
	}
	
	public String getClanSettingsVarString (int key) {
		return (String) varClanSettings.get(key);
	}
	
	public int getClanSettingsVarInt (int key) {
		if (!varClanSettings.containsKey(key)) {
			return 0;
		}
		return (int) varClanSettings.get(key);
	}
	
	public int getClanSettingsVarBit (VarBitType varBit) {
		if (!varBit.getDomainType().equals(VarDomainType.CLAN_SETTINGS)) {
			return -1;
		}
		return getClanSettingsVarBit(varBit.getBaseVarKey(), varBit.getStartBit(), varBit.getEndBit());
	}
	
	public int getClanSettingsVarBit (int key, int start, int end) {
		int mask = (start == 31) ? -1 : (1 << 1 + end) - 1;
		return (getClanSettingsVarInt(key) & mask) >>> start;
	}
	
	protected boolean addPermission (ClanRank group, ClanPermission permission) {
		if (!permissions.containsKey(group)) {
			return false;
		}
		if (permissions.get(group).contains(permission)) {
			return false;
		} else {
			VarBitType varBit = permission.getVarBit(group);
			if (varBit == null) {
				return false;
			}
			try {
				setClanSettingsVarBit(varBit, 1);
			} catch (VarBitOverflowException e) {
				e.printStackTrace();
				return false;
			}
			permissions.get(group).add(permission);
			return true;
		}
	}
	
	protected boolean removePermission (ClanRank group, ClanPermission permission) {
		if (!permissions.containsKey(group)) {
			return false;
		}
		if (!permissions.get(group).contains(permission)) {
			return false;
		} else {
			VarBitType varBit = permission.getVarBit(group);
			if (varBit == null) {
				return false;
			}
			try {
				setClanSettingsVarBit(varBit, 0);
			} catch (VarBitOverflowException e) {
				e.printStackTrace();
				return false;
			}
			permissions.get(group).remove(permission);
			return true;
		}
	}
	
	@Override
	public boolean equals (Object anObject) {
		if (this == anObject) {
            return true;
        }
		if (anObject instanceof ClanSettings) {
			ClanSettings anotherClan = (ClanSettings) anObject;
			return anotherClan.clanHash == this.clanHash;
		}
		return false;
		
	}

	@Override
	public int hashCode() {
		return (int) clanHash;
	}
}
