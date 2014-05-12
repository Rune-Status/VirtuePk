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
	private MiningRock rock;
	private Pickaxe pickaxe = Pickaxe.RUNE;
	private int calculatedTime;
	private int ticks = 0;
	
	public MiningAction (MiningRock rock) {
		this.rock = rock;
	}

	@Override
	public boolean start(Player player) {
		player.getPacketDispatcher().dispatchMessage("You swing your pickaxe at the rock.", MessageOpcode.CHAT_BOX_FILTER);
		calculatedTime = calculateDelay(player);
		System.out.println("Calculated time: "+calculatedTime);
		emoteID = 628;
		return true;
	}
	
	public int calculateDelay (Player player) {
		return rock.getOre().getMineTime() - player.getSkillManager().getLevel(Skill.MINING) - Launcher.getRandom().nextInt(pickaxe.getTimeDiscount());
	}
	
	public Pickaxe getPickaxe (Player player) {
		Pickaxe pickaxe = Pickaxe.forItemID(player.getEquipment().getAtSlot(EquipSlot.MAINHAND).getId());
		if (pickaxe == null) {
			for (Pickaxe p : Pickaxe.values()) {
				if (player.getInventory().getItem(p.getItemID()) != null) {
					pickaxe = p;
					break;//TODO: Finish implementing this.
				}
			}
		}
		return pickaxe;		
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
		System.out.println("You mine some " + ore.getDefinition().getName() + ".");
		player.getPacketDispatcher().dispatchMessage("You mine some " + ore.getDefinition().getName() + ".", MessageOpcode.CHAT_BOX_FILTER);
		rock.deplete();
	}

	@Override
	public void stop(Player player) {
		player.getUpdateArchive().queueAnimation(-1);
	}

}
