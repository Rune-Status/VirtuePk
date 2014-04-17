package org.virtue.network.protocol.handlers.impl;

import org.virtue.game.logic.node.entity.player.update.movement.Movement;
import org.virtue.game.logic.node.entity.region.Tile;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.protocol.packet.encoder.impl.MinimapFlagEncoder;
import org.virtue.network.session.impl.WorldSession;

public class MovementHandler extends PacketHandler<WorldSession> {

	@Override
	public void handle(WorldSession session) {
		int baseX = getFlag("baseX", -1);
		int baseY = getFlag("baseY", -1);
		boolean forceRun = getFlag("forceRun", false);
		int maxStepCount = 100;
		int currentX = session.getPlayer().getTile().getX();
		int currentY = session.getPlayer().getTile().getY();
		int sizeX = baseX - currentX;//TODO: Figure out which values to use...
		int sizeY = baseY - currentY;//TODO: Figure out which values to use...
		
		//System.out.println("Movement request: x="+baseX+", y="+baseY+", forceRun="+forceRun);
		Movement movement = session.getPlayer().getUpdateArchive().getMovement();
		movement.clearWalkSteps();
		if (forceRun) {
			//TODO: Implement run handling
			//movement.swapRunning();
		}
		boolean successful = movement.addWalkStepsInteract(baseX, baseY, maxStepCount, sizeX, sizeY, true);
		if (!successful) {
			int targetX = movement.getLastWalkTile()[0];
			int targetY = movement.getLastWalkTile()[1];
			Tile target = null;
			if (targetX - currentX != 0 || targetY - currentY != 0) {
				target = new Tile(targetX, targetY, session.getPlayer().getTile().getPlane());
			}//Change the minimap flag if the destination is not reachable
			session.getTransmitter().send(MinimapFlagEncoder.class, target);
		}
	}

}
