package org.virtue.game.logic.content.skills.woodcutting;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public enum Hatchet {
	BRONZE(1351, 1, 879, 1),
	IRON(1349, 1, 877, 2),
	STEEL(1353, 6, 875, 3),
	BLACK(1361, 11, 873, 4),
	MITHRIL(1355, 21, 871, 5),
	ADAMANT(1357, 31, 869, 7),
	RUNE(1359, 41, 867, 10),
	DRAGON(6739, 61, 2846, 13),
	INFERNO(13661, 61, 10251, 13);
	
	private final int itemID;
	private final int useLevel;
	private final int emoteID;
	private final int chopTime;
	
	Hatchet (int itemID, int level, int emoteID, int chopTime) {
		this.itemID = itemID;
		this.useLevel = level;
		this.emoteID = emoteID;
		this.chopTime = chopTime;
	}
	
	public int getItemID () {
		return itemID;
	}
	
	public int getLevel () {
		return useLevel;
	}
	
	public int getEmote () {
		return emoteID;
	}
	
	public int getTimeDiscount () {
		return chopTime;
	}
	
	public static Hatchet forItemID (int itemID) {
		for (Hatchet a : Hatchet.values()) {
			if (a.itemID == itemID) {
				return a;
			}
		}
		return null;
	}
}
