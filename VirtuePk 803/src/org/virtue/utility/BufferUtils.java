package org.virtue.utility;

import java.nio.ByteBuffer;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author Taylor
 * @since Sep 14, 2013, Earth 1.0
 */
public class BufferUtils {
	
	/**
	 * Reads a string from a specified {@link ChannelBuffer}
	 * @param buffer The {@link ChannelBuffer} to read the string from
	 * @return The string read from the buffer
	 */
	public static String readString(ChannelBuffer in) {
		StringBuilder sb = new StringBuilder();
		byte b;
		while (in.readable() && (b = in.readByte()) != 0) {
			sb.append((char) b);
		}
		return sb.toString();
	}
	
	/**
	 * Reads a JAG-format string from a specified {@link ByteBuffer}
	 * @param buffer The {@link ByteBuffer} to read the string from
	 * @return The string read from the buffer
	 */
	public static String readJagString(ChannelBuffer in) {
		in.readByte();
		StringBuilder sb = new StringBuilder();
		byte b;
		while (in.readable() && (b = in.readByte()) != 0) {
			sb.append((char) b);
		}
		return sb.toString();
	}
	
	/**
	 * Reads a string from a specified {@link ByteBuffer}
	 * @param buffer The {@link ByteBuffer} to read the string from
	 * @return The string read from the buffer
	 */
	public static String readString(ByteBuffer in) {
		StringBuilder sb = new StringBuilder();
		byte b;
		while (in.hasRemaining() && (b = in.get()) != 0) {
			sb.append((char) b);
		}
		return sb.toString();
	}

	/**
	 * Writes a string to a specified {@link ChannelBuffer}
	 * @param s The string to write
	 * @param buffer The {@link ChannelBuffer} to write the string to
	 */
	public static void writeString(String s, ChannelBuffer buffer) {
		for (char c : s.toCharArray()) {
			buffer.writeByte(c);
		}
		buffer.writeByte(0);
	}

	/**
	 * @param buffer The buffer to write to.
	 * @param packet The packet ID to write.
	 */
	public static void createPacket(ChannelBuffer buffer, int packet) {
		if (packet >= 128) {
			buffer.writeByte((packet >> 8) + 128);
			buffer.writeByte(packet);
		} else
			buffer.writeByte(packet);
	}
	
	public static int read24bitInteger (ChannelBuffer buffer) {
		return (buffer.readUnsignedByte() << 16) + (buffer.readUnsignedByte() << 8)
				+ (buffer.readUnsignedByte());
	}

	/**
	 * @param buffer
	 * @return
	 */
	public static int readLargeSmart(ByteBuffer buffer) {
		if (buffer.get(buffer.position()) > 0) {
			int value = buffer.getShort() & 0xFFFF;
			if (value == 32767) {
				return -1;
			}
			return value;
		}
		return buffer.getInt() & 0x7fffffff;
	}
	
	/**
	 * Reads a Smart from the buffer.
	 * @param buffer The inbound buffer.
	 * @return The Smart read.
	 */
	public static int readSmart(ByteBuffer buffer) {
		int value = buffer.get() & 0xff;
		if (value < 128) {
			return value;
		}
		int value2 = buffer.get() & 0xff;
		return (value << 8 | value2) - 32768;
	}
}
