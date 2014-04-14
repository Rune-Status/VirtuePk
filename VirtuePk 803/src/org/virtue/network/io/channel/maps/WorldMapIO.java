package org.virtue.network.io.channel.maps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

import org.virtue.game.World;
import org.virtue.game.node.entity.npc.NPC;

/**
 * @author Taylor
 * @version 1.0
 */
public class WorldMapIO {

	/**
	 * Represents the map XTEA keys.
	 */
	private final MapXTEAData MAP_XTEAS = new MapXTEAData(new HashMap<Integer, int[]>());
	
	/**
	 * Represents the XTEA parser for the map data.
	 */
	private final MapXTEADataParser XTEA_PARSER = new MapXTEADataParser();
	
	/**
	 * Represents the {@link File} that will be referred to when loading XTEA data.
	 */
	private final File XTEA_FILE = new File(XTEA_PARSER.getPath());
	
	/**
	 * Represents the NPC map spawn parser.
	 */
	private final NPCSpawnParser NPC_SPAWN_PARSER = new NPCSpawnParser();
	
	/**
	 * Called to load the XTEA and node data for the Runescape map.
	 */
	public void load() {
		try {
			XTEA_PARSER.load(XTEA_FILE);
			List<NPC> npcs = NPC_SPAWN_PARSER.load(new BufferedReader(new FileReader(NPC_SPAWN_PARSER.getPath())));
			for (NPC npc : npcs)
				World.getWorld().getNpcs().add(npc);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Loaded " + World.getWorld().getNpcs().size() + " world NPCs");
	}

	/**
	 * @return The mapXteas
	 */
	public MapXTEAData getMapXTEA() {
		return MAP_XTEAS;
	}

	/**
	 * @return The XTEA_PARSER
	 */
	public MapXTEADataParser getXTEAParser() {
		return XTEA_PARSER;
	}

	/**
	 * @return The nPC_SPAWN_PARSER
	 */
	public NPCSpawnParser getNPCSpawnParser() {
		return NPC_SPAWN_PARSER;
	}
	
}
