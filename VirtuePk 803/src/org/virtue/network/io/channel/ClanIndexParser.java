package org.virtue.network.io.channel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.virtue.network.io.IOParser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ClanIndexParser implements IOParser<Map<String, Long>> {

	private File SAVE_PATH = new File("data/clans/index.json");
	
	@Override
	public Map<String, Long> load(Object... params) throws FileNotFoundException {
		JsonParser parser = new JsonParser();
		JsonElement parsed = parser.parse(new FileReader(getPath()));
		Map<String, Long> index = new HashMap<String, Long>();
		if (parsed.isJsonArray()) {
			JsonArray data = parsed.getAsJsonArray();
			for (JsonElement element : data) {
				JsonObject keyValuePair = element.getAsJsonObject();
				index.put(keyValuePair.get("name").getAsString(), keyValuePair.get("hash").getAsLong());
			}
		}
		return index;
	}

	@Override
	public boolean save(Object... params) {
		Map<?, ?> index = (Map<?, ?>) params[0];
		JsonArray data = new JsonArray();
		for (Entry<?, ?> value : index.entrySet()) {
			JsonObject obj = new JsonObject();
			obj.addProperty("key", (String) value.getKey());
			obj.addProperty("value", (Long) value.getValue());
			data.add(obj);
		}
		try (FileWriter writer = new FileWriter(getPath())) {
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

}
