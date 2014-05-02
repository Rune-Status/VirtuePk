package org.virtue.game.logic.node.entity.player.update.movement;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.virtue.game.logic.World;
import org.virtue.game.logic.node.entity.Entity;
import org.virtue.game.logic.node.entity.npc.NPC;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.identity.Rank;
import org.virtue.game.logic.region.Tile;

/**
 * @author Taylor
 * @version 1.0
 */
public class Movement {

	public Entity entity;
	
	/**
	 * Constructs a new {@code Movement.java}.
	 * @param player The player.
	 */
	public Movement(Entity player) {
		this.entity = player;
	}

	/**
	 * Represents the walk steps.
	 */
	private ConcurrentLinkedQueue<int[]> walkSteps = new ConcurrentLinkedQueue<int[]>();
	
	/**
	 * Represents the next walk direction.
	 */
	private int nextWalkDirection = -1;
	
	/**
	 * Represents the next run direction.
	 */
	private int nextRunDirection = -1;
	
	/**
	 * Represents if the player teleported or not.
	 */
	private boolean teleported;
	
	/**
	 * Represents if the entity is running.
	 */
	private boolean running;
	
	/**
	 * Represents whether this is a forceRun movement
	 */
	private boolean forceRun;
	
	/**
	 * Represents whether this movement requires the movement type to be re-sent
	 */
	private boolean isFirstStep;
	
	/**
	 * @return The nextWalkDirection
	 */
	public int getNextWalkDirection() {
		return nextWalkDirection;
	}
	/**
	 * @param nextWalkDirection The nextWalkDirection to set
	 */
	public void setNextWalkDirection(int nextWalkDirection) {
		this.nextWalkDirection = nextWalkDirection;
	}
	/**
	 * @return The nextRunDirection
	 */
	public int getNextRunDirection() {
		return nextRunDirection;
	}
	
	/**
	 * @return If there is walk steps.
	 */
	public boolean hasWalkSteps() {
		return !walkSteps.isEmpty();
	}
	
	/**
	 * @param nextRunDirection The nextRunDirection to set
	 */
	public void setNextRunDirection(int nextRunDirection) {
		this.nextRunDirection = nextRunDirection;
	}
	/**
	 * @return The walkSteps
	 */
	public ConcurrentLinkedQueue<int[]> getWalkSteps() {
		return walkSteps;
	}
	/**
	 * @param walkSteps The walkSteps to set
	 */
	public void setWalkSteps(ConcurrentLinkedQueue<int[]> walkSteps) {
		this.walkSteps = walkSteps;
	}
	
	public void resetWalkSteps() {
		walkSteps.clear();
	}

	public boolean addWalkStepsInteract(int destX, int destY, int maxStepsCount, int size, boolean calculate) {
		return addWalkStepsInteract(destX, destY, maxStepsCount, size, size, calculate);
	}
	
	public boolean addWalkStepsInteract(final int destX, final int destY,
			int maxStepsCount, int sizeX, int sizeY, boolean calculate) {
		int[] lastTile = getLastWalkTile();
		int myX = lastTile[0];
		int myY = lastTile[1];
		//System.out.println("Current tile: x="+myX+", y="+myY+", dest tile: x="+destX+", y="+destY);
		int stepCount = 0;
		while (true) {
			stepCount++;
			int myRealX = myX;
			int myRealY = myY;

			if (myX < destX) {
				myX++;
			} else if (myX > destX) {
				myX--;
			} 
			if (myY < destY) {
				myY++;
			} else if (myY > destY) {
				myY--;
			}
			if (!addWalkStep(myX, myY, lastTile[0], lastTile[1], true)) {
				if (!calculate) {
					return false;
				}
				myX = myRealX;
				myY = myRealY;
				int[] myT = calculatedStep(myRealX, myRealY, destX, destY,
						lastTile[0], lastTile[1], sizeX, sizeY);
				if (myT == null) {
					return false;
				}
				myX = myT[0];
				myY = myT[1];
			}
			int distanceX = myX - destX;
			int distanceY = myY - destY;
			if (!(distanceX > sizeX || distanceX < -1 || distanceY > sizeY || distanceY < -1)) {
				return true;//Reached an adjacent tile
			}
			if (stepCount == maxStepsCount) {
				return true;//Overshot the maximum number of steps
			}
			lastTile[0] = myX;
			lastTile[1] = myY;
			if (lastTile[0] == destX && lastTile[1] == destY) {
				return true;//Reached the target tile
			}
		}
	}

	public int[] calculatedStep(int myX, int myY, int destX, int destY,
			int lastX, int lastY, int sizeX, int sizeY) {
		if (myX < destX) {
			myX++;
			if (!addWalkStep(myX, myY, lastX, lastY, true))
				myX--;
			else if (!(myX - destX > sizeX || myX - destX < -1
					|| myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY)
					return null;
				return new int[] { myX, myY };
			}
		} else if (myX > destX) {
			myX--;
			if (!addWalkStep(myX, myY, lastX, lastY, true))
				myX++;
			else if (!(myX - destX > sizeX || myX - destX < -1
					|| myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY)
					return null;
				return new int[] { myX, myY };
			}
		}
		if (myY < destY) {
			myY++;
			if (!addWalkStep(myX, myY, lastX, lastY, true))
				myY--;
			else if (!(myX - destX > sizeX || myX - destX < -1
					|| myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY)
					return null;
				return new int[] { myX, myY };
			}
		} else if (myY > destY) {
			myY--;
			if (!addWalkStep(myX, myY, lastX, lastY, true)) {
				myY++;
			} else if (!(myX - destX > sizeX || myX - destX < -1
					|| myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY)
					return null;
				return new int[] { myX, myY };
			}
		}
		if (myX == lastX || myY == lastY)
			return null;
		return new int[] { myX, myY };
	}

	public boolean addWalkSteps(final int destX, final int destY,
			int maxStepsCount) {
		return addWalkSteps(destX, destY, -1, true);
	}

	public boolean addWalkSteps(final int destX, final int destY,
			int maxStepsCount, boolean check) {
		int[] lastTile = getLastWalkTile();
		int myX = lastTile[0];
		int myY = lastTile[1];
		int stepCount = 0;
		while (true) {
			stepCount++;
			if (myX < destX) {
				myX++;
			} else if (myX > destX) {
				myX--;
			}
			if (myY < destY) {
				myY++;
			} else if (myY > destY) {
				myY--;
			}
			if (!addWalkStep(myX, myY, lastTile[0], lastTile[1], check)) {
				return false;
			}
			if (stepCount == maxStepsCount) {
				return true;
			}
			lastTile[0] = myX;
			lastTile[1] = myY;
			if (lastTile[0] == destX && lastTile[1] == destY) {
				return true;
			}
		}
	}

	public int[] getLastWalkTile() {
		Object[] objects = walkSteps.toArray();
		if (objects.length == 0) {
			return new int[] { entity.getTile().getX(), entity.getTile().getY() };
		}
		int step[] = (int[]) objects[objects.length - 1];
		return new int[] { step[1], step[2] };
	}

	public boolean checkWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) {
		int dir = MovementUtils.getMoveDirection(nextX - lastX, nextY - lastY);
		if (dir == -1)
			return false;
		return true;
	}
	
	public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) {
		int dir = MovementUtils.getMoveDirection(nextX - lastX, nextY - lastY);
		if (dir == -1) {
			return false;
		}
//		if (check) {
//			if (!World.getWorld().checkWalkStep(entity.getTile().getPlane(), lastX, lastY, dir, entity.getUpdateArchive().getAppearance().getSize()))
//				return false;
//		}
		//System.out.println("Adding walk step: dir="+dir+", x="+nextX+", y="+nextY);
		walkSteps.add(new int[] { dir, nextX, nextY });
		return true;
	}
	
	private int getNextWalkStep() {
		int step[] = walkSteps.poll();
		if (step == null) {
			return -1;
		}
		return step[0];
	}
	
	public void teleport(Tile tile) {
		entity.getLastTile().copy(entity.getTile());
		entity.getTile().copy(tile);
		setTeleported(true);
	}

	public void process() {
		nextWalkDirection = nextRunDirection = -1;
		nextWalkDirection = getNextWalkStep();	
		Player p = null;	
		if (nextWalkDirection != -1) {
			//entity.setLastTile(entity.getTile());
			entity.getLastTile().copy(entity.getTile());
			Tile next = Tile.edit(entity.getTile(), MovementUtils.DIRECTION_DELTA_X[nextWalkDirection], MovementUtils.DIRECTION_DELTA_Y[nextWalkDirection], 0);
			if (!(entity instanceof NPC)) {
				p = (Player) entity;
				if (next.getRegionID() != entity.getTile().getRegionID()) {
					World.getWorld().getRegionManager().getRegionByID(entity.getTile().getRegionID()).getPlayers().remove((Player) entity);
					World.getWorld().getRegionManager().getRegionByID(next.getRegionID()).addPlayer((Player) entity);
				}
			}
			//System.out.println("Current: x="+entity.getLastTile().getX()+", y="+entity.getLastTile().getY());
			entity.getTile().copy(next);
			boolean clipped = World.getWorld().getRegionManager().isClipped(entity.getTile());
			if (clipped && p != null) {
				System.out.println("Clipped: clipped="+clipped+", tile="+entity.getTile());
				if (p.getAccount().getRank().equals(Rank.ADMINISTRATOR)) {
					p.getPacketDispatcher().dispatchMessage("Tile clipped: "+p.getTile());
				}
			}
			//System.out.println("New: x="+entity.getTile().getX()+", y="+entity.getTile().getY());
		}
		if (running) {
			nextRunDirection = getNextWalkStep();
			if (nextRunDirection != -1) {
				//entity.setLastTile(entity.getTile());
				Tile next = Tile.edit(entity.getTile(), MovementUtils.DIRECTION_DELTA_X[nextRunDirection], MovementUtils.DIRECTION_DELTA_Y[nextRunDirection], 0);
				if (entity instanceof Player) {
					((Player) entity).drainRunEnergy();
					if (next.getRegionID() != entity.getTile().getRegionID()) {
						World.getWorld().getRegionManager().getRegionByID(entity.getTile().getRegionID()).getPlayers().remove((Player) entity);
						World.getWorld().getRegionManager().getRegionByID(next.getRegionID()).addPlayer((Player) entity);
					}
				}				
				entity.getTile().copy(next);
				boolean clipped = World.getWorld().getRegionManager().isClipped(entity.getTile());
				if (clipped && p != null) {
					/*System.out.println("Clipped: clipped="+clipped+", tile="+entity.getTile());
					if (p.getAccount().getRank().equals(Rank.ADMINISTRATOR)) {
						p.getPacketDispatcher().dispatchMessage("Tile clipped: "+p.getTile());
					}*/
				}
			}
		}
		if (!(entity instanceof NPC)) {
			if (((Player) entity).getViewport().needsMapUpdate()) {
				((Player) entity).getViewport().loadViewport();
				((Player) entity).loadMapRegion();
			}
		}
	}
	
	/**
	 * @return The teleported
	 */
	public boolean hasTeleported() {
		return teleported;
	}
	
	/**
	 * @param teleported The teleported to set
	 */
	public void setTeleported(boolean teleported) {
		this.teleported = teleported;
	}
	
	public int getMoveType () {
		int movementType = 0;
		if (getNextWalkDirection() != -1) {
			movementType = 1;
		}
		if (getNextRunDirection() != -1) {
			movementType = 2;
		}
		return movementType;
	}
	
	/**
	 * @return	Whether the next update needs the movement type included
	 */
	public boolean needsTypeUpdate() {
		return isFirstStep;
	}
	
	public void setNeedsTypeUpdate (boolean needsTypeUpdate) {
		this.isFirstStep = needsTypeUpdate;
	}
	
	/**
	 * @return The running
	 */
	public boolean isRunning() {
		return running;
	}
	
	public boolean isForceRun () {
		return forceRun;
	}
	
	/**
	 * @param running The running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
		setNeedsTypeUpdate(true);
	}
	
	public void swapRunning() {
		swapRunning(false);
	}
	
	public void swapRunning(boolean isForced) {
		running = !running;
		setNeedsTypeUpdate(true);
		forceRun = isForced;
	}
}
