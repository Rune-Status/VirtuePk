package org.virtue.network.io.channel;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.entity.player.identity.Account;
import org.virtue.game.logic.node.entity.player.identity.Username;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 18, 2014
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Account account = new Account(new Username("Test44"), null, null, null, 0L, 0L);
		Player p = new Player(account);
	}

}
