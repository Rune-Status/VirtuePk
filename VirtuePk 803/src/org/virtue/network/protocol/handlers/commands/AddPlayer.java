package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.logic.World;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.identity.Account;
import org.virtue.game.logic.node.entity.player.identity.Username;
import org.virtue.game.logic.node.entity.region.Tile;

public class AddPlayer implements Command {

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		String username = "Test44";
		if (args.length >= 1) {
			username = args[0];
		}
		Account account = new Account(new Username(username), null, null, null, 0L, 0L);
		Player p = new Player(account);
		p.setTile(new Tile(player.getTile().getX()+2, player.getTile().getY()+2, 0));
		//p.getChatManager().getFriendManager().init();
		World.getWorld().addPlayer(p);
		return true;
	}

	@Override
	public String[] getPossibleSyntaxes() {
		return new String[] { "addplayer" };
	}

}
