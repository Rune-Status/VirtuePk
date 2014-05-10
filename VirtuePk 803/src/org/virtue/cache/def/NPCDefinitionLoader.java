package org.virtue.cache.def;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.virtue.cache.Archive;
import org.virtue.cache.Cache;
import org.virtue.cache.Container;
import org.virtue.cache.ReferenceTable;
import org.virtue.cache.tools.CacheLoader;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 17, 2014
 */
public class NPCDefinitionLoader {

	private static NPCDefinition[] npcDefinitions;
	
	public static void load (Cache cache) throws IOException {
		int count = 0;

		Container tableContainer = Container.decode(cache.getStore().read(255, CacheIndex.NPC_DEFINITIONS));
		ReferenceTable table = ReferenceTable.decode(tableContainer.getData());

		int files = table.capacity();
		npcDefinitions = new NPCDefinition[files * 128];
		for (int file = 0; file < files; file++) {
			ReferenceTable.Entry entry = table.getEntry(file);
			if (entry == null) {
				continue;
			}
			Archive archive = Archive.decode(cache.read(CacheIndex.NPC_DEFINITIONS, file).getData(), entry.size());
			int nonSparseMember = 0;
			for (int member = 0; member < entry.capacity(); member++) {
				ReferenceTable.ChildEntry childEntry = entry.getEntry(member);
				if (childEntry == null) {
					continue;
				}
				int id = file * 128 + member;
				NPCDefinition definition = new NPCDefinition(id, archive.getEntry(nonSparseMember++).array());
				npcDefinitions[id] = definition;
				count++;
			}
		}
		System.out.println("Loaded " + count + " NPC definitions.");
	}
	
	public static int count() {
		return npcDefinitions.length;
	}

	public static NPCDefinition forId(int id) {
		if (npcDefinitions == null) {
			try {
				load(CacheLoader.getCache());
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		if (id < 0 || id > npcDefinitions.length) {
			return null;//NPC does not exist
		}
		/*NPCDefinition npcDef = npcDefinitions[id];
		if (npcDef == null) {
			byte[] data = CacheLoader.getCache().read(CacheIndex.NPC_DEFINITIONS, getArchiveId(id), getFileId(id)).array();
			if (data == null) {
				return null;
			}
			npcDefinitions[id] = npcDef = new NPCDefinition(id, data);
		}*/
		return npcDefinitions[id];
	}

	public static void dumpNpc() throws IllegalArgumentException, IllegalAccessException, IOException {
		load(CacheLoader.getCache());
		File directory = new File("./dumps/npcs/");
		directory.mkdirs();
		File file = new File(directory, "key.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		for (NPCDefinition def : npcDefinitions) {
			if (def == null) {
				continue;
			}	
			  // if (!def.getName().contains("Kalphite"))
			   // continue;
			writer.write(def.getID() + "->" + def.getName());
			writer.newLine();
			writer.flush();
			//System.out.println("===== Name: " + def.getName() + " =====");
			def.printFields();
			//System.out.println("=======================================\n");
		}
		writer.close();
	}
}
