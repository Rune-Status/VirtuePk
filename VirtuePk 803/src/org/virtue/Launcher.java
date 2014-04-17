package org.virtue;

import java.io.File;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.virtue.cache.Cache;
import org.virtue.cache.ChecksumTable;
import org.virtue.cache.Container;
import org.virtue.cache.FileStore;
import org.virtue.cache.def.AnimationDefinition;
import org.virtue.game.GameEngine;
import org.virtue.game.core.threads.MainThreadFactory;
import org.virtue.game.logic.Lobby;
import org.virtue.network.RS2Network;
import org.virtue.network.io.IOHub;
import org.virtue.network.loginserver.DataServer;
import org.virtue.utility.ConsoleLogger;
import org.virtue.utility.TimeUtil;

/**
 * @author Taylor Moon
 * @since Jan 22, 2014
 */
public class Launcher {
	
	/**
	 * Represents the secure random.
	 */
	private static final Random RANDOM = new SecureRandom();
	
	/**
	 * Represents the main executor that executes the game engine.
	 */
	private static final ExecutorService ENGINE_LOADER = Executors.newCachedThreadPool(new MainThreadFactory());
	
	/**
	 * Represents the network foundation of the server.
	 */
	private static final RS2Network NETWORK = new RS2Network();
	
	/**
	 * Represents the game engine.
	 */
	private static final GameEngine ENGINE = new GameEngine();
	
	/**
	 * Represents the game {@link Cache}.
	 */
	private static Cache CACHE;
	
	/**
	 * Represents the main method.
	 * @param args The arguments casted on runtime.
	 */
	public static void main(String[] args) {
		try {
			System.setOut(new ConsoleLogger(System.out));
			System.setErr(new ConsoleLogger(System.err));
			long currentTime = TimeUtil.currentTimeMillis();
			System.out.println("Welcome to " + Constants.NAME + ".");
			loadEngine();
			loadCache();
			if (Constants.LOGIN_SERVER) {
				DataServer.load();
			}
			Lobby.load();
			IOHub.load();
			NETWORK.load();
			System.out.println("VirtuePK took " + (TimeUtil.currentTimeMillis() - currentTime) + " milli seconds to launch.");
		} catch (Exception e) {
			ENGINE.handleException(e);
		}
	}
	
	/**
	 * Loads the Virtue game engine.
	 */
	private static void loadEngine() {
		ENGINE_LOADER.execute(ENGINE);
		ENGINE.getTickManager().register(ENGINE.getLogicProcessor());
	}

	/**
	 * Loads the Xerxes cache.
	 * @throws Exception If an exception occurs.
	 */
	private static void loadCache() throws Exception {
		System.out.println("Loading cache.");
		File cacheFile = new File(System.getProperty("user.home") + "/Desktop/cache/");
		if (!new File(cacheFile, "main_file_cache.dat2").exists()) {
			cacheFile = new File("data/cache/");
		}
		CACHE = new Cache(FileStore.open(cacheFile));
		Container container = new Container(Container.COMPRESSION_NONE, CACHE.createChecksumTable().encode(true, ChecksumTable.ON_DEMAND_MODULUS, ChecksumTable.ON_DEMAND_EXPONENT));
		CACHE.setChecksumtable(container.encode());
/*		int i = 0;
		while (i < 14484) {
			ItemDefinition item = ItemDefinition.forId(i);
	        System.out.println("Item: name="+item.getName()+", equiptID="+item.equipID+", equiptSlot="+item.equipSlotID+", maleModel="+item.maleEquip1);
			i++;
		}*/
        
	}

	/**
	 * @return the engine
	 */
	public static GameEngine getEngine() {
		return ENGINE;
	}

	/**
	 * @return the network
	 */
	public static RS2Network getNetwork() {
		return NETWORK;
	}

	/**
	 * @return the random
	 */
	public static Random getRandom() {
		return RANDOM;
	}

	/**
	 * @return the cACHE
	 */
	public static Cache getCache() {
		return CACHE;
	}
}
