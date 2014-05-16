package org.virtue.game.logic.node.entity.player.screen;

import java.util.HashMap;
import java.util.Map.Entry;

import org.virtue.Constants;
import org.virtue.utility.DisplayMode;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
	 * Represents the interface layout keys for this player
	 */
	private HashMap<Integer, Object> interfaceLayout = new HashMap<Integer, Object>();
	
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
	
	public void setLayoutKey (int key, Object value) {
		if (interfaceLayout.containsKey(key)) {
			interfaceLayout.remove(key);
		}
		interfaceLayout.put(key, value);
	}
	
	public HashMap<Integer, Object> getLayoutInfo () {
		//System.out.println("Layout info size: "+interfaceLayout.size());
		if (interfaceLayout.isEmpty()) {
			//System.out.println("The interface layout is empty. Resetting to defaults....................");
			setDefaultLayout();
		}
		return interfaceLayout;
	}
	
	public void setDefaultLayout () {
		//System.out.println("Setting default layout...");
		interfaceLayout.clear();
		for (int i=0;i<Constants.NIS_CONFIG.length;i++) {
			if (Constants.NIS_CONFIG[i] == 0) {
				continue;
			}
			interfaceLayout.put(i, Constants.NIS_CONFIG[i]);
		}
	}
	
	public void deserialiseLayout (JsonArray data) {
		setDefaultLayout();
		if (data != null) {
			for (JsonElement setting : data) {
				JsonObject keyValuePair = setting.getAsJsonObject();
				setLayoutKey(keyValuePair.get("key").getAsInt(), keyValuePair.get("value").getAsInt());
			}
		}
		//System.out.println("Initialised layout. Size="+interfaceLayout.size());
	}
	
	public JsonArray serialiseLayout () {
		//System.out.println("Serialising layout. Size="+interfaceLayout.size());
		JsonArray data = new JsonArray();
		for (Entry<Integer, Object> value : interfaceLayout.entrySet()) {
			JsonObject obj = new JsonObject();
			obj.addProperty("key", value.getKey());
			obj.addProperty("value", (Integer) value.getValue());
			data.add(obj);
		}
		return data;
	}
	
	/*public int[] getNisInit () {
		return Constants.NIS_CONFIG;
	}*/
}
