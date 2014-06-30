package org.virtue.network.protocol.codec.handshake;

/**
 * @author Virtue Development
 * @version 1.0
 */
public final class HandshakeType {

	/**
	 * @author Virtue Development
	 * @version 1.0
	 */
	public static enum HandshakeTypes {

		/**
		 * Login connection
		 */
		HANDSHAKE_LOGIN,
		
		/**
		 * Account creation
		 */
		HANSHAKE_CREATION,

		/**
		 * Js5 container ondemand
		 */
		HANDSHAKE_ONDEMAND,
		
		/**
		 * "header" RSA Block for login
		 */
		HANDSHAKE_INIT_SOCIAL_NETWORK,
		
		/**
		 * "footer" XTEA Block for login
		 */
		HANDSHAKE_SOCIAL_NETWORK_LOGIN
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
		case 29:
			return HandshakeTypes.HANDSHAKE_INIT_SOCIAL_NETWORK;
		//case 30:
			//return HandshakeTypes.HANDSHAKE_SOCIAL_NETWORK_LOGIN;
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
