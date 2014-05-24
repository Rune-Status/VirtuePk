package org.virtue.network.io.channel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.virtue.game.logic.node.entity.player.screen.InterfaceLayout;
import org.virtue.network.io.IOParser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class InterfaceLayoutParser implements IOParser<InterfaceLayout> {
	

	private File SAVE_PATH = new File("data/interface_layouts/");

	@Override
	public InterfaceLayout load(Object... params) throws FileNotFoundException {
		File file = new File(getPath(), params[0]+".json");
		JsonParser parser = new JsonParser();
		JsonElement parsed = parser.parse(new FileReader(file));
		InterfaceLayout layoutFile = new InterfaceLayout();
		layoutFile.deserialiseLayout((JsonArray) parsed);
		return layoutFile;
	}

	@Override
	public boolean save(Object... params) {
		File file = new File(getPath(), params[0]+".json");
		InterfaceLayout layout = (InterfaceLayout) params[1];
		JsonArray data = layout.serialiseLayout();

		try (FileWriter writer = new FileWriter(file)) {
			writer.write(data.toString());
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		/*try (DataOutputStream output = new DataOutputStream(new FileOutputStream(interfaceData))) {
			layout.serialise(output);
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}*/
		return true;
	}

	@Override
	public File getPath() {
		return SAVE_PATH;
	}

}
