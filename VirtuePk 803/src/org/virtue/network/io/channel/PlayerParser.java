package org.virtue.network.io.channel;

import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.network.io.IOParser;
import org.virtue.network.protocol.packet.RS3PacketReader;

/**
 * @author Taylor
 * @version 1.0
 */
public class PlayerParser implements IOParser<Player, RS3PacketReader> {

	/**
	 * (non-Javadoc)
	 * @see org.virtue.network.io.IOParser#load(java.lang.Object)
	 */
	@Override
	public Player load(RS3PacketReader buffer) {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * @see org.virtue.network.io.IOParser#getPath()
	 */
	@Override
	public String getPath() {
		return "data/accounts/";
	}

	@Override
	public boolean save(Object... params) {
		return false;
	}

}
