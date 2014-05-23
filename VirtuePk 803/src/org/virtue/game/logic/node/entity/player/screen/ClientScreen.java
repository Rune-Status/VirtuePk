package org.virtue.game.logic.node.entity.player.screen;

import java.io.IOException;

import org.virtue.network.io.IOHub;
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
	 * Represents the interface layout for this player
	 */
	private InterfaceLayout interfaceLayout = new InterfaceLayout(InterfaceLayout.DEFAULT_LAYOUT);
	//private HashMap<Integer, Object> interfaceLayout = new HashMap<Integer, Object>();
	
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
	
	public InterfaceLayout getLayout () {
		return interfaceLayout;
	}
	
	public void setLayout (InterfaceLayout interfaceLayout) {
		this.interfaceLayout = interfaceLayout;
	}
	
	/*public void setLayoutKey (int key, Object value) {
		interfaceLayout.setLayoutKey(key, value);
	}*/
	
	/*public HashMap<Integer, Object> getLayoutInfo () {
		return interfaceLayout.getLayoutInfo();
	}*/
	
	/*public void setDefaultLayout () {
		interfaceLayout.setDefaultLayout();
	}*/
	
	public void loadLayout (String layoutFileName) {
		InterfaceLayout oldLayout = interfaceLayout;
		try {
			interfaceLayout = IOHub.getInterfaceIO().load(layoutFileName);
		} catch (IOException ex) {
			interfaceLayout = oldLayout;
		}
	}
	
	/*public void deserialiseLayout (JsonArray data) {
		interfaceLayout.deserialiseLayout(data);
	}*/
	
	/*public JsonArray serialiseLayout () {
		return interfaceLayout.serialiseLayout();
	}*/
	
	/*public int[] getNisInit () {
		return Constants.NIS_CONFIG;
	}*/
}
