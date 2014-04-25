package org.virtue.game.logic.region;

import java.util.ArrayList;

import org.virtue.game.core.AttributeSet;
import org.virtue.game.logic.World;
import org.virtue.game.logic.item.GroundItem;
import org.virtue.game.logic.node.entity.npc.NPC;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.network.protocol.messages.GroundItemMessage.GroundItemType;
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
	 * Represents the {@link List} of items.
	 */
	private ArrayList<GroundItem> items = new ArrayList<>();
	
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
	
	public void addPlayer (Player player) {
		players.add(player);
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
	 * Places an item on the ground
	 * @param item	The {@link GroundItem} to add
	 */
	public void addItem (GroundItem item) {
		if (!items.contains(item)) {
			items.add(item);
			for (Player p : World.getWorld().getPlayers()) {
				if (p.getViewport().getRegions().contains(id)) {
					p.getPacketDispatcher().dispatchGroundItem(item, GroundItemType.CREATE);
				}
			}
		}
	}
	
	/**
	 * Removes an item which was previously on the ground
	 * @param item	The item to remove
	 */
	public void removeItem (GroundItem item) {
		if (!items.contains(item)) {
			return;
		}
		items.remove(item);
		for (Player p : World.getWorld().getPlayers()) {
			if (p.getViewport().getRegions().contains(id)) {
				p.getPacketDispatcher().dispatchGroundItem(item, GroundItemType.DESTROY);
			}
		}
	}
	
	public boolean containsItem (GroundItem item) {
		return items.contains(item);
	}
	
	public GroundItem getItem (int id) {
		for (GroundItem item : items) {
			if (item.getId() == id) {
				return item;
			}
		}
		return null;
	}
	
	/**
	 * Updates the region nodes.
	 * @param player The player updating.
	 */
	public void updateGroundItems(Player player, GroundItemType type) {
		for (GroundItem item : items) {
			if (item == null) {
				continue;
			}
			player.getPacketDispatcher().dispatchGroundItem(item, type);
		}
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
