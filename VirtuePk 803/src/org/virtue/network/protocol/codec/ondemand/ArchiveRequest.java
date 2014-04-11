package org.virtue.network.protocol.codec.ondemand;

/**
 * @author Taylor
 * @date Jan 16, 2014
 */
public final class ArchiveRequest {

	/**
	 * Priority over other files
	 */
	private final boolean priority;

	/**
	 * The file and type
	 */
	private final int type, file;

	/**
	 * Constructs a new file request
	 * @param priority Pritority over other files
	 * @param type The type
	 * @param file The file
	 */
	public ArchiveRequest(boolean priority, int type, int file) {
		this.priority = priority;
		this.type = type;
		this.file = file;
	}

	/**
	 * Returns if priority
	 * 
	 * @return The priority
	 */
	public boolean isPriority() {
		return priority;
	}

	/**
	 * Returns the type
	 * 
	 * @return The type
	 */
	public int getType() {
		return type;
	}

	/**
	 * Returns the file
	 * 
	 * @return The file
	 */
	public int getFile() {
		return file;
	}
}
