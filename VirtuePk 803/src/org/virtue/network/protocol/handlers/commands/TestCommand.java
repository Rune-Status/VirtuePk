package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.network.protocol.packet.RS3PacketBuilder;

/**
 * @author Taylor Moon
 * @since Jan 26, 2014
 */
public class TestCommand implements Command {

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
                RS3PacketBuilder buffer = new RS3PacketBuilder();
                buffer.putPacket(138);
                buffer.put(49);
                buffer.putByteS(51);
		player.getAccount().getSession().getTransmitter().send(buffer);
//		String text = player.requestInput("Enter Amount:");
//		player.getPacketDispatcher().dispatchMessage(text);
		return true;
	}

	@Override
	public String[] getPossibleSyntaxes() {
		return new String[] { "test" };
	}
}
