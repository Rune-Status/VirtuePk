package org.virtue.network.io.channel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.virtue.game.logic.social.clans.ClanSettings;
import org.virtue.network.io.IOParser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ClanSettingsParser implements IOParser<ClanSettings> {

	private File SAVE_PATH = new File("data/clans/");
	
	private static final int LATEST_VERSION = 6;
	
	@Override
	public ClanSettings load(Object... params) throws FileNotFoundException {
		File clanFile = new File(getPath(), params[0]+".json");
		
		JsonParser parser = new JsonParser();
		JsonElement parsed = parser.parse(new FileReader(clanFile));
		
		JsonObject clanData = parsed.getAsJsonObject();
		int version = clanData.get("version").getAsInt();
		long clanHash = clanData.get("clanHash").getAsInt();
		String clanName = clanData.get("name").getAsString();
		int updateNum = clanData.get("updateNum").getAsInt();
		ClanSettings settings = new ClanSettings(clanHash, updateNum, clanName);
		settings.deserialise(clanData, version);
		return settings;
	}

	@Override
	public boolean save(Object... params) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public File getPath() {
		return SAVE_PATH;
	}

}
