package org.virtue.game.logic.node.object;

import java.util.HashMap;
import java.util.Map;

import org.virtue.Constants;
import org.virtue.cache.def.ObjectDefinition;
import org.virtue.cache.def.ObjectDefinitionLoader;
import org.virtue.game.logic.content.skills.farming.FarmingPatch;
import org.virtue.game.logic.content.skills.mining.MiningRock;
import org.virtue.game.logic.content.skills.mining.Ore;
import org.virtue.game.logic.content.skills.runecrafting.AlterDefinition;
import org.virtue.game.logic.content.skills.runecrafting.MysteriousRunes;
import org.virtue.game.logic.content.skills.runecrafting.Runecrafting;
import org.virtue.game.logic.content.skills.runecrafting.RunecraftingAlter;
import org.virtue.game.logic.content.skills.woodcutting.TreeDefinition;
import org.virtue.game.logic.content.skills.woodcutting.WoodcuttingTree;
import org.virtue.game.logic.item.Item;
import org.virtue.game.logic.node.Node;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.identity.Rank;
import org.virtue.game.logic.node.interfaces.impl.Bank;
import org.virtue.game.logic.region.Tile;

/**
 * @author Taylor
 * @date Jan 21, 2014
 */
public class RS3Object extends Node {
	
	private static final Map<Integer, Class<? extends RS3Object>> transforms = new HashMap<Integer, Class<? extends RS3Object>>();
	
	public static void initTransforms () {
		for (FarmingPatch.Definition patch : FarmingPatch.Definition.values()) {
			transforms.put(patch.getObjectID(), FarmingPatch.class);
		}
		for (AlterDefinition alter : AlterDefinition.values()) {
			transforms.put(alter.getAlterID(), RunecraftingAlter.class);
		}
		for (MysteriousRunes.Definition runes : MysteriousRunes.Definition.values()) {
			transforms.put(runes.getObjectID(), MysteriousRunes.class);
		}
		System.out.println("Initialised "+transforms.size()+" object transforms.");
		/*for (Runecrafting.ExitPortal portal : Runecrafting.ExitPortal.values()) {
			transforms.put(portal.getID(), Portal.class);
		}*/
	}
	
	public static RS3Object create (int id, int rotation, int type, Tile tile) {
		if (transforms.containsKey(id)) {
			Class<? extends RS3Object> transform = transforms.get(id);
			if(transform.equals(FarmingPatch.class)) {
				return new FarmingPatch(id, rotation, type, tile, FarmingPatch.Definition.forID(id));
			} else if (transform.equals(RunecraftingAlter.class)) {
				return new RunecraftingAlter(id, rotation, type, tile, AlterDefinition.forAlterID(id));
			} else if (transform.equals(MysteriousRunes.class)) {
				return new MysteriousRunes(id, rotation, type, tile, MysteriousRunes.Definition.forID(id));
			}
		}
		Runecrafting.ExitPortal portal = Runecrafting.ExitPortal.forID(id);
		if (portal != null) {
			return new Portal(id, rotation, type, tile, portal.getDestination());
		}
		RS3Object object = new RS3Object(id, rotation, type, tile);
		switch (object.getDefinition().getName().toLowerCase()) {
			case "clay rocks":
			case "clay vein":
				return new MiningRock(object, Ore.CLAY);
			case "copper ore rocks":
			case "copper ore vein":
				return new MiningRock(object, Ore.COPPER);
			case "tin ore rocks":
			case "tin ore vein":
				return new MiningRock(object, Ore.TIN);
			case "iron ore rocks":
			case "iron ore vein":
				return new MiningRock(object, Ore.IRON);
			case "silver ore rocks":
				return new MiningRock(object, Ore.SILVER);
			case "coal rocks":
				return new MiningRock(object, Ore.COAL);
			case "gold ore rocks":
				return new MiningRock(object, Ore.GOLD);
			case "mithril ore rocks":
				return new MiningRock(object, Ore.MITHRIL);
			case "adamantite ore rocks":
				return new MiningRock(object, Ore.ADAMANTITE);
			case "runite ore rocks":
				return new MiningRock(object, Ore.RUNITE);
			case "tree":
				return new WoodcuttingTree(object, TreeDefinition.NORMAL);
			case "oak tree":
			case "oak":
				return new WoodcuttingTree(object, TreeDefinition.OAK);
			case "willow tree":
			case "willow":
				return new WoodcuttingTree(object, TreeDefinition.WILLOW);
			case "teak":
				return new WoodcuttingTree(object, TreeDefinition.TEAK);
			case "maple tree":
			case "maple":
				return new WoodcuttingTree(object, TreeDefinition.MAPLE);
			case "mahogany":
				return new WoodcuttingTree(object, TreeDefinition.MAHOGANY);
			case "yew tree":
			case "yew":
				return new WoodcuttingTree(object, TreeDefinition.YEW);
			case "magic tree":
				return new WoodcuttingTree(object, TreeDefinition.MAGIC);
			case "elder tree":
				return new WoodcuttingTree(object, TreeDefinition.ELDER);
		}
		return object;
	}
	
	/**
	 * Represents the ID of this object.
	 */
	private int id;
	
	/**
	 * Represents the rotation of this object.
	 */
	private int rotation;
	
	/**
	 * Represents the type of this object.
	 */
	private int type;
	
	/**
	 * Represents the cache information for the object	
	 */
	private ObjectDefinition definition;
	
	/**
	 * Constructs a new {@code RS3Object.java}.
	 * @param id The id.
	 * @param rotation The rotation.
	 * @param type The type.
	 */
	protected RS3Object(int id, int rotation, int type, Tile tile) {
		this.id = id;
		this.rotation = rotation;
		this.type = type;
		super.setTile(tile);
		this.definition = ObjectDefinitionLoader.forId(id);
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	protected void setId(int id) {
		this.id = id;
		this.definition = ObjectDefinitionLoader.forId(id);
	}

	/**
	 * @return the rotation
	 */
	public int getRotation() {
		return rotation;
	}

	/**
	 * @param rotation the rotation to set
	 */
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * @return the x-axis size of the object
	 */
	public int getSizeX() {
		return (rotation != 1 && rotation != 3) ? definition.getSize()[0] : definition.getSize()[1];
	}
	
	/**
	 * @return the y-axis size of the object
	 */
	public int getSizeY() {
		return (rotation != 1 && rotation != 3) ? definition.getSize()[1] : definition.getSize()[0];
	}
	
	/**
	 * @return	The cache definition for the object
	 */
	public ObjectDefinition getDefinition () {
		return definition;
	}
	
	/**
	 * Returns whether the given option can be handled at a distance
	 * @param option	The option to check
	 * @return			True if the option can be handled at a distance, false if the player must be adjacent first
	 */
	public boolean isDistanceOption (ObjectOption option) {
		return option.equals(ObjectOption.EXAMINE);
	}
	
	/**
	 * Handles the player interaction with this object
	 * @param player	The player interacting with the object
	 * @param option	The selected interaction option
	 */
	public void interact (Player player, ObjectOption option) {
		if (Bank.isBankBooth(this) && option.equals(ObjectOption.TWO)) {
			player.getInterfaces().openBank();
			return;
		}
		String message = "This feature has not been implemented.";
		if (Constants.DEVELOPER_MODE || player.getAccount().getRank().equals(Rank.ADMINISTRATOR)) {
			message = "Clicked object: objectID="+id+", type="+type+", x="+getTile().getX()+", y="+getTile().getY()+", z="+getTile().getPlane()+", optionID="+option.getID();
			System.out.println(message);
		}
		player.getPacketDispatcher().dispatchMessage(message);
	}

	public void useItem (Player player, Item item) {
		String message = "Nothing interesting happens.";
		if (Constants.DEVELOPER_MODE || player.getAccount().getRank().equals(Rank.ADMINISTRATOR)) {
			message = "Used item on object: objectID="+id+", type="+type+", itemID"+item.getId()+", x="+getTile().getX()+", y="+getTile().getY()+", z="+getTile().getPlane();
			System.out.println(message);
		}
		player.getPacketDispatcher().dispatchMessage(message);
	}
}
