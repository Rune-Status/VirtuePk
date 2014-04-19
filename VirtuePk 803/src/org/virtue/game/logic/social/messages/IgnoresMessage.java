package org.virtue.game.logic.social.messages;

import org.virtue.game.logic.social.Ignore;

public class IgnoresMessage {
	
	private Ignore[] ignores;
	
	private boolean isNameChange;
	private boolean fullUpdate;
	
	public IgnoresMessage (Ignore[] ignores) {
		this.ignores = ignores;
		fullUpdate = true;
	}
	
	public IgnoresMessage (Ignore i, boolean isNameChange) {
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
