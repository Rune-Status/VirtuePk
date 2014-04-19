package org.virtue.cache.def;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.virtue.cache.Archive;
import org.virtue.cache.Cache;
import org.virtue.cache.Container;
import org.virtue.cache.ReferenceTable;
import org.virtue.cache.tools.CacheLoader;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 17, 2014
 */
public class AnimationDefinitionLoader {
	
	private static AnimationDefinition[] animDefinitions;
	
	public static void load (Cache cache) throws IOException {
		int count = 0;

		Container tableContainer = Container.decode(cache.getStore().read(255, CacheIndex.ANIMATION_DEFINITIONS));
		ReferenceTable table = ReferenceTable.decode(tableContainer.getData());

		int files = table.capacity();
		animDefinitions = new AnimationDefinition[files * 128];
		for (int file = 0; file < files; file++) {
			ReferenceTable.Entry entry = table.getEntry(file);
			if (entry == null) {
				continue;
			}
			Archive archive = Archive.decode(cache.read(CacheIndex.ANIMATION_DEFINITIONS, file).getData(), entry.size());
			int nonSparseMember = 0;
			for (int member = 0; member < entry.capacity(); member++) {
				ReferenceTable.ChildEntry childEntry = entry.getEntry(member);
				if (childEntry == null) {
					continue;
				}
				int id = file * 128 + member;
				AnimationDefinition definition = new AnimationDefinition(id, archive.getEntry(nonSparseMember++).array());
				animDefinitions[id] = definition;
				count++;
			}
		}
		System.out.println("Loaded " + count + " animation definitions.");
	}
	
	public static int count() {
		return animDefinitions.length;
	}
	
	public static AnimationDefinition forId(int id) {
		if (animDefinitions == null) {
			try {
				load(CacheLoader.getCache());
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		if (id < 0 || id > animDefinitions.length) {
			return null;//Animation does not exist
		}
		return animDefinitions[id];
	}

	public static void dumpAnimations() throws IllegalArgumentException, IllegalAccessException, IOException {
		load(CacheLoader.getCache());
		for (AnimationDefinition def : animDefinitions) {
			if (def == null) {
				continue;
			}	
			  // if (!def.getName().contains("Kalphite"))
			   // continue;
			System.out.println("===== Animation: " + def.getID() + " =====");
			def.printFields();
			//System.out.println("=======================================\n");
		}
	}

}
