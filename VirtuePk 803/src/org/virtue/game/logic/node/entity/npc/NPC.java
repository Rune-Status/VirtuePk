package org.virtue.game.logic.node.entity.npc;

import org.virtue.cache.def.NPCDefinition;
import org.virtue.cache.def.NPCDefinitionLoader;
import org.virtue.game.logic.node.entity.Entity;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.impl.Bank;
import org.virtue.game.logic.region.Tile;

/**
 * @author Taylor
 * @date Jan 13, 2014
 */
public class NPC extends Entity {
	
	/**
	 * Represents the tile.
	 */
	private Tile tile;
	
	/**
	 * Represents the id.
	 */
	private int id;

	private boolean exists = true;
	
	private NPCDefinition definition;

	/**
	 * Constructs a new {@code NPC.java}.
	 * @param id The id.
	 * @param tile The tile.
	 */
	public NPC(int id, Tile tile) {
		super();
		this.id = id;
		this.tile = tile;
		this.definition = NPCDefinitionLoader.forId(id);
		if (this.definition == null) {
			throw new RuntimeException("Definition for NPC "+id+" was not found.");
		}
	}
	
	@Override
	public void start() {
		
	}

	@Override
	public void destroy() {
		
	}

	@Override
	public void onCycle() {
		getUpdateArchive().getMovement().process();
	}
	
	public NPCDefinition getDefinition () {
		return definition;
	}

	/**
	 * @return the tile.
	 */
	public Tile getTile() {
		return tile;
	}
	
	/**
	 * @return the id.
	 */
	public int getId() {
		return id;
	}

	@Override
	public boolean exists() {
		return exists;
	}

	@Override
	public void refreshOnDemand() {
		getUpdateArchive().reset();//Refresh update flags
	}

	@Override
	public void update() {
	}

	@Override
	public int getSize() {
		return definition.getSize();
	}
	
	public boolean isInteractOption (NPCOption option) {
		return !option.equals(NPCOption.EXAMINE);
	}
	
	/**
	 * Handles the player interaction with this NPC
	 * @param player	The player interacting with the NPC
	 * @param option	The selected interaction option
	 */
	public void interact (Player player, NPCOption option) {
		if (Bank.isBankNpc(this) && option.equals(NPCOption.ONE)) {
			player.getInterfaces().openBank();
			return;
		}
		System.out.println("Clicked NPC: npcIndex="+getIndex()+", id="+id+", xCoord="+getTile().getX()+", yCoord="+getTile().getY()+", optionID="+option.getID());
	}
	
	public void handleDistanceOption (Player player, NPCOption option) {
		System.out.println("Clicked NPC: npcIndex="+getIndex()+", id="+id+", xCoord="+getTile().getX()+", yCoord="+getTile().getY()+", optionID="+option.getID());
	}
}
