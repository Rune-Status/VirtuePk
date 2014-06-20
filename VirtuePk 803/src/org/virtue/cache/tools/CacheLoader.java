package org.virtue.cache.tools;

import java.io.File;
import java.io.IOException;

import org.virtue.cache.Cache;
import org.virtue.cache.FileStore;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 17, 2014
 */
public class CacheLoader {	

	private static Cache CACHE;

	public static void init() throws IOException {
		File cacheFile = new File(System.getProperty("user.home") + "/Desktop/cache/");
		if (!new File(cacheFile, "main_file_cache.dat2").exists()) {
			cacheFile = new File("data/cache/");
		}
		CACHE = new Cache(FileStore.open(cacheFile));
	}
	
	public static Cache getCache () throws IOException {
		if (CACHE == null) {
			init();
		}
		return CACHE;
	}

}
