package org.virtue.game.logic.content.skills.mining;

public enum Pickaxe {
	BRONZE(1265, 1, 625, 1), 
	IRON(1267, 1, 626, 2), 
	STEEL(1269, 6, 627, 3), 
	MITHRIL(1273, 21, 629, 5), 
	ADAMANT(1271, 31, 628, 7), 
	RUNE(1275, 41, 624, 10),
	DRAGON(15259, 61, 12190, 13);
	
	private final int itemID;
	private final int useLevel;
	private final int emoteID;
	private final int mineTime;
	
	Pickaxe (int itemID, int level, int emoteID, int mineTime) {
		this.itemID = itemID;
		this.useLevel = level;
		this.emoteID = emoteID;
		this.mineTime = mineTime;
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
		return mineTime;
	}
	
	public static Pickaxe forItemID (int itemID) {
		for (Pickaxe p : Pickaxe.values()) {
			if (p.itemID == itemID) {
				return p;
			}
		}
		return null;
	}
}
