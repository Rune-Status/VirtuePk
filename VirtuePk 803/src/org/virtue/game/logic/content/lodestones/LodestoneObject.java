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
import org.virtue.game.logic.node.object.ObjectOption;
import org.virtue.game.logic.node.object.RS3Object;
import org.virtue.game.logic.region.Tile;
import org.virtue.utility.StringUtils;
import org.virtue.utility.StringUtils.FormatType;

/**
 * A lodestone object
 *
 * @author Sundays211
 */
public class LodestoneObject extends RS3Object {
	
	private final Lodestone lodestone;
	
	public LodestoneObject(Lodestone definition, int rotation, int type, Tile tile) {
		super(definition.getBaseID(), rotation, type, tile);
		this.lodestone = definition;
	}
	
	public Lodestone getLodestone () {
		return lodestone;
	}
	
	@Override
	public void interact (Player player, ObjectOption option) {		
		if (option.equals(ObjectOption.ONE) && !lodestone.isActivated(player)) {
			lodestone.activate(player);
			player.getPacketDispatcher().dispatchMessage("You have activated the "+
					StringUtils.format(lodestone.name(), FormatType.NAME)+" lodestone");
		} else {
			System.out.println("Clicked lodestone: id="+getId()+", location="+getTile()+", option="+option);
		}
	}

}
