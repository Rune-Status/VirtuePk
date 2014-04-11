package org.virtue.network.protocol.codec.ondemand;

/**
 * @author Taylor Moon
 * @since Feb 8, 2014
 */
public enum InitialRequest {

	/**
	 * Represents the client requesting a cache archive.
	 */
	ARCHIVE_REQUEST(0),
	
	/**
	 * Represents the client representing an index 255 cache archive.
	 */
	PRIORITY_ARCHIVE_REQUEST(1), 
	
	/**
	 * Represents the client requesting an update in the archive encryption.
	 */
	VERSION_ENCRYPTION_REQUEST(4), 
	
	/**
	 * Represents the client sending an error request.
	 */
	NULL_REQUEST(7);

	/**
	 * Represents the opcode of the request.
	 */
	private final int opcode;

	/**
	 * Constructs a new {@code CacheRequest.java}.
	 * @param opcode The opcode of the request.
	 */
	InitialRequest(final int opcode) {
		this.opcode = opcode;
	}

	/**
	 * @return the opcode
	 */
	public int getOpcode() {
		return opcode;
	}
	
	/**
	 * Returns an initial request stage for the incoming opcode.
	 * @param opcode The opcode that was sent.
	 * @return The request.
	 */
	public static InitialRequest forOpcode(int opcode) {
		for (InitialRequest request : values()) {
			if (request.getOpcode() == opcode) {
				return request;
			}
		}
		return null;
	}
}
