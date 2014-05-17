package org.virtue.game.logic.node.object;

import org.virtue.game.logic.content.skills.mining.MiningRock;
import org.virtue.game.logic.content.skills.mining.Ore;
import org.virtue.game.logic.content.skills.runecrafting.MysteriousRunes;
import org.virtue.game.logic.content.skills.runecrafting.RunecraftingAlter;
import org.virtue.game.logic.content.skills.runecrafting.AlterDefinition;
import org.virtue.game.logic.content.skills.woodcutting.Log;
import org.virtue.game.logic.content.skills.woodcutting.WoodcuttingTree;

public class ObjectTransformer {

	/**
	 * Transforms a generic object into a specific object (eg tree, rock, etc)
	 * @param object	The object to transform
	 * @return			The transformed object
	 */
	public static RS3Object transformObject (RS3Object object) {
		AlterDefinition alter = AlterDefinition.forAlterID(object.getId());
		if (alter != null) {
			return new RunecraftingAlter(object, alter);
		}
		MysteriousRunes.Definition runes = MysteriousRunes.Definition.forID(object.getId());
		if (runes != null) {
			return new MysteriousRunes(object, runes);
		}
		if (object.getId() == 30624) {
			System.out.println("Blood alter at "+object.getTile()+", type="+object.getType()+", rotation="+object.getRotation());
		}
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
			return new WoodcuttingTree(object, Log.NORMAL);
		case "oak tree":
		case "oak":
			return new WoodcuttingTree(object, Log.OAK);
		case "willow tree":
		case "willow":
			return new WoodcuttingTree(object, Log.WILLOW);
		case "teak":
			return new WoodcuttingTree(object, Log.TEAK);
		case "maple tree":
		case "maple":
			return new WoodcuttingTree(object, Log.MAPLE);
		case "mahogany":
			return new WoodcuttingTree(object, Log.MAHOGANY);
		case "yew tree":
		case "yew":
			return new WoodcuttingTree(object, Log.YEW);
		case "magic tree":
			return new WoodcuttingTree(object, Log.MAGIC);
		case "elder tree":
			return new WoodcuttingTree(object, Log.ELDER);
		}
		return object;
	}
}
