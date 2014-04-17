package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.game.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.impl.CommandHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

public class CommandDecoder implements PacketDecoder<CommandHandler> {

	@Override
	public CommandHandler decodePacket(RS3PacketReader buffer, Session session, int opcode) {
		CommandHandler handler = new CommandHandler();
		boolean clientCommand = (buffer.get() & 0xFF) == 1;
		buffer.get();
		String command = buffer.getString();
		handler.putFlag("client_command", clientCommand);
		handler.putFlag("syntax", command);
		return handler;
	}

	/**
	 * (non-Javadoc)
	 * @see com.psyc.net.codec.decoder.PacketDecoder#getOpcode()
	 */
	@Override
	public int[] getPossiblePackets() {
		return new int[] { IncommingOpcodes.COMMANDS_PACKET };
	}
}
