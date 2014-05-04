package org.virtue.network.protocol.packet.encoder.impl;

import java.util.Arrays;

import org.virtue.game.config.OutgoingOpcodes;
import org.virtue.game.logic.item.Item;
import org.virtue.network.protocol.messages.ItemsMessage;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

public class ItemsEncoder implements PacketEncoder<ItemsMessage> {

	@Override
	public RS3PacketBuilder buildPacket(ItemsMessage node) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacketVarShort(node.isFullUpdate() ? OutgoingOpcodes.ITEMS_PACKET : OutgoingOpcodes.ITEMS_UPDATE_PACKET);
		buffer.putShort(node.getKey());
		buffer.put(node.isNegitiveKey() ? 0x1 : 0);
		if (node.isFullUpdate()) {
			buffer.putShort(node.getItems().length);
			packFull(node.getItems(), buffer);
		} else {
			packUpdate(node.getUpdateSlots(), node.getItems(), buffer);
		}
		/*for (Item item : node.getItems()) {
			int id = -1;
			int amount = 0;
			if (item != null) {
				id = item.getId();
				amount = item.getAmount();
			}
			buffer.putByteS(amount > 255 ? 255 : amount);
			if (amount >= 255) {
				buffer.putLEInt(amount);
			}
			buffer.putShort(id + 1);
		}*/
		buffer.endPacketVarShort();
		return buffer;
	}
	
	private void packFull (Item[] items, RS3PacketBuilder buffer) {
		for (Item item : items) {
			int id = -1;
			int amount = 0;
			if (item != null) {
				id = item.getId();
				amount = item.getAmount();
			}
			buffer.putByteS(amount > 255 ? 255 : amount);
			if (amount >= 255) {
				buffer.putLEInt(amount);
			}
			buffer.putShort(id + 1);
		}
	}
	
	private void packUpdate (int[] slots, Item[] items, RS3PacketBuilder buffer) {
		for (int slotID : slots) {
			buffer.putSmart(slotID);
			int id = -1;
			int amount = 0;
			if (items[slotID] != null) {
				id = items[slotID].getId();
				amount = items[slotID].getAmount();
			}
			buffer.putShort(id + 1);
			if (id != -1) {
				buffer.put(amount > 255 ? 255 : amount);
				if (amount >= 255) {
					buffer.putInt(amount);
				}
			}
		}
	}

}
