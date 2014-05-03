package org.virtue.game.logic.route;

import org.virtue.game.logic.node.entity.player.Player;

/**
 * @author Mangis
 * 
 */
public class RouteFinder {

	public static final int WALK_ROUTEFINDER = 0;
	private static int lastUsed;

	public static int findRoute(int type, int srcX, int srcY, int srcZ, int srcSizeXY, RouteStrategy strategy, boolean findAlternative) {
		switch (lastUsed = type) {
		case WALK_ROUTEFINDER:
			return WalkRouteFinder.findRoute(srcX, srcY, srcZ, srcSizeXY, strategy, findAlternative);
		default:
			throw new RuntimeException("Unknown routefinder type.");
		}
	}

	public static int[] getLastPathBufferX() {
		switch (lastUsed) {
		case WALK_ROUTEFINDER:
			return WalkRouteFinder.getLastPathBufferX();
		default:
			throw new RuntimeException("Unknown routefinder type.");
		}
	}

	public static int[] getLastPathBufferY() {
		switch (lastUsed) {
		case WALK_ROUTEFINDER:
			return WalkRouteFinder.getLastPathBufferY();
		default:
			throw new RuntimeException("Unknown routefinder type.");
		}
	}

	public static boolean lastIsAlternative() {
		switch (lastUsed) {
		case WALK_ROUTEFINDER:
			return WalkRouteFinder.lastIsAlternative();
		default:
			throw new RuntimeException("Unknown routefinder type.");
		}
	}

	public static int[][] walkTo(Player player, int x, int y) {
		int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getTile().getX(), player.getTile().getY(), player.getTile().getPlane(), player.getSize() /* Size */, new FixedTileStrategy(x, y), true);
		int[] bufferX = RouteFinder.getLastPathBufferX();
		int[] bufferY = RouteFinder.getLastPathBufferY();
		if (player.getTile().getX() == x && player.getTile().getY() == y) {
			return null;
		}
		if (steps <= 0) {
			return null;
		}
		/*player.setFirstStep(true);
		player.resetWalkSteps();
		for (int i = steps - 1; i >= 0; i--) {
			if (!player.addWalkSteps(bufferX[i], bufferY[i], 25, true)) {
				break;
			}
		}*/
		return null;
	}
}
