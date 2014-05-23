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

import org.virtue.utility.StringUtils;
import org.virtue.utility.StringUtils.FormatType;

/**
 * Represents the data for a member of a clan
 *
 * @author Sundays211
 */
public class ClanMember {
	
	private final String protocolName;
	
	private String displayName;
	
	private ClanRank rank;
	
	public ClanMember (String protocolName) {
		this(protocolName, ClanRank.RECRUIT);
	}
	
	public ClanMember (String protocolName, ClanRank rank) {
		this.protocolName = protocolName;
		this.displayName = StringUtils.format(protocolName, FormatType.DISPLAY);
		this.rank = rank;
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
}
