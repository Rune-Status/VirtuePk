package org.virtue.network.protocol.packet.decoder;

import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.protocol.packet.RS3PacketReader;
import org.virtue.network.session.Session;

/**
 * @author Taylor Moon
 * @since Jan 23, 2014
 * @param <T> The handler expected to handle the decoded structure.
 */
public interface PacketDecoder<T extends PacketHandler<? extends Session>> {

	/**
	 * Called when the RS2Network recieves a decodable packet from the client.
	 * @param packet The packet being decoded.
	 * @param session The bound session to this decoder.
	 * @param opcode The opcode passed through the decoder.
	 * @return The handler of the recently decoded packet.
	 */
	T decodePacket(RS3PacketReader packet, Session session, int opcode);
	
	/**
	 * Returns an array of possible opcodes that will activate this decoder.
	 * @return The opcodes holding the possibility of activating this decoder.
	 */
	int[] getPossiblePackets();
}
