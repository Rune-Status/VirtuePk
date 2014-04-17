package org.virtue.game.logic.node.entity.player.screen;

import org.virtue.Constants;
import org.virtue.utility.DisplayMode;

public class ClientScreen {
	
	/**
	 * Represents the height of the client screen
	 */
	private int screenHeight;
	
	/**
	 * Represents the width of the client screen
	 */
	private int screenWidth;
	
	/**
	 * Represents the display mode of the client
	 */
	private DisplayMode displayMode;
	
	/**
	 * Represents the current window pane (aka root pane, game screen, etc)
	 */
	private int rootPaneID;
	
	/**
	 * Specifies the root pane for the client
	 * @param paneID	The interface ID of the root pane
	 */
	public void setRootPane (int paneID) {
		this.rootPaneID = paneID;
	}
	
	/**
	 * Specifies the client screen information
	 * @param height		The height of the client window
	 * @param width			The width of the client window
	 * @param displayMode	The display mode of the client
	 */
	public void setScreenInfo (int height, int width, DisplayMode displayMode) {
		this.screenHeight = height;
		this.screenWidth = width;
		this.displayMode = displayMode;
	}
	
	public int getRootPane () {
		return rootPaneID;
	}
	
	public int getHeight () {
		return screenHeight;
	}
	
	public int getWidth () {
		return screenWidth;
	}
	
	public DisplayMode getDisplayMode () {
		return displayMode;
	}
	
	
	public int[] getNisInit () {
		return Constants.NIS_CONFIG;
	}
}
