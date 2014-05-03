package org.virtue.game.logic.route;

import org.virtue.game.logic.item.GroundItem;
import org.virtue.game.logic.node.entity.Entity;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.object.RS3Object;
import org.virtue.game.logic.region.Tile;

public class RouteEvent {

	/**
	 * Object to which we are finding the route.
	 */
	private Object object;
	/**
	 * The event instance.
	 */
	private Runnable event;
	/**
	 * Whether we also run on alternative.
	 */
	private boolean alternative;
	/**
	 * Contains last route strategies.
	 */
	private RouteStrategy[] last;

	public RouteEvent(Object object, Runnable event) {
		this(object, event, false);
	}

	public RouteEvent(Object object, Runnable event, boolean alternative) {
		this.object = object;
		this.event = event;
		this.alternative = alternative;
	}

	public boolean processEvent(final Player player) {
		/*if (!simpleCheck(player)) {
			player.getWriter().sendGameMessage("You can't reach that.");
			player.getWriter().sendResetMinimapFlag();
			return true;
		}
		RouteStrategy[] strategies = generateStrategies();
		if (last != null && match(strategies, last) && player.hasWalkSteps())
			return false;
		else if (last != null && match(strategies, last) && !player.hasWalkSteps()) {
			for (int i = 0; i < strategies.length; i++) {
				RouteStrategy strategy = strategies[i];
				int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getPlane(), player.getSize(), strategy, i == (strategies.length - 1));
				if (steps == -1)
					continue;
				if ((!RouteFinder.lastIsAlternative() && steps <= 0) || alternative) {
					if (alternative)
						player.getWriter().sendResetMinimapFlag();
					event.run();
					return true;
				}
			}
			player.getWriter().sendGameMessage("You can't reach that.");
			player.getWriter().sendResetMinimapFlag();
			return true;
		} else {
			last = strategies;
			for (int i = 0; i < strategies.length; i++) {
				RouteStrategy strategy = strategies[i];
				int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getPlane(), player.getSize(), strategy, i == (strategies.length - 1));
				if (steps == -1)
					continue;
				if ((!RouteFinder.lastIsAlternative() && steps <= 0)) {
					if (alternative)
						player.getWriter().sendResetMinimapFlag();
					event.run();
					return true;
				}
				int[] bufferX = RouteFinder.getLastPathBufferX();
				int[] bufferY = RouteFinder.getLastPathBufferY();

				Tile last = new Tile(bufferX[0], bufferY[0], player.getLocation().getPlane());
				player.resetWalkSteps();
				player.getWriter().sendMinimapFlag(last.getLocalX(player.getLastLoadedLocation()), last.getLocalY(player.getLastLoadedLocation()));

				// if (player.getFreezeDelay() > 0)
				// return false;

				for (int step = steps - 1; step >= 0; step--) {
					if (!player.addWalkSteps(bufferX[step], bufferY[step], 25, true)) {
						break;
					}
				}
				return false;
			}
			player.getWriter().sendGameMessage("You can't reach that.");
			player.getWriter().sendResetMinimapFlag();
			return true;
		}*/
		return false;
	}

	private boolean simpleCheck(Player player) {
		if (object instanceof Entity) {
			return player.getTile().getPlane() == ((Entity) object).getTile().getPlane();
		} else if (object instanceof RS3Object) {
			return player.getTile().getPlane() == ((RS3Object) object).getTile().getPlane();
		} else if (object instanceof GroundItem) {
			return player.getTile().getPlane() == ((GroundItem) object).getTile().getPlane();
		} else {
			throw new RuntimeException(object + " is not instanceof any reachable entity.");
		}
	}

	private RouteStrategy[] generateStrategies() {
		if (object instanceof Entity) {
			return new RouteStrategy[] { new EntityStrategy((Entity) object) };
		} else if (object instanceof RS3Object) {
			return new RouteStrategy[] { new ObjectStrategy((RS3Object) object) };
		} else if (object instanceof GroundItem) {
			GroundItem item = (GroundItem) object;
			return new RouteStrategy[] { new FixedTileStrategy(item.getTile().getX(), item.getTile().getY()), new GroundItemStrategy(item) };
		} else {
			throw new RuntimeException(object + " is not instanceof any reachable entity.");
		}
	}

	private boolean match(RouteStrategy[] a1, RouteStrategy[] a2) {
		if (a1.length != a2.length)
			return false;
		for (int i = 0; i < a1.length; i++)
			if (!a1[i].equals(a2[i]))
				return false;
		return true;
	}

}