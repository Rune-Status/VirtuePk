package org.virtue.game.logic.node.entity.player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.virtue.game.logic.World;
import org.virtue.game.logic.node.entity.npc.NPC;
import org.virtue.game.logic.node.entity.region.LandscapeRepository;
import org.virtue.game.logic.node.entity.region.RegionUpdateEvent;
import org.virtue.game.logic.node.entity.region.Tile;
import org.virtue.network.protocol.packet.RS3PacketBuilder;

/**
 * @author Taylor Moon
 * @since Jan 25, 2014
 */
public class Viewport {
	
	/**
	 * The map region ids.
	 */
	private final List<Integer> REGIONS = new ArrayList<>();
	
	/**
	 * 
	 */
	private final LinkedList<NPC> localNpcs = new LinkedList<NPC>();
	
	/**
	 * Represents the player.
	 */
	private Player player;
	
	/**
	 * Represents the last loaded tile.
	 */
	private Tile lastLoadedTile;
	
	/**
	 * Represents if GPI should be updated.
	 */
	private boolean sendGPI = true;
	
	/**
	 * Represents the hash data.
	 */
	private byte[] slotFlags;

	/**
	 * Represenst the local players.
	 */
	private Player[] localPlayers;
	
	/**
	 * Represents the local indexes.
	 */
	private int[] localPlayersIndexes;
	
	/**
	 * Represents the local player indexes count.
	 */
	private int localPlayersIndexesCount;

	/**
	 * Represents the global indexes.
	 */
	private int[] outPlayersIndexes;
	
	/**
	 * Represents the global index count.
	 */
	private int outPlayersIndexesCount;

	/**
	 * Represents the region data.
	 */
	private int[] regionHashes;

	/**
	 * Represents the cached appearance hashes.
	 */
	private byte[][] cachedAppearencesHashes;
	
	/**
	 * Represents the render data length;
	 */
	private int totalRenderDataSentLength;
	
	/**
	 * Represents the local added players.
	 */
	private int localAddedPlayers;
	
	/**
	 * Constructs a new {@code Viewport.java}
	 * @param player The player reference
	 */
	public Viewport(Player player) {
		this.player = player;
		setSlotFlags(new byte[2048]);
		setLocalPlayers(new Player[2048]);
		setLocalPlayersIndexes(new int[2048]);
		setOutPlayersIndexes(new int[2048]);
		setRegionHashes(new int[2048]);
		setCachedAppearencesHashes(new byte[2048][]);
	}
	
	/**
	 * Loads the global player buffer on login.
	 * @param buffer The buffer to write to.
	 */
	public void loadGlobalPlayers(RS3PacketBuilder buffer) {
		buffer.syncBits();
		buffer.putBits(30, player.getTile().getTileHash());
		getLocalPlayers()[player.getIndex()] = player;
		getLocalPlayersIndexes()[localPlayersIndexesCount++] = player.getIndex();
		System.out.println("Setting local player at index: "+player.getIndex());
		for (int playerIndex = 1; playerIndex < 2048; playerIndex++) {
			if (playerIndex == player.getIndex())
				continue;
			Player player = World.getWorld().getPlayer(playerIndex);
			buffer.putBits(18, getRegionHashes()[playerIndex] = (player == null ? 0 : player.getTile().getRegionHash()));
			getOutPlayersIndexes()[outPlayersIndexesCount++] = playerIndex;
		}
		buffer.unSyncBits();
	}

	/**
	 * Loads the {@code Player's} viewport map scene
	 */
	public void loadViewport() {
		REGIONS.clear();
		int chunkX = getPlayer().getTile().getChunkX();
		int chunkY = getPlayer().getTile().getChunkY();
		int mapHash = LandscapeRepository.REGION_SIZES[0] >> 4;
		int minRegionX = (chunkX - mapHash) / 8;
		int minRegionY = (chunkY - mapHash) / 8;
		for (int xCalc = minRegionX < 0 ? 0 : minRegionX; xCalc <= ((chunkX + mapHash) / 8); xCalc++) {
			for (int yCalc = minRegionY < 0 ? 0 : minRegionY; yCalc <= ((chunkY + mapHash) / 8); yCalc++) {
				final int region = yCalc + (xCalc << 8);
				World.getWorld().getRegionManager().registerRegionUpdate(new RegionUpdateEvent(World.getWorld().getRegionManager().getRegionById(region), new Runnable() {

					@Override
					public void run() {
						World.getWorld().getRegionManager().getRegionById(region).refresh();
					}
				}));
				REGIONS.add(region);
			}
		}
		lastLoadedTile = new Tile(getPlayer().getTile());
	}

	/**
	 * Represents if a map update is required.
	 * @return True if so; false otherwise.
	 */
	public boolean needsMapUpdate() {
		int lastMapRegionX = lastLoadedTile.getChunkX();
		int lastMapRegionY = lastLoadedTile.getChunkY();
		int regionX = getPlayer().getTile().getChunkX();
		int regionY = getPlayer().getTile().getChunkY();
		int size = ((LandscapeRepository.REGION_SIZES[0] >> 3) / 2) - 1;
		return Math.abs(lastMapRegionX - regionX) >= size || Math.abs(lastMapRegionY - regionY) >= size;
	}

	/**
	 * @return The regionIds
	 */
	public List<Integer> getRegions() {
		return REGIONS;
	}

	/**
	 * @return The lastLoadedTile
	 */
	public Tile getLastLoadedTile() {
		return lastLoadedTile;
	}

	/**
	 * @param lastLoadedTile The lastLoadedTile to set
	 */
	public void setLastLoadedTile(Tile lastLoadedTile) {
		this.lastLoadedTile = lastLoadedTile;
	}

	/**
	 * @return the sendGPI
	 */
	public boolean isSendGPI() {
		return sendGPI;
	}

	/**
	 * @param sendGPI the sendGPI to set
	 */
	public void setSendGPI(boolean sendGPI) {
		this.sendGPI = sendGPI;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @return the localPlayers
	 */
	public Player[] getLocalPlayers() {
		return localPlayers;
	}
	
	/**
	 * @return	the local NPCs
	 */
	public LinkedList<NPC> getLocalNPCs() {
		return localNpcs;
	}

	/**
	 * @param localPlayers the localPlayers to set
	 */
	public void setLocalPlayers(Player[] localPlayers) {
		this.localPlayers = localPlayers;
	}

	/**
	 * @return the localPlayersIndexes
	 */
	public int[] getLocalPlayersIndexes() {
		return localPlayersIndexes;
	}

	/**
	 * @param localPlayersIndexes the localPlayersIndexes to set
	 */
	public void setLocalPlayersIndexes(int[] localPlayersIndexes) {
		this.localPlayersIndexes = localPlayersIndexes;
	}

	/**
	 * @return the localPlayersIndexesCount
	 */
	public int getLocalPlayersIndexesCount() {
		return localPlayersIndexesCount;
	}

	/**
	 * @param localPlayersIndexesCount the localPlayersIndexesCount to set
	 */
	public void setLocalPlayersIndexesCount(int localPlayersIndexesCount) {
		this.localPlayersIndexesCount = localPlayersIndexesCount;
	}

	/**
	 * @return the outPlayersIndexes
	 */
	public int[] getOutPlayersIndexes() {
		return outPlayersIndexes;
	}

	/**
	 * @param outPlayersIndexes the outPlayersIndexes to set
	 */
	private void setOutPlayersIndexes(int[] outPlayersIndexes) {
		this.outPlayersIndexes = outPlayersIndexes;
	}

	/**
	 * @return the outPlayersIndexesCount
	 */
	public int getOutPlayersIndexesCount() {
		return outPlayersIndexesCount;
	}

	/**
	 * @param outPlayersIndexesCount the outPlayersIndexesCount to set
	 */
	public void setOutPlayersIndexesCount(int outPlayersIndexesCount) {
		this.outPlayersIndexesCount = outPlayersIndexesCount;
	}

	/**
	 * @return the regionHashes
	 */
	public int[] getRegionHashes() {
		return regionHashes;
	}

	/**
	 * @param regionHashes the regionHashes to set
	 */
	private void setRegionHashes(int[] regionHashes) {
		this.regionHashes = regionHashes;
	}

	/**
	 * @return the cachedAppearencesHashes
	 */
	public byte[][] getCachedAppearencesHashes() {
		return cachedAppearencesHashes;
	}

	/**
	 * @param cachedAppearencesHashes the cachedAppearencesHashes to set
	 */
	public void setCachedAppearencesHashes(byte[][] cachedAppearencesHashes) {
		this.cachedAppearencesHashes = cachedAppearencesHashes;
	}

	/**
	 * @return the totalRenderDataSentLength
	 */
	public int getTotalRenderDataSentLength() {
		return totalRenderDataSentLength;
	}

	/**
	 * @param totalRenderDataSentLength the totalRenderDataSentLength to set
	 */
	public void setTotalRenderDataSentLength(int totalRenderDataSentLength) {
		this.totalRenderDataSentLength = totalRenderDataSentLength;
	}

	/**
	 * @return the localAddedPlayers
	 */
	public int getLocalAddedPlayers() {
		return localAddedPlayers;
	}

	/**
	 * @param localAddedPlayers the localAddedPlayers to set
	 */
	public void setLocalAddedPlayers(int localAddedPlayers) {
		this.localAddedPlayers = localAddedPlayers;
	}

	/**
	 * @return the slotFlags
	 */
	public byte[] getSlotFlags() {
		return slotFlags;
	}

	/**
	 * @param slotFlags the slotFlags to set
	 */
	public void setSlotFlags(byte[] slotFlags) {
		this.slotFlags = slotFlags;
	}
	/**
	 * Adds the player to the local player update queue
	 * @param playerIndex
	 * @param player
	 */
	public void addLocalPlayer (int playerIndex, Player player) {		
		increaseLocalAddedPlayers();
		localPlayers[playerIndex] = player;
		slotFlags[playerIndex] |= (byte) 2;
	}
	
	/**
	 * Removes the player from the local player update queue
	 * @param playerIndex
	 */
	public void removeLocalPlayer (int playerIndex) {
		localPlayers[playerIndex] = null;
	}
	
	/**
	 * Repacks the view port so that the local players and outside players are stored in the correct place.
	 * This should be called at the end of each player update.
	 */
	public void repackViewport() {
		totalRenderDataSentLength = 0;
		localPlayersIndexesCount = 0;
		outPlayersIndexesCount = 0;
		localAddedPlayers = 0;
		for (int playerIndex = 1; playerIndex < 2048; playerIndex++) {
			slotFlags[playerIndex] >>= 1;
			Player p = localPlayers[playerIndex];
			if (p == null) {
				outPlayersIndexes[outPlayersIndexesCount++] = playerIndex;
			} else {
				localPlayersIndexes[localPlayersIndexesCount++] = playerIndex;
			}
		}
	}

	/**
	 * Increases the local added count.
	 * @return The count.
	 */
	public int increaseLocalAddedPlayers() {
		return localAddedPlayers++;
	}
	
	public int increaseLocalIndexesCount() {
		return localPlayersIndexesCount++;
	}
	
	public int increaseOutsideIndexesCount() {
		return outPlayersIndexesCount++;
	}
}
