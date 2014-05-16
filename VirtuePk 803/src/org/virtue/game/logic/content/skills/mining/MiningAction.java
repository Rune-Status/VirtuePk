package org.virtue.game.logic.content.skills.mining;

import org.virtue.Launcher;
import org.virtue.game.logic.content.skills.Skill;
import org.virtue.game.logic.events.PlayerActionEvent;
import org.virtue.game.logic.item.Item;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.container.EquipSlot;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;

public class MiningAction extends PlayerActionEvent {
	
	private int emoteID;
	private final MiningRock rock;
	private Pickaxe pickaxe = Pickaxe.BRONZE;
	private int calculatedTime;
	private int ticks = 0;
	
	public MiningAction (MiningRock rock) {
		this.rock = rock;
	}

	@Override
	public boolean start(Player player) {
		pickaxe = getPickaxe(player);
		if (pickaxe == null) {
			player.getPacketDispatcher().dispatchMessage("You need a pickaxe to mine this rock.", MessageOpcode.CHAT_BOX);
			return false;
		} else if (player.getSkills().getCurrentLevel(Skill.MINING) < pickaxe.getLevel()) {
			player.getPacketDispatcher().dispatchMessage("You dont have the required level to use this pickaxe.", MessageOpcode.CHAT_BOX);
			return false;
		} else if (player.getSkills().getCurrentLevel(Skill.MINING) < rock.getOre().getLevel()) {
			player.getPacketDispatcher().dispatchMessage("You require a mining level of "+rock.getOre().getLevel()+" to mine this rock.", MessageOpcode.CHAT_BOX);
			return false;
		} else if (player.getInventory().getItems().getFreeSlots() < 1) {
			player.getPacketDispatcher().dispatchMessage("Not enough space in your inventory.", MessageOpcode.CHAT_BOX);
			return false;
		}
		player.getPacketDispatcher().dispatchMessage("You swing your pickaxe at the rock.", MessageOpcode.CHAT_BOX_FILTER);
		calculatedTime = calculateDelay(player);
		System.out.println("Calculated time: "+calculatedTime);
		emoteID = pickaxe.getEmote();
		return true;
	}
	
	public int calculateDelay (Player player) {
		int delay = rock.getOre().getMineMaxTime() - player.getSkills().getCurrentLevel(Skill.MINING) - Launcher.getRandom().nextInt(pickaxe.getTimeDiscount());
		if (delay < 1 + rock.getOre().getRandomTime()) {
			delay = 1 + Launcher.getRandom().nextInt(rock.getOre().getRandomTime());
		}
		return delay;
	}
	
	public Pickaxe getPickaxe (Player player) {
		Item mainSlotItem = player.getEquipment().getAtSlot(EquipSlot.MAINHAND);
		Pickaxe pick = null;
		if (mainSlotItem != null) {
			pick = Pickaxe.forItemID(mainSlotItem.getId());
		}
		if (pick == null) {
			for (Pickaxe p : Pickaxe.values()) {
				if (player.getInventory().getItem(p.getItemID()) != null 
						&& player.getSkills().getCurrentLevel(Skill.MINING) >= p.getLevel()
						&& (pick == null || p.getTimeDiscount() > pick.getTimeDiscount())) {
					pick = p;
				}
			}
		}
		return pick;		
	}

	@Override
	public boolean process(Player player) {
		player.getUpdateArchive().queueAnimation(emoteID);
		if (ticks >= calculatedTime) {
			success(player);
			return true;
		}
		ticks++;
		return rock.isDepleted();
	}
	
	private void success (Player player) {
		Item ore = new Item(rock.getOre().getOreID(), 1);
		//System.out.println("You mine some " + ore.getDefinition().getName() + ".");
		player.getPacketDispatcher().dispatchMessage("You mine some " + ore.getDefinition().getName() + ".", MessageOpcode.CHAT_BOX_FILTER);		
		rock.deplete();		
		player.getInventory().add(ore);
		player.getSkills().addExperience(Skill.MINING, rock.getOre().getExperience(), 0, true);
		//System.out.println("Completed mining action.");
		
	}

	@Override
	public void stop(Player player) {
		player.getUpdateArchive().queueAnimation(-1);
	}

}
