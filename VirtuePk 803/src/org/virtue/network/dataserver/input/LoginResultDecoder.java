package org.virtue.network.dataserver.input;

import org.virtue.network.dataserver.handlers.LoginResultHandler;
import org.virtue.network.dataserver.message.LoginResult;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

/**
 * @author Taylor
 * @date Jan 16, 2014
 */
public class LoginResultDecoder implements PacketDecoder<LoginResultHandler> {

	@Override
	public LoginResultHandler decodePacket(RS3PacketReader buffer, Session session, int opcode) {
		LoginResultHandler handler = new LoginResultHandler();
		handler.putFlag("login_result", LoginResult.forOpcode(buffer.getInt()));
		handler.putFlag("username", buffer.getString());
		return handler;
	}

	@Override
	public int[] getPossiblePackets() {
		return new int[] { 1 };
	}
}
