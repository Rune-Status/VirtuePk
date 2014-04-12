package org.virtue.network.protocol.packet;


/**
 * @author Taylor
 * @version 1.0
 */
public class RS3Packet {
	
	/**
	 * The opcode
	 */
	private final int opcode;

	/**
	 * The buffer
	 */
	private final RS3HeapBuffer buffer;
	
	/**
	 * Constructs a new {@code Packet.java}
	 * @param opcode The opcode of this packet
	 * @param buffer The buffer payload
	 */
	public RS3Packet(int opcode, RS3HeapBuffer buffer) {
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
	public RS3HeapBuffer getBuffer() {
		return buffer;
	}
	
}
