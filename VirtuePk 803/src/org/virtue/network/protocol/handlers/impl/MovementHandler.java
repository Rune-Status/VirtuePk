package org.virtue.network.protocol.handlers.impl;

import org.virtue.Constants;
import org.virtue.game.logic.node.entity.player.update.movement.Movement;
import org.virtue.game.logic.region.Tile;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.protocol.packet.encoder.impl.MinimapFlagEncoder;
import org.virtue.network.session.impl.WorldSession;

public class MovementHandler extends PacketHandler<WorldSession> {

	@Override
	public void handle(WorldSession session) {
		int baseX = getFlag("baseX", -1);
		int baseY = getFlag("baseY", -1);
		boolean forceRun = getFlag("forceRun", false);
		int currentX = session.getPlayer().getTile().getX();
		int currentY = session.getPlayer().getTile().getY();
		int sizeX = getFlag("sizeX", 0);
		int sizeY = getFlag("sizeY", 0);
		
		session.getPlayer().setCoordinateEvent(null);
		session.getPlayer().getInterfaces().closeTopInterface();
		if (!getFlag("facing", false)) {
			if (((WorldSession) session).getPlayer().getUpdateArchive().getFaceEntity() != null) {
				((WorldSession) session).getPlayer().getUpdateArchive().queueFaceEntity(null);
			}
		}
		//System.out.println("Movement request: x="+baseX+", y="+baseY+", forceRun="+forceRun);
		Movement movement = session.getPlayer().getUpdateArchive().getMovement();
		movement.reset();
		if (forceRun) {
			if (!movement.isForceRun()) {
				movement.swapRunning(true);
			}
		} else if (movement.isForceRun()) {
			movement.swapRunning(false);
		}
		movement.setNeedsTypeUpdate(true);
		//movement.setRunning(true);
		movement.calculateWalkStepsInteract(new Tile(baseX, baseY, session.getPlayer().getTile().getPlane()), Constants.MAX_WALK_STEPS, sizeX, sizeY);
		//movement.addWalkStepsInteract(baseX, baseY, Constants.MAX_WALK_STEPS, sizeX, sizeY, true);
		int targetX = movement.getLastWalkTile()[0];
		int targetY = movement.getLastWalkTile()[1];
		Tile target = new Tile(targetX, targetY, 0);
		Tile flagPos = null;
		if (targetX - currentX != 0 || targetY - currentY != 0) {
			flagPos = new Tile(target.getLocalX(session.getPlayer().getViewport().getLastLoadedTile()),
					target.getLocalY(session.getPlayer().getViewport().getLastLoadedTile()), 0);
			//target = Tile.edit(target, session.getPlayer().getLastTile(), yOff, 0)
		}//Change the minimap flag if the destination is not reachable
		session.getTransmitter().send(MinimapFlagEncoder.class, flagPos);
	}

}
