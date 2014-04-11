package org.virtue.utility;

/**
 * @author Taylor
 * @version 1.0
 */
public enum DisplayMode {

	/**
	 * Represents a <i>fixed</i> screen.
	 */
	FIXED(548), 
	
	/**
	 * Represents a <i>resizable</i> screen.
	 */
	RESIZABLE(746), 
	
	/**
	 * Represents a <i>full</i> screen.
	 */
	FULL(748),
	
	/**
	 * Represents the lobby screen
	 */
	LOBBY(906),
	
	LOGIN(1477);
	
	/**
	 * Represents the screen ID.
	 */
	private final int screenId;
	
	/**
	 * Constructs a new {@code DisplayMode.java}.
	 * @param screenId The screen ID.
	 */
	DisplayMode(int screenId) {
		this.screenId = screenId;
	}

	/**
	 * @return The screenId
	 */
	public int getScreenId() {
		return screenId;
	}
	
	/**
	 * Returns the display mode corresponding to the mode ID.
	 * @param displayMode The ID.
	 * @return The mode.
	 */
	public static DisplayMode forId(int displayMode) {
		for (DisplayMode displayModes : DisplayMode.values()) {
			if (displayModes == null) {
				continue;
			}
			if ((displayModes.ordinal() + 1) == displayMode) {
				return displayModes;
			}
		}
		return null;
	}
}
