package org.virtue.network.protocol.codec.ondemand;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author Taylor Moon
 * @since Feb 8, 2014
 */
public final class FileResponse {

	/**
	 * Priority over other files
	 */
	private final boolean priority;

	/**
	 * The file and type
	 */
	private final int index, file;

	/**
	 * The buffer
	 */
	private ChannelBuffer buffer;

	/**
	 * Constructs a new file request
	 * @param priority Pritority over other files
	 * @param type The type
	 * @param file The file
	 */
	public FileResponse(boolean priority, int type, int file, ChannelBuffer buffer) {
		this.priority = priority;
		this.index = type;
		this.file = file;
		this.buffer = buffer;
	}

	/**
	 * Returns if priority
	 * @return The priority
	 */
	public boolean isPriority() {
		return priority;
	}

	/**
	 * Returns the type
	 * @return The type
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Returns the file
	 * @return The file
	 */
	public int getFile() {
		return file;
	}

	/**
	 * Returns the buffer
	 * @return The buffer
	 */
	public ChannelBuffer getContainer() {
		return buffer;
	}
}
