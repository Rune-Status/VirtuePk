package org.virtue.game.logic;

import org.virtue.Constants;
import org.virtue.game.logic.node.entity.npc.NPC;
import org.virtue.game.logic.node.entity.region.Tile;


public class World extends WorldHub {
	
	/**
	 * Constructs a new {@code World.java}.
	 * @param id The id.
	 * @param activity The activity.
	 * @param server The server.
	 * @param country The country.
	 * @param ip The ip.
	 * @param location The location.
	 * @param playerCount The players.
	 * @param members Members or not.
	 * @param online Online of offline.
	 */
	public World(int id, String activity, ServerLocation server, Country country, String ip, int playerCount, boolean members, boolean online) {
		super(id, activity, server, country, ip, playerCount, members, online);
	}

	/**
	 * Represents the world singleton.
	 */
	private static final World WORLD = new World(Constants.WORLD_ID, "VirtuePK", ServerLocation.US_EAST_1, Country.USA,  "127.0.0.1", 0, true, true);

	@Override
	public void load() {
		
	}
	
	@Override
	public int getPlayerCount() {
		return getPlayers().size();
	}

	/**
	 * @return the world
	 */
	public static World getWorld() {
		return WORLD;
	}
}
