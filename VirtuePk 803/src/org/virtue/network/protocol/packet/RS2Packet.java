package org.virtue.network.protocol.packet;


/**
 * @author Taylor
 * @version 1.0
 */
public class RS2Packet {
	
	/**
	 * The opcode
	 */
	private final int opcode;

	/**
	 * The buffer
	 */
	private final RS2HeapBuffer buffer;
	
	/**
	 * Constructs a new {@code Packet.java}
	 * @param opcode The opcode of this packet
	 * @param buffer The buffer payload
	 */
	public RS2Packet(int opcode, RS2HeapBuffer buffer) {
		this.opcode = opcode;
		this.buffer = buffer;
	}

	/**
	 * @return The opcode
	 */
	public int getOpcode() {
		return opcode;
	}

	/**
	 * @return The buffer
	 */
	public RS2HeapBuffer getBuffer() {
		return buffer;
	}
	
}
