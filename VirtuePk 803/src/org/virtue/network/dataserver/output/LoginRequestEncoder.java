package org.virtue.network.dataserver.output;

import org.virtue.game.node.entity.player.Player;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

/**
 * @author Taylor
 * @date Jan 15, 2014
 */
public class LoginRequestEncoder implements PacketEncoder<Player> {

	@Override
	public RS3PacketBuilder buildPacket(Player node) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.put(1);
		buffer.putString(node.getAccount().getUsername().getAccountName());
		return buffer;
	}
}
