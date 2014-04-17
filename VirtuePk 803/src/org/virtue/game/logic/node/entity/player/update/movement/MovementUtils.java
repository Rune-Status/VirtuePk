package org.virtue.game.logic.node.entity.player.update.movement;

import org.virtue.game.logic.node.entity.region.Tile;

/**
 * @author Taylor
 * @version 1.0
 */
public class MovementUtils {
	
	/**
	 * Represents the direction delta X data.
	 */
	public static final byte[] DIRECTION_DELTA_X = new byte[] { -1, 0, 1, -1, 1, -1, 0, 1 };
	
	/**
	 * Represents the direction delta Y data.
	 */
	public static final byte[] DIRECTION_DELTA_Y = new byte[] { 1, 1, 1, 0, 0, -1, -1, -1 };
	
	/**
	 * Returns the distance from one tile to another.
	 * @param t1 The tile one.
	 * @param t2 The tile two.
	 * @return The distance in tiles.
	 */
	public static final int getDistance(Tile t1, Tile t2) {
		return getDistance(t1.getX(), t1.getY(), t2.getX(), t2.getY());
	}
	
	/**
	 * Returns the distance from one set of coordinates to another.
	 * @param coordX1 The x1.
	 * @param coordY1 The y1.
	 * @param coordX2 The x2.
	 * @param coordY2 The y2.
	 * @return The distance in tiles.
	 */
	public static final int getDistance(int coordX1, int coordY1, int coordX2, int coordY2) {
		int deltaX = coordX2 - coordX1;
		int deltaY = coordY2 - coordY1;
		return ((int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
	}

	/**
	 * Returns the NPC move direction
	 * @param dd The direction delta
	 * @return The direction.
	 */
	public static int getNpcMoveDirection(int dd) {
		if (dd < 0) {
			return -1;
		}
		return getNpcMoveDirection(DIRECTION_DELTA_X[dd], DIRECTION_DELTA_Y[dd]);
	}

	/**
	 * Returns the NPC move direction.
	 * @param dx The delta X.
	 * @param dy The delta Y.
	 * @return The direction.
	 */
	public static int getNpcMoveDirection(int dx, int dy) {
		if (dx == 0 && dy > 0)
			return 0;
		if (dx > 0 && dy > 0)
			return 1;
		if (dx > 0 && dy == 0)
			return 2;
		if (dx > 0 && dy < 0)
			return 3;
		if (dx == 0 && dy < 0)
			return 4;
		if (dx < 0 && dy < 0)
			return 5;
		if (dx < 0 && dy == 0)
			return 6;
		if (dx < 0 && dy > 0)
			return 7;
		return -1;
	}

	/**
	 * Returns a coordintae offset near a radius.
	 * @param size The size of the radius.
	 * @return The set of coordinates.
	 */
	public static final int[][] getCoordOffsetsNear(int size) {
		int[] xs = new int[4 + (4 * size)];
		int[] xy = new int[xs.length];
		xs[0] = -size;
		xy[0] = 1;
		xs[1] = 1;
		xy[1] = 1;
		xs[2] = -size;
		xy[2] = -size;
		xs[3] = 1;
		xy[2] = -size;
		for (int fakeSize = size; fakeSize > 0; fakeSize--) {
			xs[(4 + ((size - fakeSize) * 4))] = -fakeSize + 1;
			xy[(4 + ((size - fakeSize) * 4))] = 1;
			xs[(4 + ((size - fakeSize) * 4)) + 1] = -size;
			xy[(4 + ((size - fakeSize) * 4)) + 1] = -fakeSize + 1;
			xs[(4 + ((size - fakeSize) * 4)) + 2] = 1;
			xy[(4 + ((size - fakeSize) * 4)) + 2] = -fakeSize + 1;
			xs[(4 + ((size - fakeSize) * 4)) + 3] = -fakeSize + 1;
			xy[(4 + ((size - fakeSize) * 4)) + 3] = -size;
		}
		return new int[][] { xs, xy };
	}

	/**
	 * Returns the direction to face.
	 * @param xOffset The x offset.
	 * @param yOffset The y offset.
	 * @return The direction to face.
	 */
	public static final int getFaceDirection(int xOffset, int yOffset) {
		return ((int) (Math.atan2(-xOffset, -yOffset) * 2607.5945876176133)) & 0x3fff;
	}

	/**
	 * Returns the move direction.
	 * @param xOffset The x offset.
	 * @param yOffset The y offset.
	 * @return The direction to move.
	 */
	public static final int getMoveDirection(int xOffset, int yOffset) {
		if (xOffset < 0) {
			if (yOffset < 0)
				return 5;
			else if (yOffset > 0)
				return 0;
			else
				return 3;
		} else if (xOffset > 0) {
			if (yOffset < 0)
				return 7;
			else if (yOffset > 0)
				return 2;
			else
				return 4;
		} else {
			if (yOffset < 0)
				return 6;
			else if (yOffset > 0)
				return 1;
			else
				return -1;
		}
	}

	/**
	 * Returns the hash map size.
	 * @param size The size.
	 * @return The size.
	 */
	public static int getHashMapSize(int size) {
		size--;
		size |= size >>> -1810941663;
		size |= size >>> 2010624802;
		size |= size >>> 10996420;
		size |= size >>> 491045480;
		size |= size >>> 1388313616;
		return 1 + size;
	}

	/**
	 * Returns a player's walking direction.
	 * @param dx The delta x.
	 * @param dy The delta y.
	 * @return The direction.
	 */
	public static int getPlayerWalkingDirection(int dx, int dy) {
		if (dx == -1 && dy == -1) {
			return 0;
		}
		if (dx == 0 && dy == -1) {
			return 1;
		}
		if (dx == 1 && dy == -1) {
			return 2;
		}
		if (dx == -1 && dy == 0) {
			return 3;
		}
		if (dx == 1 && dy == 0) {
			return 4;
		}
		if (dx == -1 && dy == 1) {
			return 5;
		}
		if (dx == 0 && dy == 1) {
			return 6;
		}
		if (dx == 1 && dy == 1) {
			return 7;
		}
		return -1;
	}
	
	/**
	 * Returns a player's running direction.
	 * @param dx The delta x.
	 * @param dy The delta y.
	 * @return The direction.
	 */
	public static int getPlayerRunningDirection(int dx, int dy) {
		if (dx == -2 && dy == -2)
			return 0;
		if (dx == -1 && dy == -2)
			return 1;
		if (dx == 0 && dy == -2)
			return 2;
		if (dx == 1 && dy == -2)
			return 3;
		if (dx == 2 && dy == -2)
			return 4;
		if (dx == -2 && dy == -1)
			return 5;
		if (dx == 2 && dy == -1)
			return 6;
		if (dx == -2 && dy == 0)
			return 7;
		if (dx == 2 && dy == 0)
			return 8;
		if (dx == -2 && dy == 1)
			return 9;
		if (dx == 2 && dy == 1)
			return 10;
		if (dx == -2 && dy == 2)
			return 11;
		if (dx == -1 && dy == 2)
			return 12;
		if (dx == 0 && dy == 2)
			return 13;
		if (dx == 1 && dy == 2)
			return 14;
		if (dx == 2 && dy == 2)
			return 15;
		return -1;
	}
}
