package org.virtue.game.logic.content.skills.fishing;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class Fishing {
	
	public static enum FishingAction {
		NET(303, -1, 621, Fish.SHRIMP, Fish.ANCHOVIES),
		BAIT(307, 313, 622, Fish.SARDINES, Fish.HERRING);
		
		public final int toolID, baitID;
		
		FishingAction (int toolID, int baitID, int animationID, Fish... possibleFish) {
			this.toolID = toolID;
			this.baitID = baitID;
		}
	}
}
