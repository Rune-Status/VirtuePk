package org.virtue.game.logic.region;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.virtue.game.logic.node.entity.player.update.movement.MovementUtils;

/**
 * @author Taylor Moon
 * @since Jan 25, 2014
 */
public class RegionManager {

	/**
	 * Represents the regions in this region manager.
	 */
	private final List<Region> regions = Collections.synchronizedList(new ArrayList<Region>());
	
	/**
	 * Represents the region update manager.
	 */
	private final RegionUpdateManager UPDATER = new RegionUpdateManager();
	
	/**
	 * Represents the region update manager.
	 */
	public static final RegionLoader LOADER = new RegionLoader();
	
	/**
	 * Replaces a region by it's id.
	 * @param id The id of the region to replace.
	 * @param region The new region.
	 * @return The previous region.
	 */
	public Region replaceRegionByID(int id, Region region) {
		synchronized (regions) {
			return replaceRegionByIndex(getRegionIndex(regions.get(id)), region);
		}
	}
	
	/**
	 * Replaces a region by it's index on the list.
	 * @param index The index of the region to replace.
	 * @param newRegion The replacement region.
	 * @return The previous region.
	 */
	public Region replaceRegionByIndex(int index, Region newRegion) {
		synchronized (regions) {
			Region region = regions.get(index);
			regions.set(index, newRegion);
			return region;
		}
	}
	
	/**
	 * Adds a region to the list.
	 * @param region The region to add.
	 * @param index The index of the region.
	 */
	public void addRegion(Region region, int index) {
		synchronized (regions) {
			regions.add(index, region);
		}
	}

	/**
	 * Adds a region to the list.
	 * @param region The region to add.
	 */
	public void addRegion(Region region) {
		synchronized (regions) {
			regions.add(region);
		}
	}
	
	/**
	 * Removes a region from the list by its specified ID.
	 * @param id The ID of the region to remove.
	 * @return The region that was removed.
	 */
	public Region removeById(int id) {
		Region region = getRegionByID(id);
		if (region == null) {
			return null;
		}
		synchronized (regions) {
			regions.remove(getRegionIndex(region));
		}
		return region;
	}
	
	/**
	 * Removes the region that is at the specified index of the list.
	 * @param index The index of the region to remove.
	 * @return The region that was removed.
	 */
	public Region removeByIndex(int index) {
		Region region = regions.get(index);
		if (region == null) {
			return null;
		}
		synchronized (regions) {
			regions.remove(index);
		}
		return region;
	}
	
	/**
	 * Removes a region from the list.
	 * @param region The region to remove.
	 */
	public void removeRegion(Region region) {
		synchronized(regions) {
			regions.remove(region);
		}
	}
	
	/**
	 * Returns a region corresponding to the specified index of the list.
	 * @param index The index of the region in the list to get.
	 * @return The region.
	 */
	public Region getRegionByIndex(int index) {
		synchronized (regions) {
			return regions.get(index);
		}
	}
	
	/**
	 * Returns the index of a specified region.
	 * @param region The region to get the index of.
	 * @return The index.
	 */
	public int getRegionIndex(Region region) {
		synchronized (regions) {
			for (int r = 0; r < regions.size(); r++) {
				if (regions.get(r).equals(region)) {
					return r;
				}
			}
			return -1;
		}
	}
	
	/**
	 * Returns a region corresponding to the specified id.
	 * @param id The id of the region to get.
	 * @return The region.
	 */
	public Region getRegionByID(int id) {
		synchronized (regions) {
			for (Region region : regions) {
				if (region.getId() == id) {
					return region;
				}
			}
			return null;
		}
	}
	
	/**
	 * Returns a region corresponding to the specified id.
	 * @param id The id of the region to get.
	 * @param load Whether to load the region or not
	 * @return The region.
	 */
	public Region getRegionByID(int id, boolean load) {
		Region region = getRegionByID(id);
		if (region != null) {
			if (load && !region.getLoadingStage().equals(RegionLoadingStage.DONE_LOADING)) {
				LOADER.loadRegion(region);
			}
			return region;
		} else {
			region = new Region(id);
			addRegion(region);
			if (load) {
				LOADER.loadRegion(region);
			}
			return region;
		}
	}
	
	/**
	 * Checks if the region list contains a specified region.
	 * @param id The ID of the region to check.
	 * @return True if so; false otherwise.
	 */
	public boolean containsRegion(int id) {
		synchronized (regions) {
			return regions.contains(getRegionByID(id));
		}
	}

	/**
	 * Checks if the region list contains a specified region.
	 * @param region The region to check.
	 * @return True if so; false otherwise.
	 */
	public boolean containsRegion(Region region) {
		synchronized (regions) {
			return regions.contains(region);
		}
	}

	/**
	 * Queues a new pending region update request in the update manager's list.
	 * @param event The event to register.
	 */
	public void registerRegionUpdate(RegionUpdateEvent event) {
		UPDATER.getPendingRegions().add(event);
	}
	
	public void runRegionTick () {//TODO: Find out how this is actually supposed to be handled...
		synchronized (regions) {
			for (Region r : regions) {
				r.checkTempObjects();
			}
		}
	}

	/**
	 * Gets the masks of a region.
	 * @param plane The plane.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @return The masks.
	 */
	public int getMask(int plane, int x, int y) {
		return getMask(new Tile(x, y, plane));
	}
	
	public int getMask(Tile tile) {
		int regionId = tile.getRegionID();
		Region region = getRegionByID(regionId, true);
		if (region == null) {
			return 0;
		}
		//int baseLocalX = tile.getX() - ((regionId >> 8) * 64);
		//int baseLocalY = tile.getY() - ((regionId & 0xFF) * 64);
		//System.out.println("Retreiving mask for tile: "+tile.getX()+", "+tile.getY()+", mask="+region.getMask(tile.getPlane(), tile.getXInRegion(), tile.getYInRegion()));
		return region.getMask(tile.getPlane(), tile.getXInRegion(), tile.getYInRegion());
	}
	
	public boolean isClipped (Tile tile) {
		int regionID = tile.getRegionID();
		Region region = getRegionByID(regionID, true);
		if (region == null) {
			return false;
		}
		//System.out.println("Checking x="+tile.getXInRegion()+", y="+tile.getYInRegion()+", clipType="+region.getClippedRegionMap().getMasks()[tile.getPlane()][tile.getXInRegion()][tile.getYInRegion()]);
		return region.getClippedRegionMap().getMasks()[tile.getPlane()][tile.getXInRegion()][tile.getYInRegion()] != 0;
	}

	/**
	 * Gets the rotation of a tile.
	 * @param plane The plane.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @return The rotation.
	 */
	public int getRotation(int plane, int x, int y) {
		Tile tile = new Tile(x, y, plane);
		int regionId = tile.getRegionID();
		Region region = getRegionByID(regionId, true);
		if (region == null) {
			return 0;
		}
		int baseLocalX = x - ((regionId >> 8) * 64);
		int baseLocalY = y - ((regionId & 0xff) * 64);
		return region.getRotation(tile.getPlane(), baseLocalX, baseLocalY);
	}
	
	/**
	 * Checks the walk steps for a region.
	 * @param plane The plane to check.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param dir The direction.
	 * @param size The size.
	 * @return If we can step.
	 */
	public final boolean checkWalkStep(int plane, int x, int y, int dir, int size) {
		int xOffset = MovementUtils.DIRECTION_DELTA_X[dir];
		int yOffset = MovementUtils.DIRECTION_DELTA_Y[dir];
		int rotation = getRotation(plane, x + xOffset, y + yOffset);
		if (rotation != 0) {
			for (int rotate = 0; rotate < (4 - rotation); rotate++) {
				int fakeChunckX = xOffset;
				int fakeChunckY = yOffset;
				xOffset = fakeChunckY;
				yOffset = 0 - fakeChunckX;
			}
		}

		if (size == 1) {
			int mask = getMask(plane, x + MovementUtils.DIRECTION_DELTA_X[dir], y + MovementUtils.DIRECTION_DELTA_Y[dir]);
			if (xOffset == -1 && yOffset == 0) {
				return (mask & 0x42240000) == 0;
			}
			if (xOffset == 1 && yOffset == 0) {
				return (mask & 0x60240000) == 0;
			}
			if (xOffset == 0 && yOffset == -1) {
				return (mask & 0x40a40000) == 0;
			}
			if (xOffset == 0 && yOffset == 1) {
				return (mask & 0x48240000) == 0;
			}
			if (xOffset == -1 && yOffset == -1) {
				return (mask & 0x43a40000) == 0 && (getMask(plane, x - 1, y) & 0x42240000) == 0 && (getMask(plane, x, y - 1) & 0x40a40000) == 0;
			}
			if (xOffset == 1 && yOffset == -1) {
				return (mask & 0x60e40000) == 0 && (getMask(plane, x + 1, y) & 0x60240000) == 0 && (getMask(plane, x, y - 1) & 0x40a40000) == 0;
			}
			if (xOffset == -1 && yOffset == 1) {
				return (mask & 0x4e240000) == 0 && (getMask(plane, x - 1, y) & 0x42240000) == 0 && (getMask(plane, x, y + 1) & 0x48240000) == 0;
			}
			if (xOffset == 1 && yOffset == 1) {
				return (mask & 0x78240000) == 0 && (getMask(plane, x + 1, y) & 0x60240000) == 0 && (getMask(plane, x, y + 1) & 0x48240000) == 0;
			}
		} else if (size == 2) {
			if (xOffset == -1 && yOffset == 0) {
				return (getMask(plane, x - 1, y) & 0x43a40000) == 0 && (getMask(plane, x - 1, y + 1) & 0x4e240000) == 0;
			}
			if (xOffset == 1 && yOffset == 0) {
				return (getMask(plane, x + 2, y) & 0x60e40000) == 0 && (getMask(plane, x + 2, y + 1) & 0x78240000) == 0;
			}
			if (xOffset == 0 && yOffset == -1) {
				return (getMask(plane, x, y - 1) & 0x43a40000) == 0 && (getMask(plane, x + 1, y - 1) & 0x60e40000) == 0;
			}
			if (xOffset == 0 && yOffset == 1)
				return (getMask(plane, x, y + 2) & 0x4e240000) == 0 && (getMask(plane, x + 1, y + 2) & 0x78240000) == 0;
			if (xOffset == -1 && yOffset == -1)
				return (getMask(plane, x - 1, y) & 0x4fa40000) == 0 && (getMask(plane, x - 1, y - 1) & 0x43a40000) == 0 && (getMask(plane, x, y - 1) & 0x63e40000) == 0;
			if (xOffset == 1 && yOffset == -1)
				return (getMask(plane, x + 1, y - 1) & 0x63e40000) == 0 && (getMask(plane, x + 2, y - 1) & 0x60e40000) == 0 && (getMask(plane, x + 2, y) & 0x78e40000) == 0;
			if (xOffset == -1 && yOffset == 1)
				return (getMask(plane, x - 1, y + 1) & 0x4fa40000) == 0 && (getMask(plane, x - 1, y + 1) & 0x4e240000) == 0 && (getMask(plane, x, y + 2) & 0x7e240000) == 0;
			if (xOffset == 1 && yOffset == 1)
				return (getMask(plane, x + 1, y + 2) & 0x7e240000) == 0 && (getMask(plane, x + 2, y + 2) & 0x78240000) == 0 && (getMask(plane, x + 1, y + 1) & 0x78e40000) == 0;
		} else {
			if (xOffset == -1 && yOffset == 0) {
				if ((getMask(plane, x - 1, y) & 0x43a40000) != 0 || (getMask(plane, x - 1, -1 + (y + size)) & 0x4e240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0)
						return false;
			} else if (xOffset == 1 && yOffset == 0) {
				if ((getMask(plane, x + size, y) & 0x60e40000) != 0 || (getMask(plane, x + size, y - (-size + 1)) & 0x78240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getMask(plane, x + size, y + sizeOffset) & 0x78e40000) != 0)
						return false;
			} else if (xOffset == 0 && yOffset == -1) {
				if ((getMask(plane, x, y - 1) & 0x43a40000) != 0 || (getMask(plane, x + size - 1, y - 1) & 0x60e40000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0)
						return false;
			} else if (xOffset == 0 && yOffset == 1) {
				if ((getMask(plane, x, y + size) & 0x4e240000) != 0 || (getMask(plane, x + (size - 1), y + size) & 0x78240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++)
					if ((getMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0)
						return false;
			} else if (xOffset == -1 && yOffset == -1) {
				if ((getMask(plane, x - 1, y - 1) & 0x43a40000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getMask(plane, x - 1, y + (-1 + sizeOffset)) & 0x4fa40000) != 0 || (getMask(plane, sizeOffset - 1 + x, y - 1) & 0x63e40000) != 0)
						return false;
			} else if (xOffset == 1 && yOffset == -1) {
				if ((getMask(plane, x + size, y - 1) & 0x60e40000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getMask(plane, x + size, sizeOffset + (-1 + y)) & 0x78e40000) != 0 || (getMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0)
						return false;
			} else if (xOffset == -1 && yOffset == 1) {
				if ((getMask(plane, x - 1, y + size) & 0x4e240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0 || (getMask(plane, -1 + (x + sizeOffset), y + size) & 0x7e240000) != 0)
						return false;
			} else if (xOffset == 1 && yOffset == 1) {
				if ((getMask(plane, x + size, y + size) & 0x78240000) != 0)
					return false;
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++)
					if ((getMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0 || (getMask(plane, x + size, y + sizeOffset) & 0x78e40000) != 0)
						return false;
			}
		}
		return true;
	}

	/**
	 * @return the updater
	 */
	public RegionUpdateManager getUpdater() {
		return UPDATER;
	}
}
