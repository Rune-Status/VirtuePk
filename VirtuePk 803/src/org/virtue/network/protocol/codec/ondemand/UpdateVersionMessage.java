package org.virtue.network.protocol.codec.ondemand;

/**
 * @author Taylor
 * @date Jan 16, 2014
 */
public final class UpdateVersionMessage {

	/**
	 * The version
	 */
	private final int version;

	/**
	 * The sub version
	 */
	private final int subVersion;

	/**
	 * The key
	 */
	private final String key;

	/**
	 * Constructs a new message
	 * 
	 * @param version
	 *            The version
	 */
	public UpdateVersionMessage(int version) {
		this(version, 0, null);
	}

	/**
	 * Constructs a new message
	 * @param version The version
	 * @param subVersion The sub version
	 * @param key The key
	 */
	public UpdateVersionMessage(int version, int subVersion, String key) {
		this.version = version;
		this.subVersion = subVersion;
		this.key = key;
	}

	/**
	 * Returns the version
	 * 
	 * @return The version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Returns the sub version
	 * 
	 * @return The sub version
	 */
	public int getSubVersion() {
		return subVersion;
	}

	/**
	 * Returns the key
	 * 
	 * @return The key
	 */
	public String getKey() {
		return key;
	}
}
