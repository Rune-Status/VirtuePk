package org.virtue.network.io;

import org.virtue.network.io.channel.PlayerParser;
import org.virtue.network.io.channel.maps.WorldMapIO;

/**
 * @author Taylor
 * @version 1.0
 */
public class IOHub {
	
	/**
	 * Represents the IO stream hub for the Runescape world map.
	 */
	private static final WorldMapIO WORLD_MAP_IO = new WorldMapIO();

	/**
	 * Represenst the player IO stream handler.
	 */
	private static final PlayerParser PLAYER_IO = new PlayerParser();
	
	/**
	 * Loads any parsers that need to be ran on startup.
	 */
	public static void load() {
		WORLD_MAP_IO.load();
	}
	
	/**
	 * @return The worldMapIo
	 */
	public static WorldMapIO getWorldMapIo() {
		return WORLD_MAP_IO;
	}

	/**
	 * @return The playerIo
	 */
	public static PlayerParser getPlayerIo() {
		return PLAYER_IO;
	}
}
