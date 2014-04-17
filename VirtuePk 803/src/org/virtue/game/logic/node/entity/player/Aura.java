package org.virtue.game.logic.node.entity.player;

import org.virtue.game.logic.item.Item;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 16, 2014
 */
public class Aura {
	
	public static enum AuraGraphics {
		RED(1763), GREEN(1764), YELLOW(-1);

		private int id;

		AuraGraphics(int id) {
			this.id = id;
		}

		public static AuraGraphics valueOf(Item item) {
			switch (item.getId()) {
			// TODO: cases..
			default:
				return RED;
			}
		}

		public int getId() {
			return id;
		}
	}
	
	//public static final Animation ACTIVATE_ANIMATION = new Animation(2231);
	public boolean activated;
	public int charge;

}
