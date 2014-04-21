package org.virtue.cache.def;

import java.io.IOException;

import org.virtue.cache.Archive;
import org.virtue.cache.Cache;
import org.virtue.cache.Container;
import org.virtue.cache.ReferenceTable;
import org.virtue.cache.tools.CacheLoader;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 19, 2014
 */
public class GraphicsDefinitionLoader {
	
	private static GraphicsDefinition[] gfxDefinitions;
	
	public static void load (Cache cache) throws IOException {
		int count = 0;

		Container tableContainer = Container.decode(cache.getStore().read(255, CacheIndex.GRAPHICS_DEFINITIONS));
		ReferenceTable table = ReferenceTable.decode(tableContainer.getData());

		int files = table.capacity();
		gfxDefinitions = new GraphicsDefinition[files * 256];
		for (int file = 0; file < files; file++) {
			ReferenceTable.Entry entry = table.getEntry(file);
			if (entry == null) {
				continue;
			}
			Archive archive = Archive.decode(cache.read(CacheIndex.GRAPHICS_DEFINITIONS, file).getData(), entry.size());
			int nonSparseMember = 0;
			for (int member = 0; member < entry.capacity(); member++) {
				ReferenceTable.ChildEntry childEntry = entry.getEntry(member);
				if (childEntry == null) {
					continue;
				}
				int id = file * 256 + member;
				GraphicsDefinition definition = new GraphicsDefinition(id, archive.getEntry(nonSparseMember++).array());
				gfxDefinitions[id] = definition;
				count++;
			}
		}
		System.out.println("Loaded " + count + " graphics definitions.");
	}
	
	public static int count() {
		return gfxDefinitions.length;
	}

	public static GraphicsDefinition forId(int id) {
		if (gfxDefinitions == null) {
			try {
				load(CacheLoader.getCache());
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		if (id < 0 || id > gfxDefinitions.length) {
			return null;//Graphics does not exist
		}
		return gfxDefinitions[id];
	}
	
	public static void dumpGfx () throws IOException, IllegalArgumentException, IllegalAccessException {
		load(CacheLoader.getCache());
		for (GraphicsDefinition def : gfxDefinitions) {
			if (def == null) {
				continue;
			}	
			  // if (!def.getName().contains("Kalphite"))
			   // continue;
			System.out.println("===== ID: " + def.getID() + " =====");
			def.printFields();
			//System.out.println("=======================================\n");
		}
	}

}
