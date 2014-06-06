package org.virtue.network.protocol.handlers.impl;

import org.virtue.game.logic.World;
import org.virtue.game.logic.events.impl.NPCInteractEvent;
import org.virtue.game.logic.node.entity.npc.NPC;
import org.virtue.game.logic.node.entity.npc.NPCOption;
import org.virtue.network.session.impl.WorldSession;

/**
 * 
 * @author Virtue Development Team 2014 (c).
 */
public class NpcOptionHandler extends MovementHandler {

	@Override
	public void handle(WorldSession session) {		
		NPCOption option = NPCOption.fromOpcode(getFlag("opcode", -1));
		if (option == null) {
			throw new RuntimeException("Invalid NPC Opcode: "+getFlag("opcode", -1));			
		}
		int npcIndex = getFlag("npcIndex", -1);
		if (npcIndex < 0 || npcIndex > World.getWorld().getNpcs().size()) {
			throw new RuntimeException("Invalid NPC Index: "+npcIndex);	
		}
		NPC npc = World.getWorld().getNpcs().get(npcIndex);
		if (npc == null) {
			return;//NPC does not exist; ignore request
		}	
		if (npc.isInteractOption(option)) {
			int baseX = npc.getTile().getX();
			int baseY = npc.getTile().getY();	
			session.getPlayer().getUpdateArchive().queueFaceEntity(npc);
			putFlag("facing", true);
			putFlag("baseX", baseX);
			putFlag("baseY", baseY);
			putFlag("sizeX", npc.getDefinition().getSize());
			putFlag("sizeY", npc.getDefinition().getSize());
			super.handle(session);//Handle the movement aspect.
			session.getPlayer().setCoordinateEvent(new NPCInteractEvent(npc, option));
		} else {
			npc.handleDistanceOption(session.getPlayer(), option);
			session.getPlayer().getPacketDispatcher().dispatchMinimapFlag(null);
		}
		//System.out.println("Clicked NPC: npcIndex="+npcIndex+", id="+npc.getId()+", xCoord="+baseX+", yCoord="+baseY+", optionID="+option.getID());
	}
}
