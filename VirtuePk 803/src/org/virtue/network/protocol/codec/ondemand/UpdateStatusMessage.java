package org.virtue.network.protocol.codec.ondemand;

/**
 * @author Graham
 */
public final class UpdateStatusMessage {

	/**
	 * OK status
	 */
	public static final int STATUS_OK = 0;

	/**
	 * Out of date satus
	 */
	public static final int STATUS_OUT_OF_DATE = 6;

	/**
	 * Server full status
	 */
	public static final int STATUS_SERVER_FULL = 7;

	/**
	 * The status
	 */
	private final int status;

	/**
	 * The version
	 */
	private int version;

	/**
	 * Constructs this message
	 * 
	 * @param status The status
	 * @param version The version
	 */
	public UpdateStatusMessage(int status, int version) {
		this.status = status;
		this.version = version;
	}

	/**
	 * Returns the status
	 * 
	 * @return The status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Returns the version
	 * 
	 * @return The version
	 */
	public int version() {
		return version;
	}
}
