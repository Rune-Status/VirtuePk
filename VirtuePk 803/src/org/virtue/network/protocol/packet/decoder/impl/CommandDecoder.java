package org.virtue.network.protocol.packet.decoder.impl;

import org.virtue.config.IncommingOpcodes;
import org.virtue.network.protocol.handlers.impl.CommandHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.protocol.packet.decoder.PacketDecoder;
import org.virtue.network.session.Session;

/**
 * @author Taylor
 * @version 1.0
 */
public class CommandDecoder implements PacketDecoder<CommandHandler> {

	/**
	 * (non-Javadoc)
	 * @see com.psyc.net.codec.decoder.PacketDecoder#decode(com.psyc.iobuffer.PsycInBuffer, com.psyc.net.codec.connect.Session)
	 */
	@Override
	public CommandHandler decodePacket(RS3PacketReader buffer, Session session, int opcode) {
		CommandHandler handler = new CommandHandler();
		/*
		 * Represents if the command that was executed is a client operated
		 * command, and is in the client by default.
		 */
		boolean clientCommand = (buffer.get() & 0xFF) == 1;
		/*
		 * Can't remember, TODO find out.
		 */
		buffer.get();
		/*
		 * Represents the command as a single string. The arguments will have to
		 * be sifted manually.
		 */
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
