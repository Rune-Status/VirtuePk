package org.virtue.game.logic.node.entity.region;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Taylor Moon
 * @since Jan 25, 2014
 */
public class RegionManager {

	/**
	 * Represents the regions in this region manager.
	 */
	private List<Region> regions = Collections.synchronizedList(new ArrayList<Region>());
	
	/**
	 * Represents the region update manager.
	 */
	private final RegionUpdateManager UPDATER = new RegionUpdateManager();
	
	/**
	 * Replaces a region by it's id.
	 * @param id The id of the region to replace.
	 * @param region The new region.
	 * @return The previous region.
	 */
	public Region replaceRegionById(int id, Region region) {
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
		Region region = getRegionById(id);
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
	public Region getRegionById(int id) {
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
	 * Checks if the region list contains a specified region.
	 * @param id The ID of the region to check.
	 * @return True if so; false otherwise.
	 */
	public boolean containsRegion(int id) {
		synchronized (regions) {
			return regions.contains(getRegionById(id));
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

	/**
	 * @return the updater
	 */
	public RegionUpdateManager getUpdater() {
		return UPDATER;
	}
}
