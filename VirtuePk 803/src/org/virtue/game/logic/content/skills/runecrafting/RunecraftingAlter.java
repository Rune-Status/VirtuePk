package org.virtue.game.logic.content.skills.runecrafting;

import org.virtue.game.logic.content.skills.Skill;
import org.virtue.game.logic.item.Item;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.update.masks.Graphics;
import org.virtue.game.logic.node.object.ObjectOption;
import org.virtue.game.logic.node.object.RS3Object;
import org.virtue.game.logic.region.Tile;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;

public class RunecraftingAlter extends RS3Object {
	
	private final AlterDefinition alterDetails;
	

	public RunecraftingAlter(int id, int rotation, int type, Tile tile, AlterDefinition rune) {
		super(id, rotation, type, tile);
		this.alterDetails = rune;
	}
	
	public RunecraftingAlter(RS3Object object, AlterDefinition rune) {
		super(object.getId(), object.getRotation(), object.getType(), object.getTile());
		this.alterDetails = rune;
	}
	
	public AlterDefinition getRuneDefinition () {
		return alterDetails;
	}
	
	@Override
	public void interact(Player player, ObjectOption option) {
		String message = "Clicked runecrafting alter: id="+getId()+", rotation="+getRotation()+", xCoord="+getTile().getX()+", yCoord="+getTile().getY()+", option="+getDefinition().getOption(option)+" ("+option.getID()+")";
		System.out.println(message);
		if (option.equals(ObjectOption.ONE)) {
			craftRunes(player);
		}
	}
	
	public void craftRunes(Player player) {
		if (player.getSkills().getCurrentLevel(Skill.RUNECRAFTING) < alterDetails.getLevel()) {
			player.getPacketDispatcher().dispatchMessage("You need a runecrafting level of "+alterDetails.getLevel()+" to craft this rune.", MessageOpcode.CHAT_BOX);
			return;
		}
		Item runes = Item.create(Runecrafting.PURE_ESSENCE, player.getInventory().getItems().getNumberOf(Runecrafting.PURE_ESSENCE));
		if (runes.getAmount() == 0) {
			if (!alterDetails.requiresPureEssence()) {
				runes = Item.create(Runecrafting.RUNE_ESSENCE, player.getInventory().getItems().getNumberOf(Runecrafting.RUNE_ESSENCE));
			}
			if (runes.getAmount() == 0) {
				player.getPacketDispatcher().dispatchMessage("You don't have " + (alterDetails.requiresPureEssence() ? "pure" : "rune") + " essence.", MessageOpcode.CHAT_BOX);
				return;
			}
		}			 
		double totalXP = alterDetails.getExperience() * runes.getAmount();
		player.getSkills().addExperience(Skill.RUNECRAFTING, totalXP, 0, true);
		player.getInventory().remove(runes);
		runes.setId(alterDetails.getRuneID());
		runes.setAmount(runes.getAmount() * alterDetails.getHighestMultiple(player.getSkills().getCurrentLevel(Skill.RUNECRAFTING)));
		player.getUpdateArchive().queueGraphic(new Graphics(186));
		player.getUpdateArchive().queueAnimation(791);
		player.lock(5);
		player.getInventory().add(runes);
		player.getPacketDispatcher().dispatchMessage("You bind the temple's power into "+runes.getDefinition().getName().toLowerCase()+"s.", MessageOpcode.CHAT_BOX);
	}

}
