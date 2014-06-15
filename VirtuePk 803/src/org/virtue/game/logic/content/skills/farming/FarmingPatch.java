package org.virtue.game.logic.content.skills.farming;

import org.virtue.Constants;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.identity.Rank;
import org.virtue.game.logic.node.object.ObjectOption;
import org.virtue.game.logic.node.object.RS3Object;
import org.virtue.game.logic.region.Tile;

public class FarmingPatch extends RS3Object {
	
	public static enum Definition {
		CHAMP_BUSH(7577, 76),
		RIM_BUSH(7578, 77),
		CACTUS(7771, 18416),
		FALADOR_FLOWER(7847, 72),
		CATH_FLOWER(7848, 73),
		ARDY_FLOWER(7849, 74),
		MORT_FLOWER(7850, 75),
		CATH_FRUIT_TREE(7965, 51),
		FALADOR_HERB(8150, 124),
		CATH_HERB(8151, 125),
		ARDY_HERB(8152, 126),
		MORT_HERB(8153, 127),
		ENTRANA_HOPS(8174, 61),
		LUM_HOPS(8175, 62),
		SEERS_HOPS(8176, 63),
		MUSHROOM(8337, 90),
		PORT_SPIRIT_TREE(8338, 64),
		TAVERLY_TREE(8388, 44),
		FALADOR_TREE(8389, 45),
		VARROCK_TREE(8390, 46),
		LUM_TREE(8391, 47),
		FALADOR_ALLOTMENT_1(8550, 52),
		FALADOR_ALLOTMENT_2(8551, 53),
		CATH_ALLOTMENT_1(8552, 54),
		CATH_ALLOTMENT_2(8553, 55),
		ARDY_ALLOTMENT_1(8554, 56),
		ARDY_ALLOTMENT_2(8555, 57),
		MORT_ALLOTMENT_1(8556, 58),
		MORT_ALLOTMENT_2(8557, 59);
		
		private final int objectID;
		
		private final int varBitType;
		
		Definition (int objectID, int varBitType) {
			this.objectID = objectID;
			this.varBitType = varBitType;
		}
		
		public int getObjectID () {
			return objectID;
		}
		
		public int getVarBitType() {
			return varBitType;
		}
		
		public static Definition forID (int id) {
			for (Definition type : Definition.values()) {
				if (type.objectID == id) {
					return type;
				}
			}
			return null;
		}
	}
	
	private final Definition definition;

	public FarmingPatch(int id, int rotation, int type, Tile tile, Definition def) {
		super(id, rotation, type, tile);
		this.definition = def;
	}

	@Override
	public void interact (Player player, ObjectOption option) {
		String message = "This feature has not been implemented.";
		if (Constants.DEVELOPER_MODE || player.getAccount().getRank().equals(Rank.ADMINISTRATOR)) {
			message = "Clicked farming patch: objectID="+getId()+", x="+getTile().getX()+", y="+getTile().getY()+", z="+getTile().getPlane()+", optionID="+option.getID()+", varBitType="+definition.getVarBitType();
			System.out.println(message);
		}
		player.getPacketDispatcher().dispatchMessage(message);
	}
}
