package org.virtue.game.logic.content.skills.woodcutting;

import org.virtue.Launcher;
import org.virtue.game.logic.content.skills.Skill;
import org.virtue.game.logic.events.PlayerActionEvent;
import org.virtue.game.logic.item.Item;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.container.EquipSlot;
import org.virtue.network.protocol.messages.GameMessage;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class WoodcuttingAction extends PlayerActionEvent {
	
	private int emoteID;
	private Hatchet axe = Hatchet.BRONZE;
	private final WoodcuttingTree tree;
	private int calculatedTime;
	private int ticks = 0;
	
	public WoodcuttingAction (WoodcuttingTree tree) {
		this.tree = tree;
	}

	@Override
	public boolean start(Player player) {
		axe = getAxe(player);
		if (axe == null) {
			player.getPacketDispatcher().dispatchMessage("You need a hatchet to chop this tree.", GameMessage.MessageOpcode.CHAT_BOX);
			return false;
		} else if (player.getSkillManager().getLevel(Skill.WOODCUTTING) < axe.getLevel()) {
			player.getPacketDispatcher().dispatchMessage("You dont have the required level to use this hatchet.", GameMessage.MessageOpcode.CHAT_BOX);
			return false;
		} else if (player.getSkillManager().getLevel(Skill.WOODCUTTING) < tree.getLog().getLevel()) {
			player.getPacketDispatcher().dispatchMessage("You require a woodcutting level of "+tree.getLog().getLevel()+" to chop down this tree.", GameMessage.MessageOpcode.CHAT_BOX);
			return false;
		} else if (player.getInventory().getItems().getFreeSlots() < 1) {
			player.getPacketDispatcher().dispatchMessage("Not enough space in your inventory.", GameMessage.MessageOpcode.CHAT_BOX);
			return false;
		}
		player.getPacketDispatcher().dispatchMessage("You swing your hatchet at the tree.", GameMessage.MessageOpcode.CHAT_BOX_FILTER);
		calculatedTime = calculateDelay(player);
		System.out.println("Calculated time: "+calculatedTime);
		emoteID = axe.getEmote();
		return true;
	}
	
	public int calculateDelay (Player player) {
		int delay = tree.getLog().getChopMaxTime() - player.getSkillManager().getLevel(Skill.WOODCUTTING) - Launcher.getRandom().nextInt(axe.getTimeDiscount());
		if (delay < 1 + tree.getLog().getRandomTime()) {
			delay = 1 + Launcher.getRandom().nextInt(tree.getLog().getRandomTime());
		}
		return delay;
	}
	
	public Hatchet getAxe (Player player) {
		Item mainSlotItem = player.getEquipment().getAtSlot(EquipSlot.MAINHAND);
		Hatchet axe = null;
		if (mainSlotItem != null) {
			axe = Hatchet.forItemID(mainSlotItem.getId());
		}
		if (axe == null) {
			for (Hatchet p : Hatchet.values()) {
				if (player.getInventory().getItem(p.getItemID()) != null 
						&& player.getSkillManager().getLevel(Skill.WOODCUTTING) >= p.getLevel()
						&& (axe == null || p.getTimeDiscount() > axe.getTimeDiscount())) {
					axe = p;
				}
			}
		}
		return axe;		
	}

	@Override
	public boolean process(Player player) {
		player.getUpdateArchive().queueAnimation(emoteID);
		if (ticks >= calculatedTime) {
			success(player);
			if (player.getInventory().getItems().getFreeSlots() < 1) {
				player.getPacketDispatcher().dispatchMessage("Not enough space in your inventory.", GameMessage.MessageOpcode.CHAT_BOX);
				return false;
			}
			calculatedTime = calculateDelay(player);
		}
		ticks++;
		return tree.isDepleted();
	}
	
	/**
	 * This action is run if a log is successfully chopped. Note that the action doesn't necessarily stop just because this method is called.
	 * @param player The player.
	 */
	private void success (Player player) {
		Item log = new Item(tree.getLog().getLogID(), 1);
		player.getPacketDispatcher().dispatchMessage("You get some " + log.getDefinition().getName() + ".", GameMessage.MessageOpcode.CHAT_BOX_FILTER);		
		tree.deplete();
		player.getInventory().add(log);
		player.getSkillManager().addExperience(Skill.WOODCUTTING, tree.getLog().getExperience(), 0, true);
	}

	@Override
	public void stop(Player player) {
		player.getUpdateArchive().queueAnimation(-1);
	}
	
}
