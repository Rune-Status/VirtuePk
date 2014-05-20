package org.virtue.game.logic.content.skills.runecrafting;

import org.virtue.game.logic.content.skills.runecrafting.Runecrafting.Talisman;
import org.virtue.game.logic.item.Item;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.container.EquipSlot;
import org.virtue.game.logic.node.object.ObjectOption;
import org.virtue.game.logic.node.object.RS3Object;
import org.virtue.game.logic.region.Tile;

public class MysteriousRunes extends RS3Object {
	
	public static enum Definition {
		AIR(2452, Talisman.AIR, new Tile(2841, 4829, 0)),
		MIND(2453, Talisman.MIND, new Tile(2792, 4827, 0)),
		WATER(2454, Talisman.WATER, new Tile(3482, 4838, 0)),
		EARTH(2455, Talisman.EARTH, new Tile(2655, 4830, 0)),
		FIRE(2456, Talisman.FIRE, new Tile(2574, 4848, 0)),
		BODY(2457, Talisman.BODY, new Tile(2522, 4835, 0)),
		COSMIC(2458, Talisman.COSMIC, new Tile(2142, 4853, 0)),
		CHAOS(2461, Talisman.CHAOS, new Tile(2281, 4837, 0)),
		NATURE(2460, Talisman.NATURE, new Tile(2398, 4841, 0)),
		LAW(2459, Talisman.LAW, new Tile(2464, 4834, 0)),
		DEATH(2462, Talisman.DEATH, new Tile(2207, 4836, 0)),
		BLOOD(-1, Talisman.BLOOD, new Tile(2462, 4891, 1));//FIXME: The server isn't recognising this object...
		
		private final int objectID;
		
		private final Talisman talisman;
		
		private final Tile destination;
		
		Definition(int objectID, Talisman talisman, Tile destination) {
			this.objectID = objectID;
			this.talisman = talisman;
			this.destination = destination;
		}
		
		public int getObjectID () {
			return objectID;
		}
		
		public Talisman getTalisman() {
			return talisman;
		}
		
		public Tile getDestination () {
			return destination;
		}
		
		public static Definition forID (int runesID) {
			for (Definition def : Definition.values()) {
				if (def.objectID == runesID) {
					return def;
				}
			}
			return null;
		}
	}
	
	private Definition data;

	public MysteriousRunes(int id, int rotation, int type, Tile tile, Definition definition) {
		super(id, rotation, type, tile);
		this.data = definition;
	}
	
	/**
	 * Transforms an object into a {@code MysteriousRunes} object, representing the runecrafting "Mysterious Runes"
	 * @param object	The object to transform
	 * @param type		The data associated with the mysterious runes
	 */
	public MysteriousRunes(RS3Object object, Definition type) {
		super(object.getId(), object.getRotation(), object.getType(), object.getTile());
		this.data = type;
	}

	@Override
	public void interact (Player player, ObjectOption option) {
		if (option.equals(ObjectOption.ONE) && getDefinition().getOption(option).equalsIgnoreCase("enter")) {
			Item hatSlot = player.getEquipment().getAtSlot(EquipSlot.HAT);
			Item handSlot = player.getEquipment().getAtSlot(EquipSlot.MAINHAND);
			if ((hatSlot != null && hatSlot.getId() == data.getTalisman().getTiara())
					|| (handSlot != null && handSlot.getId() == data.getTalisman().getStaff())
					|| player.getInventory().contains(data.getTalisman().getTalisman())) {
				player.teleport(data.getDestination());
			} else {
				player.getPacketDispatcher().dispatchMessage("You do not have the required talisman to enter these runes.");
			}		
		} else {
			System.out.println("Clicked mysterious runes: objectID="+getId()+", type="+getType()+", xCoord="+getTile().getX()+", yCoord="+getTile().getY()+", zCoord="+getTile().getPlane()+", optionID="+option.getID());
		}
	}
}
