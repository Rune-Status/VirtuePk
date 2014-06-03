package org.virtue.game.logic.content.skills.fishing;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class Fishing {
	
	public static enum FishingType {
		NET(303, -1, 621, Fish.SHRIMP, Fish.ANCHOVIES),
		BAIT(307, 313, 622, Fish.SARDINES, Fish.HERRING),
		RIVER_BAIT(307, 313, 622, Fish.PIKE),
		RIVER_LURE(309, 314, 622, Fish.TROUT, Fish.SALMON),
		CAGE(301, -1, 619, Fish.LOBSTER),
		HARPOON_1(311, -1, 618, Fish.TUNA, Fish.SWORDFISH),
		BIG_NET(305, -1, 620, Fish.SEAWEED, Fish.OYSTER, Fish.MACKEREL, Fish.COD, Fish.BASS),
		HARPOON_2(311, -1, 618, Fish.SHARK),
		CAVEFISH_SHOAL(307, 313, 622, Fish.CAVEFISH),
		ROCKTAIL_SHOAL(307, 15263, 622, Fish.ROCKTAIL);
		
		public final int toolID, baitID;
		
		public final int emoteID;
		
		public final int minLevel;
		
		private final Fish[] catches;
		
		FishingType (int toolID, int baitID, int animationID, Fish... possibleFish) {
			this.toolID = toolID;
			this.baitID = baitID;
			this.emoteID = animationID;
			int level = 255;
			for (Fish fish : possibleFish) {
				if (fish.getLevel() < level) {
					level = fish.getLevel();
				}
			}
			this.minLevel = level;
			this.catches = possibleFish;
		}
	
		/**
	      * Gets the item ID of the required tool
	      * @return The tool ID
	      */
		public int getToolID () {
			return toolID;
		}
	
		/**
	      * Gets the item ID of the required tool
	      * @return The bait ID, or -1 if no bait is required
	      */
		public int getBaitID () {
			return baitID;
		}
		
		/**
		 * Gets the ID of the animation performed while fishing at this spot
		 * @return	The emote ID
		 */
		public int getEmote () {
			return emoteID;
		}
		
		/**
		 * Gets the minimum level needed to catch fish at this spot
		 * @return	The minimum level
		 */
		public int getMinLevel () {
			return minLevel;
		}
		
		/**
		 * Returns the possible items which can be caught at this spot
		 * @return	An array of possible catches
		 */
		public Fish[] getCatches () {
			return catches;
		}
	}
	
	public enum Fish {
		//F2P
		SHRIMP(317, 1, 10, 1),
		ANCHOVIES(321, 15, 40, 1),
		SARDINES(327, 5, 20, 1),
		HERRING(345, 10, 30, 1),
		PIKE(349, 25, 60, 1),
		TROUT(335, 20, 50, 1),
		SALMON(331, 30, 70, 1),
		LOBSTER(377, 40, 90, 1),
		TUNA(359, 35, 80, 1),
		SWORDFISH(371, 50, 100, 1),
		//P2P
		SEAWEED(401, 1, 1, 1),
		OYSTER(407, 1, 10, 1),
		MACKEREL(353, 16, 20, 1),
		COD(341, 23, 45, 1),
		BASS(363, 46, 100, 1),
		SHARK(383, 76, 110, 1),
		CAVEFISH(15264, 85, 300, 1),
		ROCKTAIL(15270, 90, 385, 1);
		
		private final int id, level;
		private final double xp;
		
		/**
		 * Represents a type of fish
		 * @param id	The item ID for the raw fish
		 * @param level	The level required to catch the fish
		 * @param xp	The amount of xp gained if the fish is caught
		 * @param ratio	The ratio of the fish of this type caught compared to fish of other types at the same spot
		 */
		Fish(int id, int level, double xp, int ratio) {
			this.id = id;
			this.level = level;
			this.xp = xp;
		}
		
		/**
		 * Gets the item ID of the raw fish
		 * @return	The item ID
		 */
		public int getID () {
			return id;
		}
		
		/**
		 * Gets the amount of experience gained when the fish is successfully caught
		 * @return	The experience gained
		 */
		public double getExperience () {
			return xp;
		}
		
		/**
		 * Gets the level needed to catch this fish
		 * @return	The level needed
		 */
		public int getLevel () {
			return level;
		}
	}
}
