package org.virtue.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * @author Taylor
 * @version 1.0
 */
public class RS2Utils {
	
	public static char[] aCharArray6385 = { '\u20ac', '\0', '\u201a', '\u0192',
		'\u201e', '\u2026', '\u2020', '\u2021', '\u02c6', '\u2030',
		'\u0160', '\u2039', '\u0152', '\0', '\u017d', '\0', '\0', '\u2018',
		'\u2019', '\u201c', '\u201d', '\u2022', '\u2013', '\u2014',
		'\u02dc', '\u2122', '\u0161', '\u203a', '\u0153', '\0', '\u017e',
		'\u0178' };
	
	public static char method2782(byte value) {
		int byteChar = 0xff & value;
		if (byteChar == 0)
			throw new IllegalArgumentException("Non cp1252 character 0x"
					+ Integer.toString(byteChar, 16) + " provided");
		if ((byteChar ^ 0xffffffff) <= -129 && byteChar < 160) {
			int i_4_ = aCharArray6385[-128 + byteChar];
			if ((i_4_ ^ 0xffffffff) == -1)
				i_4_ = 63;
			byteChar = i_4_;
		}
		return (char) byteChar;
	}
	
	/**
	 * Writes a string to the specified output stream.
	 * @param os The output stream.
	 * @param str The string.
	 * @throws IOException if an I/O error occurs.
	 */
	public static void writeString(OutputStream os, String str) throws IOException {
		for (char c : str.toCharArray()) {
			os.write(c);
		}
		os.write('\0');
	}

	/**
	 * Reads a string from the specified input stream.
	 * @param is The input stream.
	 * @return The string.
	 * @throws IOException if an I/O error occurs.
	 */
	public static String readString(InputStream is) throws IOException {
		StringBuilder builder = new StringBuilder();
		int character;
		while ((character = is.read()) != -1 && character != '\0') {
			builder.append((char) character);
		}
		return builder.toString();
	}
	
	public static int getHashMapSize(int size) {
		size--;
		size |= size >>> -1810941663;
		size |= size >>> 2010624802;
		size |= size >>> 10996420;
		size |= size >>> 491045480;
		size |= size >>> 1388313616;
		return 1 + size;
	}
}
