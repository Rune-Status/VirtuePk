package org.virtue.network.io.channel.maps;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import org.virtue.game.logic.World;
import org.virtue.game.logic.node.entity.npc.NPC;

/**
 * @author Taylor
 * @version 1.0
 */
public class WorldMapIO {

	
	/**
	 * Represents the NPC map spawn parser.
	 */
	private final NPCSpawnParser NPC_SPAWN_PARSER = new NPCSpawnParser();
	
	/**
	 * Called to load the XTEA and node data for the Runescape map.
	 */
	public void load() {
		try {
			List<NPC> npcs = NPC_SPAWN_PARSER.load(new BufferedReader(new FileReader(NPC_SPAWN_PARSER.getPath())));
			for (NPC npc : npcs)
				World.getWorld().getNpcs().add(npc);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Loaded " + World.getWorld().getNpcs().size() + " world NPCs!");
	}

	/**
	 * @return The nPC_SPAWN_PARSER
	 */
	public NPCSpawnParser getNPCSpawnParser() {
		return NPC_SPAWN_PARSER;
	}
	
}
