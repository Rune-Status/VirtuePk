package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.identity.Rank;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;

public class SetNoClip implements Command {

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		if (!player.getAccount().getRank().equals(Rank.ADMINISTRATOR)) {
			return false;
		}
		if ("noclip".equalsIgnoreCase(syntax)) {
			player.getUpdateArchive().getMovement().setNoClip(true);
			player.getPacketDispatcher().dispatchMessage("Noclip enabled.", MessageOpcode.CONSOLE);
			return true;
		} else if ("clip".equalsIgnoreCase(syntax)) {
			player.getUpdateArchive().getMovement().setNoClip(false);
			player.getPacketDispatcher().dispatchMessage("Noclip disabled.", MessageOpcode.CONSOLE);
			return true;
		}
		return false;
	}

	@Override
	public String[] getPossibleSyntaxes() {
		return new String[] { "toggleclip", "clip", "noclip" };
	}

}
