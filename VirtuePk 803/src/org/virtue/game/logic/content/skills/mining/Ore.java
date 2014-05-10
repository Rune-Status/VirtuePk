package org.virtue.game.logic.content.skills.mining;

public enum Ore {
	CLAY(-1), 
	COPPER(-1), 
	TIN(-1), 
	IRON(-1), 
	SILVER(-1), 
	COAL(-1), GOLD(-1), MITHRIL(-1), ADAMANT(-1), RUNITE(-1);
	
	private int itemID;
	private int emptyRockID;
	private String name;
	private double experience;
	private int levelRequired;
	
	Ore (int itemID) {
		this.itemID = itemID;
	}

}
