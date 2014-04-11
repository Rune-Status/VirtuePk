package org.virtue.network.io.channel.maps;

import java.util.Map;

/**
 * @author Taylor
 * @version 1.0
 */
public class MapXTEAData {

	/**
	 * Represents the data for the runescape map. The first type argument
	 * representing the region key, the second representing the data
	 * corresponding to said integer key.
	 */
	private final Map<Integer, int[]> data;
	
	/**
	 * Constructs a new {@code XTEAFormat.java}.
	 * @param data The XTEA data for the Runescape map.
	 */
	public MapXTEAData(Map<Integer, int[]> data) {
		this.data = data;
	}

	/**
	 * @return The data
	 */
	public Map<Integer, int[]> getData() {
		return data;
	}
}
