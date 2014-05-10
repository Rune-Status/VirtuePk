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
public class ObjectDefinitionLoader {

	private static ObjectDefinition[] objectDefinitions;
	
	public static void load (Cache cache) throws IOException {
		int count = 0;

		Container tableContainer = Container.decode(cache.getStore().read(255, CacheIndex.OBJECT_DEFINITIONS));
		ReferenceTable table = ReferenceTable.decode(tableContainer.getData());

		int files = table.capacity();
		objectDefinitions = new ObjectDefinition[files * 256];
		for (int file = 0; file < files; file++) {
			ReferenceTable.Entry entry = table.getEntry(file);
			if (entry == null)
				continue;

			Archive archive = Archive.decode(cache.read(CacheIndex.OBJECT_DEFINITIONS, file).getData(), entry.size());
			int nonSparseMember = 0;
			for (int member = 0; member < entry.capacity(); member++) {
				ReferenceTable.ChildEntry childEntry = entry.getEntry(member);
				if (childEntry == null) {
					continue;
				}
				int id = file * 256 + member;
				ObjectDefinition definition = new ObjectDefinition(id, archive.getEntry(nonSparseMember++).array());
				objectDefinitions[id] = definition;
				count++;
			}
		}

		System.out.println("Loaded " + count + " object definitions.");
	}
	
	public static int count() {
		return objectDefinitions.length;
	}

	public static ObjectDefinition forId(int id) {
		if (ObjectDefinitionLoader.objectDefinitions == null) {
			try {
				load(CacheLoader.getCache());
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		if (id < 0 || id > objectDefinitions.length) {
			return null;//Object does not exist
		}
		/*ObjectDefinition objectDef = objectDefinitions[id];
		if (objectDef == null) {
			byte[] data = CacheLoader.getCache().read(CacheIndex.OBJECT_DEFINITIONS, getArchiveId(id), getFileId(id)).array();
			if (data == null) {
				return null;
			}
			ObjectDefinitionLoader.objectDefinitions[id] = objectDef = new ObjectDefinition(id, data);
		}*/
		return objectDefinitions[id];
	}	
	
	public static void dumpObjects () throws IOException, IllegalArgumentException, IllegalAccessException {
		load(CacheLoader.getCache());
		File directory = new File("./dumps/objects/");
		directory.mkdirs();
		File file = new File(directory, "key.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		for (ObjectDefinition def : objectDefinitions) {
			if (def == null) {
				continue;
			}	
			// if (!def.getName().contains("Kalphite"))
			// continue;
			writer.write(def.getID() + "->" + def.getName());
			writer.newLine();
			writer.flush();
			//System.out.println("===== ID: " + def.getID() + " =====");
			def.printFields();
			//System.out.println("=======================================\n");
		}
		writer.close();
	}
}
