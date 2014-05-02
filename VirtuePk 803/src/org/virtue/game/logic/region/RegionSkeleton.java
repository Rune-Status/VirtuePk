package org.virtue.game.logic.region;

public class RegionSkeleton {
	private int regionX;
	private int regionY;
	private int[][][] masks;
	private boolean clipedOnly;

	/**
	 * Constructs a new {@code RegionMap} instance.
	 * @param regionId The region ID.
	 * @param clipedOnly If it's clipped only.
	 */
	public RegionSkeleton(int regionId, boolean clipedOnly) {
		this.regionX = (regionId >> 8) * 64;
		this.regionY = (regionId & 0xff) * 64;
		this.masks = new int[4][64][64];
		this.clipedOnly = clipedOnly;
	}

	/**
	 * Clips a specific tile.
	 * @param plane The tile plane.
	 * @param x The tile x coord.
	 * @param y The tile y coord.
	 */
	public void clipTile(int plane, int x, int y) {
		addMask(plane, x, y, 0x200000);
	}
	
	/**
	 * Adds a mask to a tile.
	 * @param plane The plane.
	 * @param x The x coord.
	 * @param y The y coord.
	 * @param mask The mask to add.
	 */
	private void addMask(int plane, int x, int y, int mask) {
		if (x >= 64 || y >= 64 || x < 0 || y < 0) {
			//System.err.println("Invalid tile at x="+x+", y="+y);
			Tile tile = new Tile(regionX + x, regionY + y, plane);
			int regionId = tile.getRegionID();
			int newRegionX = (regionId >> 8) * 64;
			int newRegionY = (regionId & 0xff) * 64;
			if (clipedOnly) {
				//World.getWorld().getRegion(tile.getRegionID()).getClippedRegionMap().addMask(plane, tile.getX() - newRegionX, tile.getY() - newRegionY, mask);
			} else {
				//World.getWorld().getRegion(tile.getRegionID()).getRegionMap().addMask(plane, tile.getX() - newRegionX, tile.getY() - newRegionY, mask);
			}
			return;
		}
		//System.out.println("Clipping tile at x="+x+", y="+y+", z="+plane+", mask="+mask);
		masks[plane][x][y] = masks[plane][x][y] | mask;
	}
	
	/**
	 * Adds a wall onto the map.
	 * @param plane The plane of the object.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param type The type of the object.
	 * @param rotation The rotation of the object.
	 * @param solid If the object is solid.
	 * @param unknown
	 */
	public void addWall(int plane, int x, int y, int type, int rotation, boolean solid, boolean unknown) {
		if (type == 0) {
			if (rotation == 0) {
				addMask(plane, x, y, 128);
				addMask(plane, x - 1, y, 8);
			}
			if (rotation == 1) {
				addMask(plane, x, y, 2);
				addMask(plane, x, 1 + y, 32);
			}
			if (rotation == 2) {
				addMask(plane, x, y, 8);
				addMask(plane, 1 + x, y, 128);
			}
			if (rotation == 3) {
				addMask(plane, x, y, 32);
				addMask(plane, x, -1 + y, 2);
			}
		}
		if (type == 1 || type == 3) {
			if (rotation == 0) {
				addMask(plane, x, y, 1);
				addMask(plane, -1 + x, 1 + y, 16);
			}
			if (rotation == 1) {
				addMask(plane, x, y, 4);
				addMask(plane, 1 + x, 1 + y, 64);
			}
			if (rotation == 2) {
				addMask(plane, x, y, 16);
				addMask(plane, x + 1, -1 + y, 1);
			}
			if (rotation == 3) {
				addMask(plane, x, y, 64);
				addMask(plane, x - 1, -1 + y, 4);
			}
		}
		if (type == 2) {
			if (rotation == 0) {
				addMask(plane, x, y, 130);
				addMask(plane, -1 + x, y, 8);
				addMask(plane, x, y + 1, 32);
			}
			if (rotation == 1) {
				addMask(plane, x, y, 10);
				addMask(plane, x, 1 + y, 32);
				addMask(plane, 1 + x, y, 128);
			}
			if (rotation == 2) {
				addMask(plane, x, y, 40);
				addMask(plane, 1 + x, y, 128);
				addMask(plane, x, -1 + y, 2);
			}
			if (rotation == 3) {
				addMask(plane, x, y, 160);
				addMask(plane, x, -1 + y, 2);
				addMask(plane, -1 + x, y, 8);
			}
		}
		if (solid) {
			if (type == 0) {
				if (rotation == 0) {
					addMask(plane, x, y, 0x10000);
					addMask(plane, x - 1, y, 4096);
				}
				if (rotation == 1) {
					addMask(plane, x, y, 1024);
					addMask(plane, x, 1 + y, 16384);
				}
				if (rotation == 2) {
					addMask(plane, x, y, 4096);
					addMask(plane, x + 1, y, 0x10000);
				}
				if (rotation == 3) {
					addMask(plane, x, y, 16384);
					addMask(plane, x, -1 + y, 1024);
				}
			}
			if (type == 1 || type == 3) {
				if (rotation == 0) {
					addMask(plane, x, y, 512);
					addMask(plane, x - 1, y + 1, 8192);
				}
				if (rotation == 1) {
					addMask(plane, x, y, 2048);
					addMask(plane, x + 1, 1 + y, 32768);
				}
				if (rotation == 2) {
					addMask(plane, x, y, 8192);
					addMask(plane, x + 1, y - 1, 512);
				}
				if (rotation == 3) {
					addMask(plane, x, y, 32768);
					addMask(plane, x - 1, -1 + y, 2048);
				}
			}
			if (type == 2) {
				if (rotation == 0) {
					addMask(plane, x, y, 0x10400);
					addMask(plane, -1 + x, y, 4096);
					addMask(plane, x, y + 1, 16384);
				}
				if (rotation == 1) {
					addMask(plane, x, y, 5120);
					addMask(plane, x, y + 1, 16384);
					addMask(plane, 1 + x, y, 0x10000);
				}
				if (rotation == 2) {
					addMask(plane, x, y, 20480);
					addMask(plane, x + 1, y, 0x10000);
					addMask(plane, x, y - 1, 1024);
				}
				if (rotation == 3) {
					addMask(plane, x, y, 0x14000);
					addMask(plane, x, -1 + y, 1024);
					addMask(plane, x - 1, y, 4096);
				}
			}
		}
		if (unknown) {
			if (type == 0) {
				if (rotation == 0) {
					addMask(plane, x, y, 0x20000000);
					addMask(plane, x - 1, y, 0x2000000);
				}
				if (rotation == 1) {
					addMask(plane, x, y, 0x800000);
					addMask(plane, x, y + 1, 0x8000000);
				}
				if (rotation == 2) {
					addMask(plane, x, y, 0x2000000);
					addMask(plane, x + 1, y, 0x20000000);
				}
				if (rotation == 3) {
					addMask(plane, x, y, 0x8000000);
					addMask(plane, x, y - 1, 0x800000);
				}
			}
			if (type == 1 || type == 3) {
				if (rotation == 0) {
					addMask(plane, x, y, 0x400000);
					addMask(plane, x - 1, y + 1, 0x4000000);
				}
				if (rotation == 1) {
					addMask(plane, x, y, 0x1000000);
					addMask(plane, 1 + x, 1 + y, 0x10000000);
				}
				if (rotation == 2) {
					addMask(plane, x, y, 0x4000000);
					addMask(plane, x + 1, -1 + y, 0x400000);
				}
				if (rotation == 3) {
					addMask(plane, x, y, 0x10000000);
					addMask(plane, -1 + x, y - 1, 0x1000000);
				}
			}
			if (type == 2) {
				if (rotation == 0) {
					addMask(plane, x, y, 0x20800000);
					addMask(plane, -1 + x, y, 0x2000000);
					addMask(plane, x, 1 + y, 0x8000000);
				}
				if (rotation == 1) {
					addMask(plane, x, y, 0x2800000);
					addMask(plane, x, 1 + y, 0x8000000);
					addMask(plane, x + 1, y, 0x20000000);
				}
				if (rotation == 2) {
					addMask(plane, x, y, 0xa000000);
					addMask(plane, 1 + x, y, 0x20000000);
					addMask(plane, x, y - 1, 0x800000);
				}
				if (rotation == 3) {
					addMask(plane, x, y, 0x28000000);
					addMask(plane, x, y - 1, 0x800000);
					addMask(plane, -1 + x, y, 0x2000000);
				}
			}
		}	
	}
	
	/**
	 * Adds an object onto the map.
	 * @param plane The object plane.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param sizeX The size x.
	 * @param sizeY The size y.
	 * @param solid If the object is solid.
	 * @param b
	 */
	public void addObject(int plane, int x, int y, int sizeX, int sizeY, boolean solid, boolean b) {
		int mask = 256;
		if (solid) {
			mask |= 131072;
		}
		if (b) {
			mask |= 1073741824;
		}
		for (int tileX = x; tileX < x + sizeX; tileX++) {
			for (int tileY = y; tileY < y + sizeY; tileY++) {
				addMask(plane, tileX, tileY, mask);
			}
		}
	}
	
	/**
	 * @return The map masks.
	 */
	public int[][][] getMasks() {
		return masks;
	}

	/**
	 * @return the regionX
	 */
	public int getRegionX() {
		return regionX;
	}

	/**
	 * @return the regionY
	 */
	public int getRegionY() {
		return regionY;
	}

	/**
	 * @return the clipedOnly
	 */
	public boolean isClipedOnly() {
		return clipedOnly;
	}
}
