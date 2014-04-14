package org.virtue.network.session.impl;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.virtue.network.protocol.codec.creation.AccountCreationResponse;
import org.virtue.network.protocol.codec.creation.PersonalDetails;
import org.virtue.network.protocol.codec.creation.UsernameRequest;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.session.Session;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 14, 2014
 */
public class AccoutCreationSession extends Session {

	/**
	 * Constructs a new {@code AccoutCreationSession.java}.
	 * @param context
	 */
	public AccoutCreationSession(ChannelHandlerContext context) {
		super(context);
	}

	@Override
	public void decode(Object message) {
		if (message instanceof PersonalDetails) {
			RS3PacketBuilder buffer = new RS3PacketBuilder();
			buffer.put(AccountCreationResponse.STATUS_OK);
			getTransmitter().send(buffer);
		} else if (message instanceof UsernameRequest) {
			UsernameRequest request = (UsernameRequest) message;
			String username = request.getUsername();
			RS3PacketBuilder buffer = new RS3PacketBuilder();
			buffer.put(AccountCreationResponse.STATUS_OK);
			getTransmitter().send(buffer);
		}
	}

	@Override
	public void disconnect() {
	}

}
