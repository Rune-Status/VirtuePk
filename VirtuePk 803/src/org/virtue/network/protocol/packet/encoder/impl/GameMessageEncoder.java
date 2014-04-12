package org.virtue.network.protocol.packet.encoder.impl;

import org.virtue.config.OutgoingOpcodes;
import org.virtue.network.messages.GameMessage;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.encoder.PacketEncoder;

/**
 * @author Taylor
 * @version 1.0
 */
public class GameMessageEncoder implements PacketEncoder<GameMessage> {

	/**
	 * (non-Javadoc)
	 * @see com.psyc.net.codec.encoder.PacketEncoder#encode(java.lang.Object)
	 */
	@Override
	public RS3PacketBuilder buildPacket(GameMessage message) {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		buffer.putPacketVarByte(OutgoingOpcodes.MESSAGE_PACKET);
		/*
		 * Tells the client the type of message to dispatch.
		 */
		buffer.putSmart(message.getOpcode().getOpcode());
		/*
		 * Something to do with the client state. Probably not even used.
		 */
		buffer.putInt(message.getPlayer().getTile().getTileHash());
		int bitMask = 0;
		if (message.getPlayer().getAccount().getUsername().getName() != null) {
			/*
			 * Signifies that an account name is present.
			 */
			bitMask |= 0x1;
		}
		if (message.getPlayer().getAccount().getUsername().hasTitle()) {
			/*
			 * Signifies the player has a different reply-to name.
			 */
			bitMask |= 0x2;
		}
		buffer.put(bitMask);
		if (message.getPlayer().getAccount().getUsername().getName() != null) {
			/*
			 * Writes the display name.
			 */
			buffer.putString(message.getPlayer().getAccount().getUsername().getNameWithTitles());
		}
		if (message.getPlayer().getAccount().getUsername().hasTitle()) {
			/*
			 * Writes the protocol display name.
			 */
			buffer.putString(message.getPlayer().getAccount().getUsername().getNameAsProtocol().replace('_', ' '));
		}
		/*
		 * Writes the message output.
		 */
		buffer.putString(message.getMessage());
		buffer.endPacketVarByte();
		return buffer;
	}

}
