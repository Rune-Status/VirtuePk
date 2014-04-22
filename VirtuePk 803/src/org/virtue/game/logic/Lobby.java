package org.virtue.game.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.virtue.game.logic.node.entity.player.Player;

/**
 * @author Taylor
 * @date Jan 16, 2014
 */
public class Lobby {

	/**
	 * Represents the players.
	 */
	private static CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<>();
	
	/**
	 * Represents the worlds in this lobby.
	 */
	private static List<WorldHub> worlds = new ArrayList<>();
	
	/**
	 * Loads the worlds in the lobby.
	 */
	public static void load() {
		worlds.add(World.getWorld());
	}
	
	/**
	 * Recieves a ls update.
	 * @param worlds The worlds.
	 */
	public static void recieveLoginServerUpdate(List<WorldHub> worlds) {
		Lobby.worlds = worlds;
		for (WorldHub world : worlds) {
			world.load();
		}
	}

	/**
	 * Adds a {@link Player} to this {@link World}.
	 * @param player The player to add.
	 */
	public static void addPlayer(Player player) {
		players.add(player);
	}

	/**
	 * Removes a {@link Player} from this {@link World}.
	 * @param player The player to remove.
	 */
	public static void removePlayer(Player player) {
		removePlayer(player.getAccount().getUsername().getAccountName());
	}
	
	/**
	 * Checks if this container contains a specified player.
	 * @param username The username.
	 * @return True if so; false otherwise.
	 */
	public static boolean contains(String username) {
		for (Player player : players) {
			if (player == null) {
				continue;
			}
			if (player.getAccount().getUsername().getAccountName().equalsIgnoreCase(username)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns a player.
	 * @param username The username.
	 * @return The player.
	 */
	public static Player getPlayer(String username) {
		for (Player player : players) {
			if (player == null) {
				continue;
			}
			if (player.getAccount().getUsername().getAccountName().equalsIgnoreCase(username)) {
				return player;
			}
		}
		return null;
	}
	
	/**
	 * Removes a player.
	 * @param username The username.
	 */
	public static void removePlayer(String username) {
		for (Player player : players) {
			if (player.getAccount().getUsername().getAccountName().equalsIgnoreCase(username)) {
				removePlayer(player);
			}
		}
	}

	/**
	 * Returns a player.
	 * @param index The player index.
	 * @return The player.
	 */
	public static Player getPlayer(int index) {
		return players.get(index);
	}

	/**
	 * @return The players.
	 */
	public static List<Player> getPlayers() {
		return players;
	}

	/**
	 * @return the worlds
	 */
	public static List<WorldHub> getWorlds() {
		return worlds;
	}

	/**
	 * @param worlds the worlds to set
	 */
	public static void setWorlds(List<WorldHub> worlds) {
		Lobby.worlds = worlds;
	}
}
