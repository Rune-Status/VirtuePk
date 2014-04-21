package org.virtue.game.logic.node.entity.player.container;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public enum EquipSlot {
    HAT(0), CAPE(1), AMULET(2), MAINHAND(3), CHEST(4),
    OFFHAND(5), LEGS(7), HANDS(9), FEET(10),
    RING(12), ARROWS(13), AURA(14), POCKET(15);
    
    private final int id;
    
    EquipSlot (int slotID) {
        this.id = slotID;
    }
    
    public int getSlotID () {
		return id;
    }
    
    public static EquipSlot forID (int id) {
		if (id < EquipSlot.values().length) {
			EquipSlot slot = EquipSlot.values()[id];
			if (slot.id == id) {
				return slot;
			}
		}
		for (EquipSlot b : EquipSlot.values()) {
			if (b.id == id) {
			return b;
			}
		}
		return null;
    }
}
