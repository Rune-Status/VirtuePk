package org.virtue.game.logic.social.internal;

public enum ChannelPermission {
	JOIN(2, -1), TALK(3, -1), KICK(4, 2), LOOTSHARE(5, 0);
	
	private final int settingsID;
	private final int minRank;
	
	ChannelPermission(int settingsID, int minRank) {
		this.settingsID = settingsID;
		this.minRank = minRank;
	}
	
	public int getSettingsID () {
		return settingsID;
	}
	
	public int getMinRank () {
		return minRank;
	}
	
	public static ChannelPermission forComponent (int compID) {
		if ((compID-2) >= 0 && (compID-2) < ChannelPermission.values().length) {
			ChannelPermission p = ChannelPermission.values()[compID-2];
			if (p.settingsID == compID) {
				return p;
			}
		}
		for (ChannelPermission p : ChannelPermission.values()) {
			if (p.settingsID == compID) {
				return p;
			}
		}
		return null;
	}
}