package org.virtue.game.logic.events.impl;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.AbstractInterface;

public class InterfaceOnPlayerEvent extends EntityInteractEvent {
	
	private final Player target;
	
	private final AbstractInterface iFace;
	
	private final int compID, slot1, slot2;

	public InterfaceOnPlayerEvent(Player player, AbstractInterface iFace, int compID, int slot1, int slot2) {
		super(player);
		this.target = player;
		this.iFace = iFace;
		this.compID = compID;
		this.slot1 = slot1;
		this.slot2 = slot2;
	}

	@Override
	public void run(Player player) {
		iFace.handleInterfaceOnPlayer(target, compID, slot1, slot2);
	}

}
