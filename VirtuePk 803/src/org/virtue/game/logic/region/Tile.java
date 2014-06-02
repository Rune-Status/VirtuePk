package org.virtue.game.logic.region;

/**
 * @author Taylor
 * @version 1.0
 */
public class Tile {
	
	/**
	 * Represents the size X of this {@link Tile}.
	 */
	public static final int SIZE_X = 0x1;
	
	/**
	 * Represents the size Y of this {@link Tile}.
	 */
	public static final int SIZE_Y = 0x1;

	/**
	 * Represents the X coordinate on the grid.
	 */
	private short x;
	
	/**
	 * Represents the Y coordinate on the grid.
	 */
	private short y;
	
	/**
	 * Represents the height of the coordinates {@link #x} and {@link #y}.
	 */
	private byte plane;

	/**
	 * Constructs a new {@code Tile.java}.
	 * @param x The X coordinate on the grid.
	 * @param y The Y coordinate on the grid.
	 * @param plane The height of the coordinates {@link #x} and {@link #y}.
	 */
	public Tile(int x, int y, int plane) {
		this.x = (short) x;
		this.y = (short) y;
		this.plane = (byte) plane;
	}
	
	/**
	 * Constructs a new {@code Tile.java} based on local coordinates and a region ID.
	 * @param localX	The local X coordinate inside the region
	 * @param localY	The local Y coordinate inside the region
	 * @param plane		The height of the coordinates {@link #x} and {@link #y}.
	 * @param regionID	The ID of the region
	 */
	public Tile(int localX, int localY, int plane, int regionID) {
		this.x = (short) (localX + (((regionID >> 8) & 0xff) << 6));
		this.y = (short) (localY + ((regionID & 0xff) << 6));
		this.plane = (byte) plane;
	}

	/**
	 * Constructs a new {@code Tile.java}.
	 * @param tile The tile to trasfer.
	 */
	public Tile(Tile tile) {
		this.x = tile.x;
		this.y = tile.y;
		this.plane = tile.plane;
	}

	/**
	 * Constructs a new {@code Tile.java}.
	 * @param hash The <b>tile's</b> hash on the grid.
	 */
	public Tile(int hash) {
		this.x = (short) (hash >> 14 & 0x3fff);
		this.y = (short) (hash & 0x3fff);
		this.plane = (byte) (hash >> 28);
	}
	
	/**
	 * Manipulates the coordinates of this {@link Tile}.
	 * @param xOffset The amount of <b>tiles</b> in the horizontal direction to manipulate.
	 * @param yOffset The amount of <b>tiles</b> in the vertical direction to manipulate.
	 * @param planeOffset The amount of <b>tiles</b> in height to manipulate.
	 * @return A new {@link Tile} with the manipulated set of coordinates.
	 */
	public Tile manipulate(int xOffset, int yOffset, int planeOffset) {
		x += xOffset;
		y += yOffset;
		plane += planeOffset;
		return new Tile(x, y, plane);
	}

	/**
	 * Sets the coordinates of this {@link Tile} to a given {@link Tile}.
	 * @param tile The {@link Tile} to copy.
	 */
	public void copy(Tile tile) {
		copy(tile.x, tile.y, tile.plane);
	}

	/**
	 * Sets the coordinates of this {@link Tile} to a given <b>X</b>, <b>Y</b>
	 * and <b>height</b>.
	 * @param x The X coordinate to set.
	 * @param y The Y coordinate to set.
	 * @param plane The height to set.
	 */
	public void copy(int x, int y, int plane) {
		this.x = (short) x;
		this.y = (short) y;
		this.plane = (byte) plane;
	}

	/**
	 * Returns the X coordinate in the corresponding {@link Region}.
	 * @return The X in the {@link Region} of this {@link Tile}.
	 */
	public int getXInRegion() {
		return x & 0x3F;
	}

	/**
	 * Returns the Y coordinate in the corresponding {@link Region}.
	 * @return The Y in the {@link Region} of this {@link Tile}.
	 */
	public int getYInRegion() {
		return y & 0x3F;
	}

	/**
	 * Returns the X coordinate of this {@link Tile}
	 * @return The X coordinate.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Returns the Y coordinate of this {@link Tile}
	 * @return The Y coordinate.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Returns the plane of this {@link Tile}.
	 * @return The <b>height</b>, or <b>plane</b> of this {@link Tile}.
	 */
	public int getPlane() {
		if (plane > 3)
			return 3;
		return plane;
	}

	/**
	 * Returns the X coordinate corresponding to the {@link Chunk} of this {@link Tile}.
	 * @return The X in the {@link Chunk} of this {@link Tile}.
	 */
	public int getChunkX() {
		return (x >> 3);
	}

	/**
	 * Returns the Y coordinate corresponding to the {@link Chunk} of this {@link Tile}.
	 * @return The Y in the {@link Chunk} of this {@link Tile}.
	 */
	public int getChunkY() {
		return (y >> 3);
	}

	/**
	 * Returns the X coordinate in the corresponding {@link Region}.
	 * @return The X in the {@link Region} of this {@link Tile}.
	 * @see {@link #getXInRegion()}, not to be confused with this.
	 */
	public int getRegionX() {
		return (x >> 6);
	}

	/**
	 * Returns the Y coordinate in the corresponding {@link Region}.
	 * @return The Y in the {@link Region} of this {@link Tile}.
	 * @see {@link #getYInRegion()}, not to be confused with this.
	 */
	public int getRegionY() {
		return (y >> 6);
	}

	/**
	 * Returns the ID of the {@link Region} that this {@link Tile} is located on.
	 * @return The {@link Region} ID of this {@link Tile}.
	 */
	public int getRegionID() {
		return ((getRegionX() << 8) + getRegionY());
	}

	/**
	 * Returns the <b>local</b> X coordinate of a given {@link Tile} compared to this.
	 * @param tile The tile to compare
	 * @param mapSize The size of the map to be delt with
	 * @return The local X coordinate.
	 */
	public int getLocalX(Tile tile, int mapSize) {
		return x - 8 * (tile.getChunkX() - (LandscapeRepository.REGION_SIZES[mapSize] >> 4));
	}

	/**
	 * Returns the <b>local</b> Y coordinate of a given {@link Tile} compared to this.
	 * @param tile The tile to compare
	 * @param mapSize The size of the map to be delt with
	 * @return The local Y coordinate.
	 */
	public int getLocalY(Tile tile, int mapSize) {
		return y - 8 * (tile.getChunkY() - (LandscapeRepository.REGION_SIZES[mapSize] >> 4));
	}

	/**
	 * Returns the <i>16 bit</b> local X coordinate of this {@link Tile}.
	 * @return The </i>16 bit</b> local X coordinate.
	 */
	public int get16BitLocalX() {
		return get16BitLocalX(this);
	}

	/**
	 * Returns the <i>16 bit</i> local X coordinate of a given {@link Tile} compared to this.
	 * @param tile The tile to compare
	 * @return The local X coordinate.
	 */
	public int get16BitLocalX(Tile tile) {
		return x - ((tile.getChunkX() - 6) << 3);
	}

	/**
	 * Returns the <i>16 bit</b> local Y coordinate of this {@link Tile}.
	 * @return The </i>16 bit</b> local Y coordinate.
	 */
	public int get16BitLocalY() {
		return get16BitLocalY(this);
	}

	/**
	 * Returns the <i>16 bit</i> local Y coordinate of a given {@link Tile} compared to this.
	 * @param tile The tile to compare
	 * @return The local Y coordinate.
	 */
	public int get16BitLocalY(Tile tile) {
		return y - ((tile.getChunkY() - 6) << 3);
	}

	/**
	 * Returns the local X coordinate of this compared to a given {@link Tile}.
	 * @param tile The {@link Tile} to compare.
	 * @return The local X coordinate.
	 */
	public int getLocalX(Tile tile) {
		return getLocalX(tile, 0);
	}

	/**
	 * Returns the local Y coordinate of this compared to a given {@link Tile}.
	 * @param tile The {@link Tile} to compare.
	 * @return The local Y coordinate.
	 */
	public int getLocalY(Tile tile) {
		return getLocalY(tile, 0);
	}

	/**
	 * Returns the local X coordinate of this {@link Tile}.
	 * @return The local X coordinate.
	 */
	public int getLocalX() {
		return getLocalX(this);
	}
	
	/**
	 * Returns the local Y coordinate of this {@link Tile}.
	 * @return The local Y coordinate.
	 */
	public int getLocalY() {
		return getLocalY(this);
	}

	/**
	 * Returns the hash of the {@link Region} that this {@link Tile} is located on.
	 * @return The corresponding {@link Region} hash of this {@link Tile}.
	 */
	public int getRegionHash() {
		return getRegionY() + (getRegionX() << 8) + (plane << 16);
	}

	/**
	 * Returns the unique <b>hash</b> of this {@link Tile}.
	 * @return The <b>hash</b>.
	 */
	public int getTileHash() {
		return y + (x << 14) + (plane << 28);
	}

	/**
	 * Checks if a given {@link Tile} is within a certain distance of this {@link Tile}.
	 * @param tile The {@link Tile} being checked
	 * @param distance The distance, in tiles, to check.
	 * @return {@code True} if so; {@code False} otherwise.
	 */
	public boolean withinDistance(Tile tile, int distance) {
		if (tile.plane != plane)
			return false;
		int deltaX = tile.x - x, deltaY = tile.y - y;
		return deltaX <= distance && deltaX >= -distance && deltaY <= distance && deltaY >= -distance;
	}

	/**
	 * Checks if a given {@link Tile} is within a certain distance of this {@link Tile}.
	 * @param tile The {@link Tile} being checked
	 * @return {@code True} if so; {@code False} otherwise.
	 */
	public boolean withinDistance(Tile tile) {
		if (tile.plane != plane)
			return false;
		return Math.abs(tile.x - x) <= 14 && Math.abs(tile.y - y) <= 14;
	}

	/**
	 * Returns the coordinate face {@link Tile} of a given {@link Node} size.
	 * @param sizeX The X coordinate size of the given {@link Node}.
	 * @return The coordinate face X.
	 */
	public int getCoordFaceX(int sizeX) {
		return getCoordFaceX(sizeX, -1, -1);
	}

	/**
	 * Returns the coordinate face {@link Tile} of a given {@link Node} size.
	 * @param x The X coordinate of the given {@link Node}.
	 * @param sizeX The X coordinate size of the given {@link Node}.
	 * @param sizeY The Y coordinate size of the given {@link Node}.
	 * @param rotation The rotation given {@link Node}.
	 * @return The coordinate face X.
	 */
	public static final int getCoordFaceX(int x, int sizeX, int sizeY, int rotation) {
		return x + ((rotation == 1 || rotation == 3 ? sizeY : sizeX) - 1) / 2;
	}

	/**
	 * Returns the coordinate face {@link Tile} of a given {@link Node} size.
	 * @param y The Y coordinate of the given {@link Node}.
	 * @param sizeX The X coordinate size of the given {@link Node}.
	 * @param sizeY The Y coordinate size of the given {@link Node}.
	 * @param rotation The rotation given {@link Node}.
	 * @return The coordinate face Y.
	 */
	public static final int getCoordFaceY(int y, int sizeX, int sizeY, int rotation) {
		return y + ((rotation == 1 || rotation == 3 ? sizeX : sizeY) - 1) / 2;
	}

	/**
	 * Returns the coordinate face {@link Tile} of a given {@link Node} size.
	 * @param sizeX The X coordinate size of the given {@link Node}.
	 * @param sizeY The Y coordinate size of the given {@link Node}.
	 * @param rotation The rotation given {@link Node}.
	 * @return The coordinate face X.
	 */
	public int getCoordFaceX(int sizeX, int sizeY, int rotation) {
		return x + ((rotation == 1 || rotation == 3 ? sizeY : sizeX) - 1) / 2;
	}

	/**
	 * Returns the coordinate face {@link Tile} of a given {@link Node} size.
	 * @param sizeY The Y coordinate size of the given {@link Node}.
	 * @return The coordinate face Y.
	 */
	public int getCoordFaceY(int sizeY) {
		return getCoordFaceY(-1, sizeY, -1);
	}

	/**
	 * Returns the coordinate face {@link Tile} of a given {@link Node} size.
	 * @param sizeX The X coordinate size of the given {@link Node}.
	 * @param sizeY The Y coordinate size of the given {@link Node}.
	 * @param rotation The rotation given {@link Node}.
	 * @return The coordinate face Y.
	 */
	public int getCoordFaceY(int sizeX, int sizeY, int rotation) {
		return y + ((rotation == 1 || rotation == 3 ? sizeX : sizeY) - 1) / 2;
	}

	/**
	 * Copies this {@link Tile} to a new {@link Tile} but with an added X Y and Z offset.
	 * @param x The X to add.
	 * @param y The Y to add.
	 * @param plane The height to add.
	 * @return The new copied {@link Tile}.
	 */
	public Tile copyNew(int x, int y, int plane) {
		return new Tile(this.x + x, this.y + y, this.plane + plane);
	}

	/**
	 * Checks if the map of a given {@link Tile} is different than this.
	 * @param tile The {@link Tile} to compare.
	 * @return {@code True} if so; {@code False} otherwise.
	 */
	public boolean differentMap(Tile tile) {
		return distance(getChunkX(), getChunkY(), tile.getChunkX(), tile.getChunkY()) >= 4;
	}

	/**
	 * Returns the distance between the given X and Y coordinates.
	 * @param x The X to compare.
	 * @param y The Y to compare.
	 * @param x2 The victim X to compare.
	 * @param y2 The victim Y to compare.
	 * @return The distance between the two coordinate sets.
	 */
	private final double distance(int x, int y, int x2, int y2) {
		return Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2));
	}

	/**
	 * Returns the distance between a given {@link Tile}.
	 * @param tile The {@link Tile} to check.
	 * @return The distance between the two tiles.
	 */
	public int distance(Tile tile) {
		return (int) distance(x, y, tile.x, tile.y);
	}

	/**
	 * Checks if this {@link Tile} matches a given {@link Tile}, only checking X and Y and not height.
	 * @param tile The {@link Tile} to compare.
	 * @return {@code True} if so; {@code False} otherwise.
	 */
	public boolean lazyMatches(Tile tile) {
		return x == tile.x && y == tile.y;
	}
	
	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!(other instanceof Tile)) {
			return false;
		}
		Tile t = (Tile) other;
		return x == t.x && y == t.y && plane == t.plane;
	}

	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "X: " + x + ", Y: " + y + ", Z: " + plane + " (REGION: " + getRegionID() + ")";
	}

	/**
	 * Edits a tile.
	 * @param tile The tile to edit.
	 * @param xOff The xOff.
	 * @param yOff The yOff.
	 * @param zOff The zOff
	 * @return The tile.
	 */
	public static Tile edit(Tile tile, byte xOff, byte yOff, int zOff) {
		return new Tile(tile.getX() + xOff, tile.getY() + yOff, tile.getPlane() + zOff);
	}
	
	/**
	 * Checks if a tile is next to another given tile.
	 * @param tile The tile to check.
	 * @return True if so; false otherwise.
	 */
	public boolean nextTo(Tile tile) {
		return ((tile.getX() - 1) == getX()) || ((tile.getY() - 1) == getY()) || ((tile.getX() + 1) == getX()) || ((tile.getY() + 1) == getY());
	}
}
