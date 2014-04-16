package org.virtue.network.protocol.handlers.commands;

import org.virtue.game.World;
import org.virtue.game.node.entity.player.Player;
import org.virtue.game.node.entity.player.identity.Account;
import org.virtue.game.node.entity.player.identity.Username;
import org.virtue.game.node.entity.region.Tile;

public class AddPlayer implements Command {

	@Override
	public boolean handle(String syntax, Player player, boolean clientCommand, String... args) {
		Account account = new Account(new Username("Test44"), null, null, null, 0L, 0L);
		Player p = new Player(account);
		p.setTile(new Tile(3203, 3203, 0));
		World.getWorld().addPlayer(p);
		return true;
	}

	@Override
	public String[] getPossibleSyntaxes() {
		return new String[] { "addplayer" };
	}

}
