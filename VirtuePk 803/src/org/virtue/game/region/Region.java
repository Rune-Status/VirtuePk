package org.virtue.game.region;

import org.virtue.engine.AttributeSet;
import org.virtue.game.World;
import org.virtue.game.node.entity.npc.NPC;
import org.virtue.game.node.entity.player.Player;
import org.virtue.utility.EntityList;

/**
 * @author Taylor Moon
 * @since Jan 25, 2014
 */
public class Region extends AttributeSet implements SubRegion {
	
	/**
	 * Represents the amount of players that can fit in a single region at once.
	 */
	public static final int PLAYER_CAP = 2048;
	
	/**
	 * Represents the amount of npcs that can fit in a single region at once.
	 */
	public static final int NPC_CAP = 10000;
	
	/**
	 * Represents the ID of this region
	 */
	private int id;
	
	/**
	 * Represents the players in this region.
	 */
	private EntityList<Player> players;
	
	/**
	 * Represents the NPCs in this region.
	 */
	private EntityList<NPC> npcs;
	
	/**
	 * Constructs a new {@code Region.java}.
	 * @param id The ID.
	 */
	public Region(int id) {
		this.id = id;
		players = new EntityList<Player>(PLAYER_CAP);
		npcs = new EntityList<NPC>(NPC_CAP);
	}
	
	/**
	 * Returns a player corresponding to the specified name.
	 * @param name The name of the player to get
	 * @return The player.
	 */
	public Player getPlayer(String name) {
		for (Player player : players) {
			if (player.getAccount().getUsername().getAccountName().equalsIgnoreCase(name)) {
				return player;
			}
		}
		return null;
	}

	/**
	 * @return the players
	 */
	public EntityList<Player> getPlayers() {
		return players;
	}

	/**
	 * @param players the players to set
	 */
	public void setPlayers(EntityList<Player> players) {
		this.players = players;
	}

	/**
	 * @return the npcs
	 */
	public EntityList<NPC> getNpcs() {
		return npcs;
	}

	/**
	 * @param npcs the npcs to set
	 */
	public void setNpcs(EntityList<NPC> npcs) {
		this.npcs = npcs;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public void preUpdate() {
		if (!World.getWorld().getRegionManager().containsRegion(id)) {
			World.getWorld().getRegionManager().addRegion(this);
		}
	}

	@Override
	public void update() {
		//render region update.
	}

	@Override
	public void postUpdate() {
		if (getFlag("concealed_update", false)) {
			World.getWorld().getRegionManager().removeRegion(this);
		}
	}

	@Override
	public void refresh() {
		flags.clear();
	}
}
