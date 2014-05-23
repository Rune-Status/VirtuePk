package org.virtue.game.logic.content.skills.fishing;

import org.virtue.game.logic.node.entity.npc.NPC;
import org.virtue.game.logic.node.entity.npc.NPCOption;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.region.Tile;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class FishingSpot extends NPC {
	
	public static enum Type {
		NET_BAIT(327),
		NET_BAIT_1(330),
		BAIT_LURE(328),
		CAGE_HARPOON(312),
		NET_HARPOON(313),
		CAVEFISH_SHOAL(8841),
		ROCKTAIL_SHOAL(8842);
		//233->Fishing spot (Bait)
		//234->Fishing spot (Bait)
		//235->Fishing spot (Net/Bait)
		//236->Fishing spot (Net/Bait)	
		
		private final int id;
		
		Type (int id, Fish... possibleFish) {
			this.id = id;
		}
		
		public int getID () {
			return id;
		}
		
		public static Type forID (int id) {
			for (Type t : Type.values()) {
				if (t.id == id) {
					return t;
				}
			}
			return null;
		}
	}

	public FishingSpot(Tile tile, Type type) {
		super(type.getID(), tile);
	}
	
	@Override
	public void onCycle() {
		//TODO: This step should handle the random movement of the fishing spot
	}
	
	@Override
	public void interact(Player player, NPCOption option) {
		System.out.println("Clicked fishing spot: npcIndex="+getIndex()+", id="+getID()+", xCoord="+getTile().getX()+", yCoord="+getTile().getY()+", optionID="+option.getID());
	}
	
}
