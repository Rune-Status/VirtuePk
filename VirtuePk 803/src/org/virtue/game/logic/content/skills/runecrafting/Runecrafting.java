package org.virtue.game.logic.content.skills.runecrafting;

import org.virtue.game.logic.region.Tile;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 13, 2014
 */
public class Runecrafting {
	
	public static final int RUNE_ESSENCE = 1436, PURE_ESSENCE = 7936;
	
	public static enum ExitPortal {
		AIR(2465, new Tile(3129, 3407, 0)),
		MIND(2466, new Tile(2980, 3515, 0)),
		WATER(2467, new Tile(3157, 3160, 0)),
		EARTH(2468, new Tile(3304, 3476, 0)),
		FIRE(2469, new Tile(3311, 3257, 0)),
		BODY(2470, new Tile(3055, 3443, 0)),
		COSMIC(2471, new Tile(2406, 4379, 0)),
		CHAOS(2474, new Tile(3059, 3588, 0)),
		NATURE(2473, new Tile(2867, 3017, 0)),
		LAW(2472, new Tile(2856, 3379, 0)),
		DEATH(2475, new Tile(1863, 4637, 0)),
		BLOOD(-1, new Tile(3560, 9779, 0));//TODO: Find this//x=3555, y=9770, z=0
		
		private final int id;
		private final Tile destination;
		
		ExitPortal (int id, Tile destination) {
			this.id = id;
			this.destination = destination;
		}
		
		public int getID () {
			return this.id;
		}
		
		public Tile getDestination () {
			return this.destination;
		}
		
		public static ExitPortal forID (int id) {
			for (ExitPortal portal : ExitPortal.values()) {
				if (portal.id == id) {
					return portal;
				}
			}
			return null;
		}
	}

	public static enum TiaraDefinitions {
		
		AIR_TIARA(5527, 2452), 
		MIND_TIARA(5529, 2453), 
		WATER_TIARA(5531, 2454), 
		EARTH_TIARA(5535, 2455), 
		FIRE_TIARA(5537, 2456), 
		BODY_TIARA(5533, 2457), 
		COSMIC_TIARA(5539, 2458), 
		CHAOS_TIARA(5543, 2459), 
		ASTRAL_TIARA(9106, 17010), 
		NATURE_TIARA(5541, 2460), 
		LAW_TIARA(5545, 2459), 
		DEATH_TIARA(5547, -1), 
		BLOOD_TIARA(5549, -1);

		/**
		 * Represents the item id of the tiara.
		 */
		private final int item;

		/**
		 * Represents the object id of the temple.
		 */
		private final int object;

		/**
		 * Constructs a new {@code TiaraDefinitions} {@code Object}.
		 * 
		 * @param item
		 *            the item of the tiara.
		 * @param object
		 *            the object of the temple.
		 */
		TiaraDefinitions(int item, int object) {
			this.item = item;
			this.object = object;
		}

		/**
		 * Gets the item.
		 * 
		 * @return the item
		 */
		public int getItem() {
			return item;
		}

		/**
		 * Gets the object of the temple.
		 * 
		 * @return the object of the temple.
		 */
		public int getObject() {
			return object;
		}
	}

	public enum Talisman {
		
		AIR(1438, 5527, 13630, 2452), 
		MIND(1448, 5529, 13631, 2453), 
		WATER(1444, 5531, 13632, 2454), 
		EARTH(1440, 5535, 13633, 2455), 
		FIRE(1442, 5537, 13634, 2456), 
		BODY(1446, 5533, 13635, 2457), 
		COSMIC(1454, 5539, 13636, 2458), 
		CHAOS(1452, 5543, 13637, 2461), 
		NATURE(1462, 5541, 13638, 2460), 
		LAW(1458, 5545, 13639, 2459), 
		DEATH(1456, 5547, 13640, -1), 
		BLOOD(1450, 5549, 13641, -1);

		/**
		 * Represents the item id of the talisman.
		 */
		private final int talismanID;
		
		private final int tiaraID;
		
		private final int staffID;

		/**
		 * Represents the object id of the temple.
		 */
		private final int alterID;

		/**
		 * Constructs a new {@code TalismanDefinitions} {@code Object}.
		 * 
		 * @param item
		 *            the item of the talisman.
		 * @param object
		 *            the object of the temple.
		 */
		Talisman(int talisman, int tiara, int staff, int alter) {
			this.talismanID = talisman;
			this.tiaraID = tiara;
			this.staffID = staff;
			this.alterID = alter;
		}

		/**
		 * Gets the talisman item ID.
		 * 
		 * @return the talisman item ID
		 */
		public int getTalisman() {
			return talismanID;
		}
		
		/**
		 * Gets the tiara item ID
		 * @return	the tiara item ID
		 */
		public int getTiara() {
			return tiaraID;
		}
		
		/**
		 * Gets the staff item ID
		 * @return	the staff item ID
		 */
		public int getStaff() {
			return staffID;
		}

		/**
		 * Gets the object id of the temple.
		 * 
		 * @return the object
		 */
		public int getObject() {
			return alterID;
		}
	}
	
	public enum ReturnPortalDefinition {
		AIR(-1);
		
		ReturnPortalDefinition (int id) {
			
		}
	}

	public enum TalismanStaffDefinitions {
		
		AIR_STAFF(13630, 2452), 
		MIND_STAFF(13631, 2453), 
		WATER_STAFF(13632, 2454), 
		EARTH_STAFF(13633, 2455), 
		FIRE_STAFF(13634, 2456), 
		BODY_STAFF(13635, 2457), 
		COSMIC_STAFF(13636, 2458), 
		CHAOS_STAFF(13637, 2461), 
		NATURE_STAFF(13638, 2460), 
		LAW_STAFF(13639, 2459), 
		DEATH_STAFF(13640, -1), 
		BLOOD_STAFF(13641, -1);

		/**
		 * Represents the item id of the talisman staff.
		 */
		private final int item;

		/**
		 * Represents the object id of the temple.
		 */
		private final int object;

		/**
		 * Constructs a new {@code TalismanStaffDefinitions} {@code Object}.
		 * 
		 * @param item
		 *            the item of the talisman staff.
		 * @param object
		 *            the object of the temple.
		 */
		TalismanStaffDefinitions(int item, int object) {
			this.item = item;
			this.object = object;
		}

		/**
		 * Gets the item.
		 * 
		 * @return the item
		 */
		public int getItem() {
			return item;
		}

		/**
		 * Gets the object id of the temple.
		 * 
		 * @return the object of the temple.
		 */
		public int getObject() {
			return object;
		}
	}
	
}
