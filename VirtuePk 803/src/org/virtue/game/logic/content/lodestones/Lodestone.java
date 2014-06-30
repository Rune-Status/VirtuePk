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
package org.virtue.game.logic.content.lodestones;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.region.Tile;

/**
 * The definition for a lodestone
 *
 * @author Sundays211
 */
public enum Lodestone {
	BANDIT_CAMP(-1, null, -1, 9),//Object 69827 clicked at x=3214, y=2955 does not exist!
	LUNAR(-1, null, -1, 20),
	AL_KHARID(69829, new Tile(3297, 3185, 0), 28, 21),
	ARDOUGNE(69830, new Tile(2634, 3349, 0), 29, 22),
	BURTHORPE(69831, new Tile(2899, 3545, 0), 30, 23),
	CATHERBY(69832, new Tile(2831, 3452, 0), 31, 24),
	DRAYNOR_VILLAGE(69833, new Tile(3105, 3299, 0), 32, 25),
	EDGEVILLE(69834, new Tile(3067, 3506, 0), 33, 26),
	FALADOR(69835, new Tile(2967, 3404, 0), 34, 27),
	LUMBRIDGE(69836, new Tile(3233, 3222, 0), 35, 28),//x=2056, y=6205, z=0 x=2529, y=6422, z=0
	PORT_SARIM(69837, new Tile(3011, 3216, 0), 36, 29),
	SEERS_VILLAGE(69838, new Tile(2689, 3483, 0), 37, 30),
	TAVERLY(69839, new Tile(2878, 3443, 0), 38, 31),
	VARROCK(69840, new Tile(3214, 3377, 0), 39, 32),
	YANILLE(69841, new Tile(2529, 3095, 0), 40, 33),
	CANAFIS(84748, new Tile(3517, 3516, 0), 18523, 34),
	EAGLES_PEAK(84749, new Tile(2366, 3480, 0), 18524, 35),
	FREMENNIK(-1, null, -1, 36),
	KARAMJA(84751, new Tile(2761, 3148, 0), 18526, 37),
	OOGLOG(84752, new Tile(2532, 2872, 0), 18527, 38),
	TIRANNWN(-1, null, -1, 39),
	WILDERNESS(84754, new Tile(3143, 3636, 0), 18529, 40),
	ASHDALE(-1, null, -1, -1);
	
	private final int baseID;
	
	private final Tile location;
	
	private final int varBitID;
	
	private final int ifaceComponent;
	
	Lodestone(int baseID, Tile location, int varBitID, int ifaceComponent) {
		this.baseID = baseID;
		this.location = location;
		this.varBitID = varBitID;
		this.ifaceComponent = ifaceComponent;
	}
	
	public int getBaseID () {
		return baseID;
	}
	
	public Tile getLocation () {
		return location;
	}
	
	public int getVarBitID () {
		return varBitID;
	}
	
	public int getInterfaceComponentID () {
		return ifaceComponent;
	}
	
	public boolean isActivated (Player player) {
		return player.getVarManager().getVarBit(varBitID) == 1;
	}
	
	public void activate (Player player) {
		player.getVarManager().setVarBit(varBitID, 1);
	}
	
	public static Lodestone forObjectID (int id) {
		for (Lodestone l : Lodestone.values()) {
			if (l.baseID == id) {
				return l;
			}
		}
		return null;
	}
}