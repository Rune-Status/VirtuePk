package org.virtue.network.loginserver.output;

import org.virtue.game.node.entity.player.identity.Account;
import org.virtue.network.protocol.codec.login.LoginType;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

/**
 * @author Taylor
 * @date Jan 16, 2014
 */
public class LoginVerificationEncoder implements PacketEncoder<Account> {

	@Override
	public RS3PacketBuilder buildPacket(Account node) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.put(2);
		buffer.putString(node.getUsername().getAccountName());
		buffer.put(node.getFlag("login_type", LoginType.LOBBY).ordinal());
		return buffer;
	}
}
