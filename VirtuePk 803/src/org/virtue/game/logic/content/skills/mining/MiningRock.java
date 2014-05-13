package org.virtue.game.logic.content.skills.mining;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.identity.Rank;
import org.virtue.game.logic.node.object.ObjectOption;
import org.virtue.game.logic.node.object.RS3Object;
import org.virtue.game.logic.node.object.TemporaryObject;
import org.virtue.game.logic.region.Tile;

public class MiningRock extends TemporaryObject {
	
	public static int getEmptyRockID (RS3Object object) {
		switch (object.getDefinition().modelIDs[0][0]) {
		/*5766 to 5786*/
		case 65251:
			return 5765;
		case 65252:
			return 5763;
		case 65253:
			return 5764;
			/*78911,78912,78913*///88880, 3042, 3043, 3227, 3281,6947, 6948, 9723, 9724, 9725
		default:
			System.out.println("Unknown mining rock model: "+object.getDefinition().modelIDs[0][0]+" for rock: "+object.getId());
			return 5764;
		}
	}
	
	private final Ore ore;

	public MiningRock(int id, int rotation, int type, Tile tile, int replacementID, Ore ore) {
		super(id, rotation, type, tile, replacementID, ore.getRespawnTime());
		this.ore = ore;
	}
	
	public MiningRock(RS3Object object, Ore ore) {
		super(object.getId(), object.getRotation(), object.getType(), object.getTile(), getEmptyRockID(object), ore.getRespawnTime());
		this.ore = ore;
	}
	
	public Ore getOre() {
		return ore;
	}
	
	@Override
	public void interact (Player player, ObjectOption option) {
		String message = "Clicked mining rock: id="+getId()+", rotation="+getRotation()+", xCoord="+getTile().getX()+", yCoord="+getTile().getX()+", optionID="+option.getID();
		System.out.println(message);
		if (player.getAccount().getRank().equals(Rank.ADMINISTRATOR)) {
			player.setActionEvent(new MiningAction(this));
			/*player.getInventory().add(new Item(ore.getOreID(), 1), true);
			player.getPacketDispatcher().dispatchMessage(message);
			super.deplete();*/	
		}
	}

}
