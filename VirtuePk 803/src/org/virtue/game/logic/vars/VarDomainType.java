package org.virtue.game.logic.vars;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public enum VarDomainType {
	PLAYER(0), NPC(1), CLIENT(2), WORLD(3), REGION(4), OBJECT(5), CLAN(6), 
	CLAN_SETTINGS(7), UNKNOWN_1(8), UNKNOWN_2(10);
	
	private final int id;
	
	VarDomainType(int id) {
		this.id = id;
	}
	
	public int getID () {
		return this.id;
	}
	
	public static VarDomainType forID (int id) {
		VarDomainType domain = VarDomainType.values()[id];
		if (domain.id == id) {
			return domain;
		} else {
			for (VarDomainType type : VarDomainType.values()) {
				if (type.id == id) {
					return type;
				}
			}
		}
		return null;
	}
}
