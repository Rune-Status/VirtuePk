package org.virtue.game.logic.node.entity.player.screen;

import java.util.HashMap;
import java.util.Map.Entry;

import org.virtue.Constants;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class InterfaceLayout {
	
	public static final InterfaceLayout DEFAULT_LAYOUT = new InterfaceLayout();
	
	static {
		for (int i=0;i<Constants.NIS_CONFIG.length;i++) {
			if (Constants.NIS_CONFIG[i] == 0) {
				continue;
			}
			DEFAULT_LAYOUT.setLayoutKey(i, Constants.NIS_CONFIG[i]);
		}
	}
	
	public static final int FILE_VERSION = 1;
		
	/**
	 * Represents the interface layout keys for this player
	 */
	private final HashMap<Integer, Object> interfaceLayout;

	public InterfaceLayout () {
		this.interfaceLayout = new HashMap<Integer, Object>();
	}
	
	public InterfaceLayout (InterfaceLayout layout) {
		this.interfaceLayout = new HashMap<Integer, Object>(layout.interfaceLayout);
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
	
	public void setLayoutKey (int key, Object value) {
		if (interfaceLayout.containsKey(key)) {
			interfaceLayout.remove(key);
		}
		interfaceLayout.put(key, value);
	}
}
