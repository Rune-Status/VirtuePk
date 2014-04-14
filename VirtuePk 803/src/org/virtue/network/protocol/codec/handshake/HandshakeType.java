package org.virtue.network.protocol.codec.handshake;

/**
 * @author Taylor
 * @version 1.0
 */
public final class HandshakeType {

	/**
	 * @author Taylor
	 * @version 1.0
	 */
	public static enum HandshakeTypes {

		/**
		 * Js5 connection
		 */
		HANDSHAKE_LOGIN,
		
		/**
		 * Account creation
		 */
		HANSHAKE_CREATION,

		/**
		 * Js5 container ondemand
		 */
		HANDSHAKE_ONDEMAND
	}

	/**
	 * The type
	 */
	private HandshakeTypes type;

	/**
	 * Constructs a new {@code HandshakeType} instance.
	 * @param opcode The incomming handshake opcode.
	 */
	public HandshakeType(int opcode) {
		this.type = forOpcode(opcode);
	}

	/**
	 * Gets the handshake type based on the incomming opcode.
	 * @param opcode The incomming opcode from the handshake state.
	 * @return The {@link HandshakeTypes} for the opcode.
	 */
	public HandshakeTypes forOpcode(int opcode) {
		switch (opcode) {
		case 15:
			return HandshakeTypes.HANDSHAKE_ONDEMAND;
		case 14:
			return HandshakeTypes.HANDSHAKE_LOGIN;
		case 28:
			return HandshakeTypes.HANSHAKE_CREATION;
		}
		throw new IllegalStateException("No such state for incomming opcode: " + opcode);
	}

	/**
	 * Gets the current type of the handshake.
	 * 
	 * @return The handshake type.
	 */
	public HandshakeTypes getType() {
		return type;
	}
}
