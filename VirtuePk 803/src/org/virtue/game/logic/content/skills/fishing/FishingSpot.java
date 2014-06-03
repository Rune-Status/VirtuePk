package org.virtue.game.logic.content.skills.fishing;

import org.virtue.game.logic.content.skills.fishing.Fishing.FishingType;
import org.virtue.game.logic.node.entity.npc.NPC;
import org.virtue.game.logic.node.entity.npc.NPCOption;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.identity.Rank;
import org.virtue.game.logic.region.Tile;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class FishingSpot extends NPC {
	
	public static enum Type {
		NET_BAIT(327, FishingType.NET, FishingType.BAIT),
		NET_BAIT_1(330, FishingType.NET, FishingType.BAIT),
		BAIT_LURE(328, FishingType.RIVER_BAIT, FishingType.RIVER_LURE),
		CAGE_HARPOON(312, FishingType.CAGE, FishingType.HARPOON_1),
		NET_HARPOON(313, FishingType.BIG_NET, FishingType.HARPOON_2),
		CAVEFISH_SHOAL(8841, FishingType.CAVEFISH_SHOAL),
		ROCKTAIL_SHOAL(8842, FishingType.ROCKTAIL_SHOAL);
		//233->Fishing spot (Bait)
		//234->Fishing spot (Bait)
		//235->Fishing spot (Net/Bait)
		//236->Fishing spot (Net/Bait)	
		
		private final int id;
		
		private final FishingType action1, action2;
		
		Type (int id, FishingType action) {
			this(id, action, null);
		}
		
		Type (int id, FishingType action1, FishingType action2) {
			this.id = id;
			this.action1 = action1;
			this.action2 = action2;
		}
		
		public int getID () {
			return id;
		}
		
		public FishingType getAction1 () {
			return action1;
		}
		
		public FishingType getAction2 () {
			return action2;
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
	
	private final Type type;

	public FishingSpot(Tile tile, Type type) {
		super(type.getID(), tile);
		this.type = type;
	}
	
	@Override
	public void onCycle() {
		super.onCycle();
		//TODO: This step should handle the random movement of the fishing spot
	}
	
	@Override
	public void interact(Player player, NPCOption option) {
		String message = "Clicked fishing spot: npcIndex="+getIndex()+", id="+getID()+", xCoord="+getTile().getX()+", yCoord="+getTile().getY()+", optionID="+option.getID();
		if (option.equals(NPCOption.ONE) && type.getAction1() != null) {
			player.setActionEvent(new FishingAction(this, type.getAction1()));
		} else if (option.equals(NPCOption.THREE) && type.getAction2() != null) {
			player.setActionEvent(new FishingAction(this, type.getAction2()));
		} else {
			System.out.println(message);
			if (player.getAccount().getRank().equals(Rank.ADMINISTRATOR)) {
				player.getPacketDispatcher().dispatchMessage(message);
			}
		}
	}
	
}
