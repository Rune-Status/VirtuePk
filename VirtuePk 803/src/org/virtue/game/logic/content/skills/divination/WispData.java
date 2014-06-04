package org.virtue.game.logic.content.skills.divination;


/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 13, 2014
 */
public enum WispData {
	

	PALE(18150, 1, 0), 
	FLICKERING(18151, 10, 0), 
	BRIGHT(18153, 20, 0), 
	GLOWING(18155, 30, 0), 
	SPARKLING(18157, 40, 0), 
	GLEAMING(18159, 50, 0), 
	VIBRANT(18161, 60, 0), 
	LUSTROUS(18163, 70, 0), 
	BRILLIANT(18165, 80, 0), 
	RADIANT(18167, 85, 0), 
	LUMINOUS(18169, 90, 0), 
	INCANDESCENT(18171, 95, 0);

	private int npcId;
	private int level;

	private WispData(int npcId, int level, int experience) {
		this.npcId = npcId;
		this.level = level;
	}

	public static WispData forNpcID(int id) {
		for (WispData info : WispData.values()) {
			if (info.getNpcId() == id)
				return info;
		}
		return null;
	}

	public int getHarvestXp() {
		return this.ordinal() + 1;
	}

	public int getNpcId() {
		return npcId;
	}

	public int getSpringNpcID() {
		return npcId + 23;
	}

	public int getEnrichedSpringNpcId() {
		return getEnrichedNpcId() + 23;
	}

	public int getEnrichedNpcId() {
		if (this == PALE)
			return 18150;
		return npcId + 1;
	}

	public int getLevel() {
		return level;
	}

	public int getEnergyId() {
		return 29313 + this.ordinal();
	}

	public int getMemoryId() {
		return 29384 + this.ordinal();
	}

	public int getEnrichedMemoryId() {
		if (this == PALE)
			return 29384;
		return 29384 + (this.ordinal()) + 11;
	}

}
