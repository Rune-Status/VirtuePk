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

import org.virtue.Launcher;
import org.virtue.game.logic.vars.VarBitOverflowException;
import org.virtue.utility.StringUtils;
import org.virtue.utility.StringUtils.FormatType;

/**
 * Represents the data for a member of a clan
 *
 * @author Sundays211
 */
public class ClanMember {
	
	public static enum VarClanMember {
		JOB(0, 9),
		CITADEL_BANNED(11),
		KEEP_BANNED(12),
		ISLAND_BANNED(13),
		PROBATION(14, 15);
		
		VarClanMember(int bit) {
			this(bit, bit);
		}
		
		private final int startBit;
		private final int endBit;
		private final int maxValue;
		
		VarClanMember(int startBit, int endBit) {
			this.startBit = startBit;
			this.endBit = endBit;
			this.maxValue = (1 << Math.abs(startBit-endBit)+1) - 1;
		}
		
		public int getStartBit () {
			return startBit;
		}
		
		public int getEndBit () {
			return endBit;
		}
		
		public int getMaxValue () {
			return maxValue;
		}
	}
	
	//0, 1, 2, 4, 8, f
	public static final int FLAG_CITADEL_BANNED = 0x2;
	public static final int FLAG_KEEP_BANNED = 0x2;
	public static final int FLAG_ISLAND_BANNED = 0x2;
	public static final int FLAG_PROBATION_START = 0x4000;
	public static final int FLAG_PROBATION_END = 0x8000;
	
	private final String protocolName;
	
	private String displayName;
	
	private ClanRank rank;
	
	private int varClanMember;
	
	private final int joinDay;
	
	//private boolean bannedFromCitadel = true;
	
	public ClanMember (String protocolName) {
		this(protocolName, ClanRank.RECRUIT, 0, Launcher.getDay());
	}
	
	public ClanMember (String protocolName, ClanRank rank, int varClanMember, int joinDay) {
		this.protocolName = protocolName;
		this.displayName = StringUtils.format(protocolName, FormatType.DISPLAY);
		this.rank = rank;
		this.varClanMember = varClanMember;
		this.joinDay = joinDay;
	}
	
	public String getProtocolName () {
		return protocolName;
	}
	
	public String getDisplayName () {
		return displayName;
	}
	
	public ClanRank getRank () {
		return rank;
	}
	
	public int getVarClanMember () {
		return varClanMember;
	}
	
	protected void setVarMemberBit (VarClanMember type, int value) throws VarBitOverflowException {
		if (value > type.getMaxValue() || value < 0) {
			throw new VarBitOverflowException("VarClanMember "+type.name()+" value "+value+" is outside the range of 0-"+type.getMaxValue());
		}
		setVarMemberBit(value, type.getStartBit(), type.getEndBit());
	}
	
	private void setVarMemberBit (int value, int startBit, int endBit) {
		int i_4_ = (1 << startBit) - 1;
		int i_5_ = endBit == 31 ? -1 : (1 << 1 + endBit) - 1;
		int mask = i_5_ ^ i_4_;
		value <<= startBit;
		value &= mask;
		varClanMember &= mask ^ 0xffffffff;
		varClanMember |= value;
	}
	
	public int getVarMemberBit (VarClanMember type) {
		return getVarMemberBit(type.getStartBit(), type.getEndBit());
	}
	
	public int getVarMemberBit (int startBit, int endBit) {
		int mask = (endBit == 31) ? -1 : (1 << 1 + endBit) - 1;
		return (varClanMember & mask) >>> startBit;
	}
	
	public int getJoinDay () {
		return joinDay;
	}
	
	/*public boolean isBannedFromCitadel () {
		return bannedFromCitadel;
	}*/
	
	/**
	 * Sets the rank of the clan member. Note that no checks are performed within this method, so it should be used with care.
	 * @param desiredRank	The rank to set
	 */
	protected void setRank (ClanRank desiredRank) {
		this.rank = desiredRank;
	}
}
