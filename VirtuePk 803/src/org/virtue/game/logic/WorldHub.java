package org.virtue.game.logic;

import org.virtue.game.logic.node.entity.npc.NPC;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.region.RegionManager;
import org.virtue.utility.EntityList;

/**
 * @author Taylor
 * @date Jan 13, 2014
 */
public class WorldHub {

	/**
	 * Represents a {@link EntityContainer} inhabited by players in this
	 * {@link World}.
	 */
	protected final EntityList<Player> PLAYERS = new EntityList<>(2048);

	/**
	 * Represents a {@link EntityContainer} inhabited by NPCS in this
	 * {@link World}.
	 */
	protected final EntityList<NPC> NPCS = new EntityList<>(25000);
	
	/**
	 * Represents the region manager.
	 */
	protected final RegionManager REGION_MANAGER = new RegionManager();

	/**
	 * Represents the ID.
	 */
	private int id;
	
	/**
	 * Represents the activity.
	 */
	private String activity;
	
	/**
	 * Represents the server.
	 */
	private ServerLocation server;
	
	/**
	 * Represents the flag.
	 */
	private int flag;
	
	/**
	 * Represents the region.
	 */
	private String region;
	
	/**
	 * Represents the country.
	 */
	private Country country;
	
	/**
	 * Represents the IP.
	 */
	private String ip;
	
	/**
	 * Represents the player count.
	 */
	private int playerCount;
	
	/**
	 * Represents the members.
	 */
	private boolean members;
	
	/**
	 * Represents if this worldframe should appear online.
	 */
	protected boolean online = true;
	
	/**
	 * Constructs a new {@code World.java}.
	 * @param id The id.
	 * @param activity The activity.
	 * @param server The server.
	 * @param flag The flag.
	 * @param region The region.
	 * @param country The country.
	 * @param ip The IP.
	 * @param location The location.
	 * @param playerCount The player count.
	 * @param members Members or not.
	 * @param online Online of offline.
	 */
	public WorldHub(int id, String activity, ServerLocation server, int flag, String region, Country country, String ip, int playerCount, boolean members, boolean online) {
		this.id = id;
		this.activity = activity;
		this.server = server;
		this.flag = flag;
		this.region = region;
		this.country = country;
		this.ip = ip;
		this.playerCount = playerCount;
		this.members = members;
		this.online = online;
	}
	
	/**
	 * Loads the world.
	 */
	public void load(){}
	
	
	/**
	 * Returns a player corresponding to the given name.
	 * @param name The name of the player to get.
	 * @return The player.
	 */
	public Player getPlayerByName(String name) {
		for (Player p : PLAYERS) {
			if (p == null) {
				continue;
			}
			if (p.getAccount().getUsername().getAccountName().equals(name)) {
				return p;
			} else {
				continue;
			}
		}
		return Lobby.getPlayer(name);
	}

	/**
	 * Adds a {@link Player} to this {@link World}.
	 * @param player The player to add.
	 */
	public synchronized void addPlayer(Player player) {
		PLAYERS.add(player);
	}

	/**
	 * Adds a {@link Player} to this {@link World}.
	 * @param npc The player to add.
	 */
	public synchronized void addNPC(NPC npc) {
		NPCS.add(npc);
	}

	/**
	 * Removes a {@link Player} from this {@link World}.
	 * @param player The player to remove.
	 */
	public synchronized void removePlayer(Player player) {
		PLAYERS.remove(player);
		//removePlayer(player.getAccount().getUsername().getAccountName());
	}
	
	/**
	 * Checks if this container contains a specified player.
	 * @param username The username.
	 * @return True if so; false otherwise.
	 */
	public boolean contains(String username) {
		for (Player player : PLAYERS) {
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
	public Player getPlayer(String username) {
		for (Player player : PLAYERS) {
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
	public void removePlayer(String username) {
		for (Player player : PLAYERS) {
			if (player.getAccount().getUsername().getAccountName().equalsIgnoreCase(username)) {
				PLAYERS.remove(player);
			}
		}
	}

	/**
	 * Returns a player.
	 * @param index The player index.
	 * @return The player.
	 */
	public Player getPlayer(int index) {
		return (Player) PLAYERS.get(index);
	}

	/**
	 * @return The players.
	 */
	public EntityList<Player> getPlayers() {
		return PLAYERS;
	}

	/**
	 * @return The npcs.
	 */
	public EntityList<NPC> getNpcs() {
		return NPCS;
	}

	/**
	 * @return The online
	 */
	public boolean isOnline() {
		return online;
	}

	/**
	 * @param online The online to set
	 */
	public void setOnline(boolean online) {
		this.online = online;
	}

	/**
	 * @return the rEGION_MANAGER
	 */
	public RegionManager getRegionManager() {
		return REGION_MANAGER;
	}
	
	/**
	 * @return the id
	 */
	public int getNodeID() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the activity
	 */
	public String getActivity() {
		return activity;
	}

	/**
	 * @param activity the activity to set
	 */
	public void setActivity(String activity) {
		this.activity = activity;
	}

	/**
	 * @return the server
	 */
	public ServerLocation getServer() {
		return server;
	}

	/**
	 * @param server the server to set
	 */
	public void setServer(ServerLocation server) {
		this.server = server;
	}

	/**
	 * @return the flag
	 */
	public int getFlag() {
		return flag;
	}

	/**
	 * @param flag the flag to set
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * @return the country
	 */
	public Country getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(Country country) {
		this.country = country;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the playerCount
	 */
	public int getPlayerCount() {
		return playerCount;
	}

	/**
	 * @param playerCount the playerCount to set
	 */
	public void setPlayerCount(int playerCount) {
		this.playerCount = playerCount;
	}

	/**
	 * @return the members
	 */
	public boolean isMembers() {
		return members;
	}

	/**
	 * @param members the members to set
	 */
	public void setMembers(boolean members) {
		this.members = members;
	}
}
