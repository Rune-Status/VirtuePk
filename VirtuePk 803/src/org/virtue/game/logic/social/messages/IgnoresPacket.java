package org.virtue.game.logic.social.messages;

import org.virtue.game.logic.social.internal.Ignore;

public class IgnoresPacket {
	
	private Ignore[] ignores;
	
	private boolean isNameChange;
	private boolean fullUpdate;
	
	public IgnoresPacket (Ignore[] ignores) {
		this.ignores = ignores;
		fullUpdate = true;
	}
	
	public IgnoresPacket (Ignore i, boolean isNameChange) {
		ignores = new Ignore[] { i.clone() };
		this.isNameChange = isNameChange;
	}
	
	public boolean isFullUpdate () {
		return fullUpdate;
	}
	
	public boolean isNameChange () {
		return isNameChange;
	}
	
	public Ignore[] getIgnores () {
		return ignores;
	}
}
