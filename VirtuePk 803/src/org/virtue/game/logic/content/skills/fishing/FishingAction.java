package org.virtue.game.logic.content.skills.fishing;

import java.util.ArrayList;

import org.virtue.Launcher;
import org.virtue.game.logic.content.skills.Skill;
import org.virtue.game.logic.content.skills.fishing.Fishing.Fish;
import org.virtue.game.logic.content.skills.fishing.Fishing.FishingType;
import org.virtue.game.logic.events.PlayerActionEvent;
import org.virtue.game.logic.item.Item;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.network.protocol.messages.GameMessage;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class FishingAction extends PlayerActionEvent {
	
	private final FishingSpot spot;
	
	private final FishingType type;
	
	private Fish nextCatch;
	
	private int calculatedTime;
	private int ticks = 0;
	
	public FishingAction (FishingSpot spot, FishingType type) {
		this.spot = spot;
		this.type = type;
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll (player)) {
			return false;
		}
		player.getPacketDispatcher().dispatchMessage("You cast out your net...", GameMessage.MessageOpcode.CHAT_BOX_FILTER);
		nextCatch = getCatch(player);
		calculatedTime = calculateDelay(player);
		ticks = 0;
		return true;
	}
	
	public int calculateDelay (Player player) {
		//Calculation retrieved from Matrix 718
		int playerLevel = player.getSkills().getCurrentLevel(Skill.FISHING);
		int fishLevel = nextCatch.getLevel();
		int modifier = nextCatch.getLevel();
		int randomAmt = Launcher.getRandom().nextInt(4);
		int delay = (int) Math.ceil((fishLevel * 50 - playerLevel * 10) / modifier * 0.25 - randomAmt * 4) + 1;
		if (delay < 1 + 9) {//TODO: Make this specific for each fish.
			delay = 1 + Launcher.getRandom().nextInt(9);//Added so that level one fishes aren't caught in only a couple of ticks, on average
		}
		return delay;
	}
	
	public boolean checkAll (Player player) {
		if (player.getInventory().getItem(type.getToolID()) == null) {
			player.getPacketDispatcher().dispatchMessage("You need a fishing rod and some bait to fish here.", GameMessage.MessageOpcode.CHAT_BOX);
			return false;//Player does not have the required tool
		} else if (type.getBaitID() != -1 && player.getInventory().getItem(type.getBaitID()) == null) {
			player.getPacketDispatcher().dispatchMessage("You need a fishing rod and some bait to fish here.", GameMessage.MessageOpcode.CHAT_BOX);
			return false;//Player does not have the required bait
		} else if (player.getSkills().getCurrentLevel(Skill.FISHING) < type.getMinLevel()) {
			player.getPacketDispatcher().dispatchMessage("You need a fishing level of at least "+type.getMinLevel()+" to fish here.", GameMessage.MessageOpcode.CHAT_BOX);
			return false;
		} else if (player.getInventory().getItems().getFreeSlots() < 1) {
			player.getPacketDispatcher().dispatchMessage("You can't carry any more fish.", GameMessage.MessageOpcode.CHAT_BOX);
			return false;
		} else {
			return true;
		}
	}
	
	public Fish getCatch (Player player) {
		ArrayList<Fish> possible = new ArrayList<Fish>(type.getCatches().length);
		for (Fish fish : type.getCatches()) {
			if (fish.getLevel() <= player.getSkills().getCurrentLevel(Skill.FISHING)) {
				possible.add(fish);
			}
		}
		Fish selected = possible.get(Launcher.getRandom().nextInt(possible.size()));
		return selected;
	}

	@Override
	public boolean process(Player player) {
		player.getUpdateArchive().queueAnimation(type.getEmote());
		if (ticks >= calculatedTime) {
			success(player);
			if (!checkAll(player)) {//Make sure the player still has the right equipment
				return true;//If one of the conditions is no longer met, stop the action
			}
			nextCatch = getCatch(player);
			calculatedTime = calculateDelay(player);
			ticks = 0;			
		}
		ticks++;
		return !spot.exists();
	}
	
	/**
	 * This action is run if a fish is successfully caught. The action will not stop unless the fishing spot has been depleated
	 * @param player The player.
	 */
	private void success (Player player) {
		Item fish = Item.create(nextCatch.getID(), 1);
		player.getPacketDispatcher().dispatchMessage("You catch some " + fish.getDefinition().getName() + ".", GameMessage.MessageOpcode.CHAT_BOX_FILTER);		
		//TODO: Send filterable message
		player.getInventory().add(fish);
		if (type.getBaitID() != -1) {
			player.getInventory().remove(Item.create(type.getBaitID(), 1));
		}
		player.getSkills().addExperience(Skill.FISHING, nextCatch.getExperience(), 0, true);
	}

	@Override
	public void stop(Player player) {
		player.getUpdateArchive().queueAnimation(-1);
	}
	
}
