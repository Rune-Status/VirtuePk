package org.virtue.game.logic.content.skills.firemaking;

import org.virtue.Constants;
import org.virtue.game.logic.World;
import org.virtue.game.logic.item.GroundItem;
import org.virtue.game.logic.item.Item;
import org.virtue.game.logic.item.Logs;
import org.virtue.game.logic.item.Logs.LogType;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.identity.Rank;
import org.virtue.game.logic.node.object.ObjectOption;
import org.virtue.game.logic.node.object.TemporaryObject;
import org.virtue.game.logic.region.Region;
import org.virtue.game.logic.region.Tile;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */

public class Fire extends TemporaryObject {
	
	public static enum Type {
		NORMAL(LogType.NORMAL, 1, 40, 70755, 300, 20),
		OAK(LogType.OAK, 15, 60, 70757, 450, 1),//TODO: Find the actual spawn time delay
		WILLOW(LogType.WILLOW, 30, 90, 70758, 450, 1),
		TEAK(LogType.TEAK, 35, 105, 70759, 450, 1),
		MAPLE(LogType.MAPLE, 45, 141, 70761, 500, 1),
		MAHOGANY(LogType.MAHOGANY, 50, 157.5, 70762, 700, 1),
		YEW(LogType.YEW, 60, 202.5, 70764, 800, 1),
		MAGIC(LogType.MAGIC, 75, 303.8, 70765, 900, 1),
		ELDER(LogType.ELDER, 90, 450, -1, -1, 1);//TODO: Find fire ID and duration

		private final int fireID;
		
		private final LogType logs;

		private final int level;

		private final double experience;

		private final int duration, spawnTime;

		Type (LogType logs, int level, double xp, int fireID, int duration, int spawnTime) {
			this.logs = logs;
			this.level = level;
			this.experience = xp;
			this.fireID = fireID;
			this.duration = duration;
			this.spawnTime = spawnTime;
		}

		/**
		 * Gets the LogType of the logs used to start the fire
		 * @return	The log type
		 */
		public LogType getLogType () {
			return logs;
		}

		/**
		 * Gets the level needed to light this log
		 * @return	The level needed
		 */
		public int getLevel () {
			return level;
		}

		/**
		 * Gets the amount of experience gained when a fire is successfully lit
		 * @return	The experience gained
		 */
		public double getExperience () {
			return experience;
		}

		/**
		 * Gets the object ID of the fire
		 * @return	The fire ID
		 */
		public int getFireID () {
			return fireID;
		}

		/**
		 * Gets the number of ticks the first will last after being lit
		 * @return	The duration (in ticks)
		 */
		public int getDuration () {
			return duration;
		}

		/**
		 * Gets the average number of ticks the fire should take to light
		 * @return	The spawn time (in ticks)
		 */
		public int getSpawnTime () {
			return spawnTime;
		}
		
		public static Type forLogs (int logID) {
			for (Type type : Type.values()) {
				if (logID == type.logs.getID()) {
					return type;
				}
			}
			return null;
		}
	}	
	
	private boolean exists = true;

	public Fire(Tile tile, Type type) {
		super(type.getFireID(), 0, 10, tile, -1, type.getDuration());
	}
	
	@Override
	public boolean isDepleted () {
		return true;//The object is always in the "depletable" state, as it is not respawnable
	}
	
	public boolean exists () {
		return exists;
	}
	
	@Override
	public void respawn () {
		exists = false;
		Region region = World.getWorld().getRegionManager().getRegionByID(getTile().getRegionID());
		region.destroyTemporyObject(this);//Remove the fire
		region.addItem(new GroundItem(592, 1, getTile()));//Spawn ashes
	}
	
	@Override
	public void interact (Player player, ObjectOption option) {
		super.interact(player, option);//TODO: Implement fire interaction
	}
	
	@Override
	public void useItem (Player player, Item item) {
		if (item instanceof Logs) {
			Logs logs = (Logs) item;
			BonfireAction.Type addType = BonfireAction.Type.forLogType(logs.getType());
			if (addType != null) {
				player.setActionEvent(new BonfireAction(this, addType));
				return;
			}
		}		
		String message = "Nothing interesting happens.";
		if (Constants.DEVELOPER_MODE || player.getAccount().getRank().equals(Rank.ADMINISTRATOR)) {
			message = "Used item "+item.getDefinition().getName()+" ("+item.getId()+") on fire at x="+getTile().getX()+", y="+getTile().getY()+", z="+getTile().getPlane();
		}
		player.getPacketDispatcher().dispatchMessage(message);
	}
	
}
