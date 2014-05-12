package org.virtue.game.logic.region;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.virtue.Launcher;

import org.virtue.cache.def.ObjectDefinition;
import org.virtue.game.core.AttributeSet;
import org.virtue.game.logic.World;
import org.virtue.game.logic.item.GroundItem;
import org.virtue.game.logic.node.entity.npc.NPC;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.object.RS3Object;
import org.virtue.game.logic.node.object.TemporaryObject;
import org.virtue.network.protocol.messages.GroundItemMessage.GroundItemType;
import org.virtue.network.protocol.messages.ObjectMessage.ObjectType;
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
	 * Represents an array of world objects in this region.
	 */
	private RS3Object[][][][] objects;
	
	/**
	 * Represents the tempory objects located within the region
	 */
	private final CopyOnWriteArrayList<TemporaryObject> tempObjects = new CopyOnWriteArrayList<TemporaryObject>();
	
	/**
	 * Represents the skeleton map.
	 */
	private RegionSkeleton map;

	/**
	 * Represents the skeleton clipping map.
	 */
	private RegionSkeleton clipedOnlyMap;
	
	/**
	 * Represents the current loading stage for this {@link Region}.
	 */
	private RegionLoadingStage loadingStage = RegionLoadingStage.NONE;
	
	/**
	 * Constructs a new {@code Region.java}.
	 * @param id The ID.
	 */
	public Region(final int id) {
		this.id = id;
		players = new EntityList<Player>(PLAYER_CAP);
		npcs = new EntityList<NPC>(NPC_CAP);
		RegionManager.LOADER.loadRegion(this);//TODO: Find the proper place to put region initialisation
		Launcher.getEngine().getTickManager().register(new TempObjectUpdate(id));
	}

	/**
	 * @return The loadingStage
	 */
	public RegionLoadingStage getLoadingStage() {
		return loadingStage;
	}

	/**
	 * @param loadingStage The loadingStage to set
	 */
	public void setLoadingStage(RegionLoadingStage loadingStage) {
		this.loadingStage = loadingStage;
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
	 * @return	The tempery objects
	 */
	public List<TemporaryObject> getTemporyObjects () {
		return tempObjects;
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
	 * Returns the region skeleton.
	 * @return The skeleton.
	 */
	public RegionSkeleton getRegionMap() {
		if (map == null)
			map = new RegionSkeleton(id, false);
		return map;
	}
	
	/**
	 * Returns the clipped map skeleton.
	 * @return The clipped skeleton.
	 */
	public RegionSkeleton getClippedRegionMap() {
		if (clipedOnlyMap == null) {
			clipedOnlyMap = new RegionSkeleton(id, true);
		}
		return clipedOnlyMap;
	}
	
	/**
	 * Gets the mask for a region.
	 * @param plane The region plane.
	 * @param localX The local x.
	 * @param localY The local y.
	 * @return The masks.
	 */
	public int getMask(int plane, int localX, int localY) {
		if (map == null || !loadingStage.equals(RegionLoadingStage.DONE_LOADING))
			return -1;
		if (localX >= 64 || localY >= 64 || localX < 0 || localY < 0) {
			Tile tile = new Tile(map.getRegionX() + localX, map.getRegionY() + localY, plane);
			int regionId = tile.getRegionID();
			int newRegionX = (regionId >> 8) * 64;
			int newRegionY = (regionId & 0xff) * 64;
			return World.getWorld().getRegionManager().getRegionByID(tile.getRegionID(), true).getMask(plane, tile.getX() - newRegionX, tile.getY() - newRegionY);
		}
		return map.getMasks()[plane][localX][localY];
	}

	/**
	 * Gets the default rotation of a tile.
	 * @param plane The plane.
	 * @param baseLocalX The local X.
	 * @param baseLocalY The local Y.
	 * @return The rotation
	 */
	public int getRotation(int plane, int baseLocalX, int baseLocalY) {
		return 0;
	}
	
	public void updateObject (TemporaryObject object) {
		updateObject(object, false);
	}
	
	public void updateObject (TemporaryObject object, boolean remove) {
		if (!tempObjects.contains(object) && !remove) {
			tempObjects.add(object);
		}
		for (Player p : World.getWorld().getPlayers()) {
			if (p.getViewport().getRegions().contains(id)) {
				p.getPacketDispatcher().dispatchObjectUpdate(object, ObjectType.CREATE);
			}
		}
		if (remove) {
			tempObjects.remove(object);
		}
	}
	
	public void spawnObject (TemporaryObject object) {
		addObject(object, object.getTile().getPlane(), object.getTile().getXInRegion(), object.getTile().getYInRegion());
		synchronized (tempObjects) {
			tempObjects.add(object);
		}
		for (Player p : World.getWorld().getPlayers()) {
			if (p.getViewport().getRegions().contains(id)) {
				p.getPacketDispatcher().dispatchObjectUpdate(object, ObjectType.CREATE);
			}
		}
	}
	
	public void removeTemporyObject (TemporaryObject object) {
		synchronized (tempObjects) {
			tempObjects.remove(object);
		}
	}
	
	public void destroyObject (RS3Object object) {
		for (Player p : World.getWorld().getPlayers()) {
			if (p.getViewport().getRegions().contains(id)) {
				p.getPacketDispatcher().dispatchObjectUpdate(object, ObjectType.DESTROY);
			}
		}
	}

	/**
	 * Adds a object into the region.
	 * @param object The object to add.
	 * @param plane The plane to add.
	 * @param localX The x coordinate to add.
	 * @param localY The y coordinate to add.
	 */
	public void addObject(RS3Object object, int plane, int localX, int localY) {
		//System.out.println("Adding object at plane="+plane+", localX="+localX+", localY="+localY);
		addMapObject(object, localX, localY);
		if (objects == null) {
			objects = new RS3Object[4][64][64][];
		}
		RS3Object[] tileObjects = objects[plane][localX][localY];
		if (tileObjects == null) {
			objects[plane][localX][localY] = new RS3Object[] { object };
		} else {
			RS3Object[] newTileObjects = new RS3Object[tileObjects.length + 1];
			newTileObjects[tileObjects.length] = object;
			System.arraycopy(tileObjects, 0, newTileObjects, 0, tileObjects.length);
			objects[plane][localX][localY] = newTileObjects;
		}
	}

	/**
	 * Adds object data to the map.
	 * @param object The object to add.
	 * @param localX The local x coord.
	 * @param localY The local y coord.
	 */
	private void addMapObject(RS3Object object, int x, int y) {
		if (map == null) {
			map = new RegionSkeleton(id, false);
		}
		if (clipedOnlyMap == null) {
			clipedOnlyMap = new RegionSkeleton(id, true);
		}
		int plane = object.getTile().getPlane();
		int type = object.getType();
		int rotation = object.getRotation();
		if (x < 0 || y < 0 || x >= map.getMasks()[plane].length || y >= map.getMasks()[plane][x].length) {
			return;
		}
		ObjectDefinition objectDef = object.getDefinition();//ObjectDefinitionLoader.forId(object.getId());
		if (type == 22 ? objectDef.clipType != 0 : objectDef.clipType == 0) {
			return;
		}
		if (type >= 0 && type <= 3) {
			map.addWall(plane, x, y, type, rotation, objectDef.isProjectileClipped(), true);
			if (objectDef.projectileClipped) {
				clipedOnlyMap.addWall(plane, x, y, type, rotation, objectDef.isProjectileClipped(), true);
			}
		} else if (type >= 9 && type <= 21) {
			int sizeX;
			int sizeY;
			if (rotation != 1 && rotation != 3) {
				sizeX = objectDef.getSize()[0];
				sizeY = objectDef.getSize()[1];
			} else {
				sizeX = objectDef.getSize()[1];
				sizeY = objectDef.getSize()[0];
			}
			map.addObject(plane, x, y, sizeX, sizeY, objectDef.isProjectileClipped(), true);
			if (objectDef.projectileClipped) {
				clipedOnlyMap.addObject(plane, x, y, sizeX, sizeY, objectDef.isProjectileClipped(), true);
			}
		}
		/*if (type >= 0 && type <= 3) {
			map.addWall(plane, x, y, type, rotation, objectDef.isProjectileClipped(), true);
			if (objectDef.isProjectileClipped()) {//objectDef.isProjectileClipped()
				//clipedOnlyMap.addWall(plane, x, y, type, rotation, objectDef.isProjectileClipped(), true);
				//System.out.println(clipedOnlyMap.getMasks()[plane][x][y]+"................................");	
			}
		} else if (type >= 9 && type <= 21) {
			int sizeX, sizeY;
			if (rotation != 1 && rotation != 3) {
				sizeX = objectDef.getSize()[0];
				sizeY = objectDef.getSize()[1];
			} else {
				sizeX = objectDef.getSize()[1];
				sizeY = objectDef.getSize()[0];
			}
			map.addObject(plane, x, y, sizeX, sizeY, objectDef.isProjectileClipped(), true);
			if (objectDef.isProjectileClipped()) {//objectDef.isProjectileClipped()
				//clipedOnlyMap.addObject(plane, x, y, sizeX, sizeY, objectDef.isProjectileClipped(), true);
			}
		}*/
	}
	
	public RS3Object getObject (int id, Tile location) {
		//System.out.println("Searching for object at plane="+location.getPlane()+", localX="+location.getXInRegion()+", localY="+location.getYInRegion());
		RS3Object[] tileObjects = objects[location.getPlane()][location.getXInRegion()][location.getYInRegion()];
		if (tileObjects == null) {
			return null;
		}
		for (RS3Object object : tileObjects) {
			if (object.getId() == id) {
				return object;
			}
		}
		return null;
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
		//Really have no idea what this is supposed to do...
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
	
	public void checkTempObjects () {
		//System.out.println("Checking temporary objects in region "+id);
		for (TemporaryObject object : tempObjects) {
			if (object.checkRespawn()) {
				object.respawn();
				tempObjects.remove(object);
			}
		}
	}
	
	/**
	 * Returns the far X coordinate of this region.
	 * @return The X coordinate.
	 */
	public int getRegionX() {
		return (id >> 8) * 64;
	}
	
	/**
	 * Returns the near Y coordinate of this region.
	 * @return The Y coordinate.
	 */
	public int getRegionY() {
		return (id & 0xFF) * 64;
	}
	
	public int getArchiveKey () {
		return ((getRegionX() >> 3) / 8) | (((getRegionY() >> 3) / 8) << 7);
	}
}
