package org.virtue.game.logic.content.skills.firemaking;

import org.virtue.game.logic.World;
import org.virtue.game.logic.content.skills.Skill;
import org.virtue.game.logic.events.PlayerActionEvent;
import org.virtue.game.logic.item.Item;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.region.Tile;
import org.virtue.network.protocol.messages.GameMessage;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class FiremakingAction extends PlayerActionEvent {
	
	private final Fire.Type fireType;
	
	private int emoteID = 16700;
	
	private final Item logs;
	private final int logSlot;
	
	private int calculatedTime;
	private int ticks = 0;
	
	public FiremakingAction (Item logs) {
		this(logs, -1);
	}
	
	public FiremakingAction (Item logs, int slot) {
		this(logs, slot, Fire.Type.forLogs(logs.getId()));		
	}
	
	public FiremakingAction (Item logs, int slot, Fire.Type type) {
		this.logs = logs;
		this.logSlot = slot;
		this.fireType = type;
	}

	@Override
	public boolean start(Player player) {
		if (fireType == null) {
			System.err.println("Error - fire not found for logs "+logs.getDefinition().getName()+" (id="+logs.getId()+")");
			return false;//This should indicates some kind of server issue
		} else if (player.getInventory().getItem(590) == null) {//Player does not have a tinderbox
			player.getPacketDispatcher().dispatchMessage("You need a tinderbox to light these logs.", GameMessage.MessageOpcode.CHAT_BOX);
			return false;
		} else if (player.getSkills().getCurrentLevel(Skill.FIREMAKING) < fireType.getLevel()) {
			player.getPacketDispatcher().dispatchMessage("You need a firemaking level of at least "+fireType.getLevel()+" to light these logs.", GameMessage.MessageOpcode.CHAT_BOX);
			return false;
		} else if (!checkTile(player)) {
			player.getPacketDispatcher().dispatchMessage("You can't light a fire here.", GameMessage.MessageOpcode.CHAT_BOX);
			return false;
		}
		player.getPacketDispatcher().dispatchMessage("You attempt to light the logs.", GameMessage.MessageOpcode.CHAT_BOX_FILTER);
		calculatedTime = fireType.getSpawnTime();//TODO: Calculate this based of the player's level, etc
		return true;
	}

	@Override
	public boolean process(Player player) {
		player.getUpdateArchive().queueAnimation(emoteID);
		if (ticks >= calculatedTime) {
			success(player);
			return true;
		}
		ticks++;
		return !checkTile(player);
	}
	
	private void success (Player player) {
		Tile location = new Tile(player.getTile());		
		player.getPacketDispatcher().dispatchMessage("The fire catches and the logs begin to burn.", GameMessage.MessageOpcode.CHAT_BOX_FILTER);
		player.getInventory().remove(logs, logSlot);
		player.getSkills().addExperience(Skill.FIREMAKING, fireType.getExperience(), 0, true);
		Fire fire = new Fire(location, fireType);
		World.getWorld().getRegionManager().getRegionByID(player.getTile().getRegionID()).updateTempObject(fire);
		player.moveAdjacentTo(location, false);//Moves the player to beside the fire
	}
	
	public boolean checkTile (Player player) {
		return World.getWorld().getRegionManager().getRegionByID(player.getTile().getRegionID()).isTileEmpty(player.getTile());
	}

	@Override
	public void stop(Player player) {
		player.getUpdateArchive().queueAnimation(-1);
	}
	
}
