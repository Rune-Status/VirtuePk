package org.virtue.network.io;

import org.virtue.network.io.channel.AccountParser;
import org.virtue.network.io.channel.FriendsParser;
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
	 * Represents the player IO stream handler.
	 */
	private static final AccountParser ACCOUNT_IO = new AccountParser();
	
	/**
	 * Represents the friends/ignores IO stream handler
	 */
	private static final FriendsParser FRIEND_IO = new FriendsParser();
	
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
	public static AccountParser getAccountIo() {
		return ACCOUNT_IO;
	}
	
	/**
	 * @return The friend IO manager
	 */
	public static FriendsParser getFriendsIO () {
		return FRIEND_IO;
	}
}
