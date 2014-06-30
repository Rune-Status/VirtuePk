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
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.game.logic.node.interfaces.RS3Interface;
import org.virtue.utility.StringUtils;
import org.virtue.utility.StringUtils.FormatType;

/**
 * The lodestone teleport interface
 *
 * @author Sundays211
 */
public class LodestoneInterface extends AbstractInterface {

	public LodestoneInterface(Player p) {
		super(RS3Interface.LODESTONE, p);
	}

	@Override
	public void postSend() {

	}

	@Override
	public void handleActionButton(int component, int slot1, int slot2, ActionButton button) {
		Lodestone selectedStone = null;
		for (Lodestone stone : Lodestone.values()) {
			if (stone.getInterfaceComponentID() == component) {
				selectedStone = stone;
				break;
			}
		}
		if (selectedStone != null) {
			if (selectedStone.isActivated(player)) {
				System.out.println("Attempting to teleport to "+selectedStone);			
				close();
				player.setActionEvent(new HomeTeleportAction(selectedStone));
			} else {
				player.getPacketDispatcher().dispatchMessage("You'll need to activate the "+
						StringUtils.format(selectedStone.name(), FormatType.NAME)+" lodestone before you can Home Teleport there.");
			}
		} else {
			System.out.println("Unhandled lodestone button: component="+component+", button="+button+", slot1="+slot1+", slot2="+slot2);
		}
	}

	@Override
	public int getTabID() {
		return -1;
	}

}
