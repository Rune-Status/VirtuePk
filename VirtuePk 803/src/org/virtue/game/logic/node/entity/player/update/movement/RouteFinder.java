package org.virtue.game.logic.node.entity.player.update.movement;

import org.virtue.game.logic.World;
import org.virtue.game.logic.region.Flags;
import org.virtue.game.logic.region.RegionManager;
import org.virtue.game.logic.region.Tile;

public class RouteFinder {
	
	private static final int GRAPH_SIZE = 128;
	private static final int QUEUE_SIZE = (GRAPH_SIZE * GRAPH_SIZE) / 4;
	
	private static final int DIR_NORTH = 0x1;
	private static final int DIR_EAST = 0x2;
	private static final int DIR_SOUTH = 0x4;
	private static final int DIR_WEST = 0x8;
	
	private static final int ALTERNATIVE_ROUTE_MAX_DISTANCE = 100;
	private static final int ALTERNATIVE_ROUTE_RANGE = 10;
	
	private int[][] directions = new int[GRAPH_SIZE][GRAPH_SIZE];
	private int[][] distances = new int[GRAPH_SIZE][GRAPH_SIZE];
	private int[][] routeFinderArray = new int[QUEUE_SIZE][2];
	private RegionManager regionManager = World.getWorld().getRegionManager();	

	private int exitX = -1;
	private int exitY = -1;
	private boolean isAlternative;
	private int destSizeX, destSizeY;
	
	private Tile startPoint, target;
	
	public RouteFinder (Tile start) {
		this.startPoint = start;
	}
	
	public void setTarget (Tile target, int sizeX, int sizeY) {
		this.target = target;
		this.destSizeX = sizeX;
		this.destSizeY = sizeY;
	}
	
	public int calculateRoute (int srcSizeXY, boolean findAlternative) {
		int srcX = startPoint.getX();
		int srcY = startPoint.getY();
		//System.out.println("From: "+startPoint+", to: "+target+", size: "+srcSizeXY);
		isAlternative = false;
		for (int x = 0; x < GRAPH_SIZE; x++) {
			for (int y = 0; y < GRAPH_SIZE; y++) {
				directions[x][y] = 0;
				distances[x][y] = 99999999;
			}
		}

		/*if (debug) {
			long start = System.nanoTime();
			transmitClipData(srcX, srcY, srcZ);
			debug_transmittime = System.nanoTime() - start;
		} else {
			transmitClipData(srcX, srcY, srcZ);
		}*/

		boolean found = false;
		switch (srcSizeXY) {
		case 1:
			found = calculate1(srcX, srcY);
			break;
		case 2:
			//found = performCalculationS2(srcX, srcY);
			break;
		default:
			//found = performCalculationSX(srcX, srcY, srcSizeXY);
			break;
		}

		if (!found && !findAlternative) {
			return -1;
		}

		int graphBaseX = srcX - (GRAPH_SIZE / 2);
		int graphBaseY = srcY - (GRAPH_SIZE / 2);
		int endX = exitX;
		int endY = exitY;

		if (!found && findAlternative) {
			isAlternative = true;
			int lowestCost = Integer.MAX_VALUE;
			int lowestDistance = Integer.MAX_VALUE;

			int approxDestX = target.getX();
			int approxDestY = target.getY();

			for (int checkX = (approxDestX - ALTERNATIVE_ROUTE_RANGE); checkX <= (approxDestX + ALTERNATIVE_ROUTE_RANGE); checkX++) {
				for (int checkY = (approxDestY - ALTERNATIVE_ROUTE_RANGE); checkY <= (approxDestY + ALTERNATIVE_ROUTE_RANGE); checkY++) {
					int graphX = checkX - graphBaseX;
					int graphY = checkY - graphBaseY;
					if (graphX < 0 || graphY < 0 || graphX >= GRAPH_SIZE || graphY >= GRAPH_SIZE || distances[graphX][graphY] >= ALTERNATIVE_ROUTE_MAX_DISTANCE) {
						continue;
					}
					int deltaX = 0;
					int deltaY = 0;
					if (approxDestX <= checkX) {
						deltaX = 1 - approxDestX - (destSizeX - checkX);
					} else {
						deltaX = approxDestX - checkX;
					}
					if (approxDestY <= checkY) {
						deltaY = 1 - approxDestY - (destSizeY - checkY);
					} else {
						deltaY = approxDestY - checkY;
					}

					int cost = (deltaX * deltaX) + (deltaY * deltaY);
					if (cost < lowestCost || (cost <= lowestCost && distances[graphX][graphY] < lowestDistance)) {
						lowestCost = cost;
						lowestDistance = distances[graphX][graphY];
						endX = checkX;
						endY = checkY;
					}
				}
			}

			if (lowestCost == Integer.MAX_VALUE || lowestDistance == Integer.MAX_VALUE) {
				return -1;
			}
		}

		if (endX == srcX && endY == srcY) {
			return 0;
		}

		int steps = 0;
		int traceX = endX;
		int traceY = endY;
		int direction = directions[traceX - graphBaseX][traceY - graphBaseY];
		int lastwritten = direction;
		routeFinderArray[steps][0] = traceX;
		routeFinderArray[steps++][1] = traceY;
		//System.out.println("EndX="+endX+", endY="+endY+", startX="+srcX+", startY="+srcY);
		//System.out.print("Directions: ");
		while (traceX != srcX || traceY != srcY) {
			//if (lastwritten != direction) {
				routeFinderArray[steps][0] = traceX;
				routeFinderArray[steps++][1] = traceY;
				lastwritten = direction;
			//}

			if ((direction & DIR_EAST) != 0) {
				traceX++;
			} else if ((direction & DIR_WEST) != 0) {
				traceX--;
			}

			if ((direction & DIR_NORTH) != 0) {
				traceY++;
			} else if ((direction & DIR_SOUTH) != 0) {
				traceY--;
			}
			//System.out.print(direction+", ");
			direction = directions[traceX - graphBaseX][traceY - graphBaseY];
		}
		//System.out.println("total="+steps);
		return steps;
	}
	
	public int[][] getSteps () {
		return routeFinderArray;
	}
	
	private boolean calculate1 (int startX, int startY) {
		int currentX = startX;
		int currentY = startY;
		int currentGraphX = (GRAPH_SIZE/2);
		int currentGraphY = (GRAPH_SIZE/2);
		int graphBaseX = startX - currentGraphX;
		int graphBaseY = startY - currentGraphY;
		int plane = startPoint.getPlane();
		directions[currentGraphX][currentGraphY] = 99;
		distances[currentGraphX][currentGraphY] = 0;
		int write = 0;
		int read = 0;
		routeFinderArray[write][0] = currentX;
		routeFinderArray[write++][1] = currentY;
		while (read != write) {
			currentX = routeFinderArray[read][0];
			currentY = routeFinderArray[read][1];
			read = 1 + read & 0xfff;
			currentGraphX = currentX - graphBaseX;
			currentGraphY = currentY - graphBaseY;
			//int clipX = 
			//System.out.println("1currentGraphX="+currentGraphX+", currentGraphY="+currentGraphY+", write="+write);
			if (canExit(currentX, currentY, graphBaseX, graphBaseY)) {
				//System.out.println("2currentGraphX="+currentGraphX+", currentGraphY="+currentGraphY+", write="+write);
				exitX = currentX;
				exitY = currentY;
				return true;
			}
			int nextDistance = distances[currentGraphX][currentGraphY] + 1;
			if (currentGraphX > 0 && directions[currentGraphX - 1][currentGraphY] == 0
					&& (regionManager.getMask(plane, currentX - 1, currentY) & 
							(Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_EAST_BLOCKSWALK_ALTERNATIVE)) == 0) {
				routeFinderArray[write][0] = currentX - 1;
				routeFinderArray[write][1] = currentY;
				write = 1 + write & 0xfff;
				directions[currentGraphX - 1][currentGraphY] = DIR_EAST;
				distances[currentGraphX - 1][currentGraphY] = nextDistance;
			}
			if (currentGraphX < (GRAPH_SIZE - 1) && directions[currentGraphX + 1][currentGraphY] == 0
					&& (regionManager.getMask(plane, currentX + 1, currentY) & 
							(Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE)) == 0) {
				routeFinderArray[write][0] = currentX + 1;
				routeFinderArray[write][1] = currentY;
				write = write + 1 & 0xfff;
				directions[currentGraphX + 1][currentGraphY] = DIR_WEST;
				distances[currentGraphX + 1][currentGraphY] = nextDistance;
			}
			if (currentGraphY > 0 && directions[currentGraphX][currentGraphY - 1] == 0
					&& (regionManager.getMask(plane, currentX, currentY - 1) & 
							(Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_NORTH_BLOCKSWALK_ALTERNATIVE)) == 0) {
				routeFinderArray[write][0] = currentX;
				routeFinderArray[write][1] = currentY - 1;
				write = 1 + write & 0xfff;
				directions[currentGraphX][currentGraphY - 1] = DIR_NORTH;
				distances[currentGraphX][currentGraphY - 1] = nextDistance;
			}
			if (currentGraphY < (GRAPH_SIZE - 1) && directions[currentGraphX][currentGraphY + 1] == 0
					&& (regionManager.getMask(plane, currentX, 1 + currentY) & 
							(Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE)) == 0) {
				routeFinderArray[write][0] = currentX;
				routeFinderArray[write][1] = 1 + currentY;
				write = 1 + write & 0xfff;
				directions[currentGraphX][currentGraphY + 1] = DIR_SOUTH;
				distances[currentGraphX][currentGraphY + 1] = nextDistance;
			}
			if (currentGraphX > 0 && currentGraphY > 0 && directions[currentGraphX - 1][currentGraphY - 1] == 0
					&& (regionManager.getMask(plane, currentX - 1, currentY - 1) & 
							(Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_NORTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_EAST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_NORTHEAST_BLOCKSWALK_ALTERNATIVE)) == 0
					&& (regionManager.getMask(plane, currentX - 1, currentY) & 
							(Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_EAST_BLOCKSWALK_ALTERNATIVE)) == 0
					&& (regionManager.getMask(plane, currentX, currentY - 1) & 
							(Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_NORTH_BLOCKSWALK_ALTERNATIVE)) == 0) {
				routeFinderArray[write][0] = currentX - 1;
				routeFinderArray[write][1] = currentY - 1;
				write = write + 1 & 0xfff;
				directions[currentGraphX - 1][currentGraphY - 1] = DIR_NORTH | DIR_EAST;
				distances[currentGraphX - 1][currentGraphY - 1] = nextDistance;
			}
			if (currentGraphX < (GRAPH_SIZE - 1) && currentGraphY > 0 && directions[currentGraphX + 1][currentGraphY - 1] == 0
					&& (regionManager.getMask(plane, currentX + 1, currentY - 1) & 
							(Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_NORTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_NORTHWEST_BLOCKSWALK_ALTERNATIVE)) == 0
					&& (regionManager.getMask(plane, currentX + 1, currentY) & 
							(Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE)) == 0
					&& (regionManager.getMask(plane, currentX, currentY - 1) & 
							(Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_NORTH_BLOCKSWALK_ALTERNATIVE)) == 0) {
				routeFinderArray[write][0] = currentX + 1;
				routeFinderArray[write][1] = currentY - 1;
				write = write + 1 & 0xfff;
				directions[currentGraphX + 1][currentGraphY - 1] = DIR_NORTH | DIR_WEST;
				distances[currentGraphX + 1][currentGraphY - 1] = nextDistance;
			}
			if (currentGraphX > 0 && currentGraphY < (GRAPH_SIZE - 1) && directions[currentGraphX - 1][currentGraphY + 1] == 0
					&& (regionManager.getMask(plane, currentX - 1, 1 + currentY) & 
							(Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_EAST_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_SOUTHEAST_BLOCKSWALK_ALTERNATIVE)) == 0
					&& (regionManager.getMask(plane, currentX - 1, currentY) & 
							(Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_EAST_BLOCKSWALK_ALTERNATIVE)) == 0
					&& (regionManager.getMask(plane, currentX, 1 + currentY) & 
							(Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE)) == 0) {
				routeFinderArray[write][0] = currentX - 1;
				routeFinderArray[write][1] = currentY + 1;
				write = write + 1 & 0xfff;
				directions[currentGraphX - 1][currentGraphY + 1] = DIR_SOUTH | DIR_EAST;
				distances[currentGraphX - 1][1 + currentGraphY] = nextDistance;
			}
			if (currentGraphX < (GRAPH_SIZE - 1) && currentGraphY < (GRAPH_SIZE - 1) && directions[currentGraphX + 1][currentGraphY + 1] == 0
					&& 0 == (regionManager.getMask(plane, 1 + currentX, 1 + currentY) & 
							(Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE | Flags.CORNEROBJ_SOUTHWEST_BLOCKSWALK_ALTERNATIVE))
					&& 0 == (regionManager.getMask(plane, 1 + currentX, currentY) & 
							(Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE))
					&& 0 == (regionManager.getMask(plane, currentX, currentY + 1) & 
							(Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ_BLOCKSWALK_ALTERNATIVE | Flags.WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE))) {
				routeFinderArray[write][0] = 1 + currentX;
				routeFinderArray[write][1] = 1 + currentY;
				write = write + 1 & 0xfff;
				directions[currentGraphX + 1][currentGraphY + 1] = DIR_SOUTH | DIR_WEST;
				distances[currentGraphX + 1][1 + currentGraphY] = nextDistance;
			}
		}
		//System.out.println("1currentGraphX="+currentGraphX+", currentGraphY="+currentGraphY+", write="+write);
		exitX = currentX;
		exitY = currentY;
		return false;
	}
	
	public boolean isAlternative() {
		return isAlternative;
	}
	
	public boolean canExit (int currentX, int currentY, int clipBaseX, int clipBaseY) {
		int distanceX = currentX - target.getX();
		int distanceY = currentY - target.getY();
		//Is within x distance - Distance is greater than zero - Is within y distance - Distance y greater than zero
		return distanceX <= destSizeX && distanceX >= 0 && distanceY <= destSizeY && distanceY >= 0;
	}
}
