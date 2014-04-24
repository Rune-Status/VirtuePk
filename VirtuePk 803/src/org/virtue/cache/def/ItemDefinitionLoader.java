package org.virtue.cache.def;

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
public class ItemDefinitionLoader {

	private static ItemDefinition[] itemDefinitions;

	public static void load (Cache cache) throws IOException {
		int count = 0;

		Container tableContainer = Container.decode(cache.getStore().read(255, CacheIndex.ITEM_DEFINITIONS));
		ReferenceTable table = ReferenceTable.decode(tableContainer.getData());

		int files = table.capacity();
		itemDefinitions = new ItemDefinition[files * 256];
		
		for (int file = 0; file < files; file++) {
			ReferenceTable.Entry entry = table.getEntry(file);
			if (entry == null)
				continue;

			Archive archive = Archive.decode(cache.read(CacheIndex.ITEM_DEFINITIONS, file).getData(), entry.size());
			int nonSparseMember = 0;
			for (int member = 0; member < entry.capacity(); member++) {
				ReferenceTable.ChildEntry childEntry = entry.getEntry(member);
				if (childEntry == null) {
					continue;
				}
				int id = file * 256 + member;
				ItemDefinition definition = new ItemDefinition(id, archive.getEntry(nonSparseMember++).array());
				itemDefinitions[id] = definition;
				count++;
			}
		}

		System.out.println("Loaded " + count + " item definitions.");
	}
	
	public static int count() {
		return itemDefinitions.length;
	}

	public static ItemDefinition forId(int id) {
		try {
			if (itemDefinitions == null) {
				load(CacheLoader.getCache());
			}
			if (id < 0 || id > itemDefinitions.length) {
				return null;//Item does not exist
			}
			/*ItemDefinition itemDef =itemDefinitions[id];
			if (itemDef == null) {
				byte[] data = CacheLoader.getCache().read(CacheIndex.ITEM_DEFINITIONS, getArchiveId(id), getFileId(id)).array();				
				itemDefinitions[id] = itemDef = new ItemDefinition(id, data);
			}*/
			return itemDefinitions[id];
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static ItemDefinition forName(String name) throws IOException {
		if (itemDefinitions == null) {
			load(CacheLoader.getCache());
		}
		for (ItemDefinition itemDef : itemDefinitions) {
			if (itemDef == null) {
				continue;
			}
			String itemName = itemDef.getName().toLowerCase();
			if (name.equalsIgnoreCase(itemName)) {
				return itemDef;
			}
		}
		return null;
	}

	public static void dumpItems() throws IllegalArgumentException, IllegalAccessException, IOException {
		load(CacheLoader.getCache());
		for (ItemDefinition def : itemDefinitions) {
			//ItemDefinition def = forId(i);
			if (def == null) {
				continue;
			}
		  // if (!def.getName().contains("Drygore"))
		   // continue;
	
		   System.out.println("===== Name: " + def.getName() + " =====");
		   def.printFields();
		   //System.out.println("=======================================\n");
		}
	}

}
