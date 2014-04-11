package org.virtue.network.protocol.codec.ondemand;

/**
 * @author Taylor
 * @date Jan 16, 2014
 */
public final class UpdateEncryptionMessage {

	/**
	 * The key
	 */
	private final int key;

	/**
	 * Constructs this message
	 * @param key The key
	 */
	public UpdateEncryptionMessage(int key) {
		this.key = key;
	}

	/**
	 * Returns the key
	 * 
	 * @return The key
	 */
	public int getKey() {
		return key;
	}

}
