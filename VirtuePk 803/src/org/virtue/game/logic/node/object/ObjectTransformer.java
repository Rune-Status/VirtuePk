package org.virtue.game.logic.node.object;

import org.virtue.game.logic.content.skills.mining.MiningRock;
import org.virtue.game.logic.content.skills.mining.Ore;

public class ObjectTransformer {

	public static RS3Object transformObject (RS3Object object) {
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
		}
		return object;
	}
}
