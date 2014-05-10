package org.virtue.game.logic.events.impl;

import org.virtue.game.logic.events.CoordinateEvent;
import org.virtue.game.logic.node.entity.npc.NPC;
import org.virtue.game.logic.node.entity.npc.NPCOption;
import org.virtue.game.logic.node.entity.player.Player;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class NPCInteractEvent extends CoordinateEvent {
	
	private final NPC npc;
	private final NPCOption option;

	public NPCInteractEvent(NPC npc, NPCOption option) {
		super(npc.getTile(), npc.getSize(), npc.getSize());
		this.npc = npc;
		this.option = option;
	}

	@Override
	public void run(Player player) {
		npc.interact(player, option);
	}
	
}
