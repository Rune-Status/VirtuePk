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

import org.virtue.game.logic.events.PlayerActionEvent;
import org.virtue.game.logic.node.entity.player.Player;

/**
 * A home teleport action. Teleports the player to the target lodestone
 *
 * @author Sundays211
 */
public class HomeTeleportAction extends PlayerActionEvent {
	
	private final Lodestone target;
	
	public HomeTeleportAction (Lodestone target) {
		this.target = target;
	}

	@Override
	public boolean start(Player player) {
		//TODO: Make sure the player is able to teleport and set the emote		
		return true;
	}

	@Override
	public boolean process(Player player) {
		player.teleport(target.getLocation());//TODO: Implement landing animation
		return false;
	}

	@Override
	public void stop(Player player) {
		player.getUpdateArchive().queueAnimation(-1);
	}
	
}
