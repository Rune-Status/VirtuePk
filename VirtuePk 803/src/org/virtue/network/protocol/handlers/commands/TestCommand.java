package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.node.entity.player.Player;
import org.virtue.network.protocol.packet.encoder.impl.PlayerEncoder;

/**
 * @author Taylor Moon
 * @since Jan 26, 2014
 */
public class TestCommand implements Command {

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		player.getAccount().getSession().getTransmitter().send(PlayerEncoder.class, player);
//		String text = player.requestInput("Enter Amount:");
//		player.getPacketDispatcher().dispatchMessage(text);
		return true;
	}

	@Override
	public String[] getPossibleSyntaxes() {
		return new String[] { "test" };
	}
}
