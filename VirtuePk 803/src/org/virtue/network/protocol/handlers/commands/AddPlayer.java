package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.logic.World;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.identity.Account;
import org.virtue.game.logic.node.entity.player.identity.Username;
import org.virtue.game.logic.region.Tile;
import org.virtue.network.protocol.messages.GameMessage.MessageOpcode;

public class AddPlayer implements Command {

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		int x, y;
		String username = "Test44";
		if (args.length < 2) {
			player.getPacketDispatcher().dispatchMessage("At least two arguments are required (x, y).", MessageOpcode.CONSOLE);
			return false;
		}
		try {
			x = Integer.parseInt(args[0]);
			y = Integer.parseInt(args[1]);
			if (args.length >= 1) {
				username = args[2];
			}
		} catch (NumberFormatException ex) {
			player.getPacketDispatcher().dispatchMessage("At least two arguments are required (x, y).", MessageOpcode.CONSOLE);
			return false;
		}
		Account account = new Account(new Username(username), null, null, null, 0L, 0L);
		Player p = new Player(account);
		p.setTile(new Tile(x, y, 0));
		//p.getChatManager().getFriendManager().init();
		World.getWorld().addPlayer(p);
		return true;
	}

	@Override
	public String[] getPossibleSyntaxes() {
		return new String[] { "addplayer" };
	}

}
