package org.virtue.game.logic.content.skills.firemaking;

import org.virtue.game.logic.content.skills.Skill;
import org.virtue.game.logic.events.CoordinateEvent;
import org.virtue.game.logic.events.PlayerActionEvent;
import org.virtue.game.logic.item.Logs;
import org.virtue.game.logic.item.Logs.LogType;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.update.masks.Graphics;
import org.virtue.game.logic.region.Tile;
import org.virtue.network.protocol.messages.GameMessage;

public class BonfireAction extends PlayerActionEvent {
	
	public static enum Type {
		NORMAL(LogType.NORMAL, 1, 50, 3098),
		OAK(LogType.OAK, 15, 85, 3099),
		WILLOW(LogType.WILLOW, 30, 105, 3101),//3105 - Willow pyre logs
		TEAK(LogType.TEAK, 35, 120, 3110),//TODO: Find gfx ID
		MAPLE(LogType.MAPLE, 45, 135, 3100),
		MAHOGANY(LogType.MAHOGANY, 50, 180, 3109),//3112 - Mahogany pyre logs
		EUCALYPTUS(LogType.EUCALYPTUS, 58, 195, 3113),
		YEW(LogType.YEW, 60, 260, 3114),//3111
		MAGIC(LogType.MAGIC, 75, 309.5, 3115),//3135
		ELDER(LogType.ELDER, 90, 449, 3116);//Close enough, unless someone else can find the proper gfx better
		
		private final LogType logs;

		private final int level;

		private final double experience;
		
		private final int gfxID;
		
		Type (LogType logs, int level, double xp, int gfxID) {
			this.logs = logs;
			this.level = level;
			this.experience = xp;
			this.gfxID = gfxID;
		}

		/**
		 * Gets the LogType of the logs used to add to the fire
		 * @return	The log type
		 */
		public LogType getLogType () {
			return logs;
		}

		/**
		 * Gets the level needed to add this log to a bonfire
		 * @return	The level needed
		 */
		public int getLevel () {
			return level;
		}

		/**
		 * Gets the amount of experience gained when the log is successfully added to a bonfire
		 * @return	The experience gained
		 */
		public double getExperience () {
			return experience;
		}

		/**
		 * Gets the ID of the graphics used when a log is added to the bonfire
		 * @return	The level needed
		 */
		public int getGraphicsID () {
			return gfxID;
		}
		
		public static Type forLogType (LogType logs) {
			for (Type type : Type.values()) {
				if (type.logs.equals(logs)) {
					return type;
				}
			}
			return null;
		}
	}
	
	private int emoteID = 16703;
	
	private final Fire fire;
	
	private final Type addType;
	
	private final Logs logs;
	
	public static final int STANDARD_DELAY = 6;
	
	private int calculatedTime;
	private int ticks = 0;
	private int preferedSlot;
	
	private boolean tryMove = true;
	
	public BonfireAction (Fire fire, Type logType) {
		this(fire, logType, -1);
	}
	
	public BonfireAction (Fire fire, Type logType, int preferedSlot) {
		this.fire = fire;
		this.addType = logType;
		this.logs = new Logs(logType.getLogType(), 1);
		this.preferedSlot = preferedSlot;
	}

	@Override
	public boolean start(Player player) {
		if (player.getSkills().getCurrentLevel(Skill.FIREMAKING) < addType.getLevel()) {
			player.getPacketDispatcher().dispatchMessage("You need a firemaking level of at least "+addType.getLevel()+" to add these logs to a bonfire.", GameMessage.MessageOpcode.CHAT_BOX);
			return false;
		} else if (!player.isAdjacentTo(fire.getTile(), false)) {
			if (!tryMove) {
				return false;
			}
			if (player.moveAdjacentTo(fire.getTile(), false)) {//Try to move the player to an adjacent tile
				int[] walkTile = player.getUpdateArchive().getMovement().getLastWalkTile();
				Tile target = new Tile(walkTile[0], walkTile[1], fire.getTile().getPlane());
				final BonfireAction action = this;
				tryMove = true;
				player.setCoordinateEvent(new CoordinateEvent (target, 0, 0) {
					@Override
					public void run(Player player) {
						player.setActionEvent(action);
					}					
				});
			}
			return false;//TODO: Move the player adjacent to the bonfire
		}
		calculatedTime = STANDARD_DELAY;
		player.getUpdateArchive().queueFaceDirection(fire.getTile());
		player.getUpdateArchive().getAppearance().setRenderEmote(2498);
		player.getUpdateArchive().getAppearance().packBlock();
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (ticks >= calculatedTime) {
			if (!player.getInventory().contains(addType.getLogType().getID())) {
				return true;//No logs left
			}
			success(player);
			ticks = -1;
		}
		ticks++;
		return !fire.exists();
	}
	
	private void success (Player player) {
		player.getInventory().remove(logs, preferedSlot);
		player.getSkills().addExperience(Skill.FIREMAKING, addType.getExperience(), 0, true);
		player.getUpdateArchive().queueAnimation(emoteID);
		player.getUpdateArchive().queueGraphic(new Graphics(addType.getGraphicsID()));
		player.getPacketDispatcher().dispatchMessage("You add a log to the fire.", GameMessage.MessageOpcode.CHAT_BOX);
	}

	@Override
	public void stop(Player player) {
		player.getUpdateArchive().queueAnimation(-1);
		player.getUpdateArchive().getAppearance().setRenderEmote(-1);
		player.getUpdateArchive().getAppearance().packBlock();
	}

}
