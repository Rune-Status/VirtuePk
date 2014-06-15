package org.virtue.network.io.channel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.virtue.game.logic.vars.VarManager;
import org.virtue.network.io.IOParser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VarParser implements IOParser<int[]> {
	
	private File SAVE_PATH = new File("data/characters/vars/");

	@Override
	public int[] load(Object... params) throws FileNotFoundException {
		File file = new File(getPath(), params[0]+".json");
		int varCount = VarManager.VARP_COUNT;
		JsonParser parser = new JsonParser();
		JsonElement parsed = parser.parse(new FileReader(file));
		int[] vars = new int[varCount];
		for (JsonElement varpData : ((JsonArray) parsed)) {
			JsonObject data = varpData.getAsJsonObject();
			int key = data.get("key").getAsInt();
			int value = data.get("value").getAsInt();
			vars[key] = value;
		}
		return vars;
	}

	@Override
	public boolean save(Object... params) {
		File file = new File(getPath(), params[0]+".json");
		VarManager varManager = (VarManager) params[1];
		JsonArray data = varManager.serialiseVarps();
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(data.toString());
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public File getPath() {
		return SAVE_PATH;
	}
	
	public boolean exists(String name) {
		File file = new File(getPath(), name+".json");
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

}
