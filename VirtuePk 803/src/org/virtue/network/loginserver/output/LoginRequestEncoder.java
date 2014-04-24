package org.virtue.network.loginserver.output;

import org.virtue.game.logic.node.entity.player.identity.Account;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

/**
 * @author Taylor
 * @date Jan 15, 2014
 */
public class LoginRequestEncoder implements PacketEncoder<Account> {

	@Override
	public RS3PacketBuilder buildPacket(Account node) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.put(1);
		buffer.putString(node.getUsername().getAccountName());
		return buffer;
	}
}
