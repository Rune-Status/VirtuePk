package org.virtue.network.protocol.handlers;

import org.virtue.game.core.AttributeSet;
import org.virtue.network.session.Session;

/**
 * @author Taylor Moon
 * @since Jan 23, 2014
 */
public abstract class PacketHandler<T extends Session> extends AttributeSet {

	/**
	 * Called when a packet should be handled.
	 */
	public abstract void handle(T session);
}
