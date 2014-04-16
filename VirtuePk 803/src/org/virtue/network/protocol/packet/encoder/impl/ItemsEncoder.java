package org.virtue.network.protocol.packet.encoder.impl;

import org.virtue.config.OutgoingOpcodes;
import org.virtue.game.item.Item;
import org.virtue.network.protocol.messages.ItemsMessage;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

public class ItemsEncoder implements PacketEncoder<ItemsMessage> {

	@Override
	public RS3PacketBuilder buildPacket(ItemsMessage node) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacketVarShort(OutgoingOpcodes.ITEMS_CONTAINER);
		buffer.putShort(node.getKey());
		buffer.put(node.isNegitiveKey() ? 0x1 : 0);
		buffer.putShort(node.getItems().length);
		for (Item item : node.getItems()) {
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
		buffer.endPacketVarShort();
		return buffer;
	}

}
