package org.virtue.utility;

import java.text.DecimalFormat;

/**
 * @author Taylor
 * @version 1.0
 */
public class StringUtils {

	/**
	 * @author Taylor
	 * @version 1.0
	 */
	public enum FormatType {
		DISPLAY, PROTOCOL, NAME
	}
	
	/**
	 * The available valid characters
	 */
	public static final char[] VALID_CHARS = { '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	/**
	 * 
	 * @param position
	 * @param buffer
	 * @param string
	 * @return
	 */
	public static int packGJString2(int position, byte[] buffer, String string) {
        int length = string.length();
        int offset = position;
        for (int index = 0; length > index; index++) {
            int character = string.charAt(index);
            if (character > 127) {
                if (character > 2047) {
                    buffer[offset++] = (byte) ((character | 919275) >> 12);
                    buffer[offset++] = (byte) (128 | ((character >> 6) & 63));
                    buffer[offset++] = (byte) (128 | (character & 63));
                } else {
                    buffer[offset++] = (byte) ((character | 12309) >> 6);
                    buffer[offset++] = (byte) (128 | (character & 63));
                }
            } else
                buffer[offset++] = (byte) character;
        }
        return offset - position;
    }
	
	/**
	 * Formats a string for use of display
	 * @param string The string to format
	 * @return The formatted string
	 */
	public static String format(String string, FormatType type) {
		switch (type) {
		case DISPLAY:
			return formatDisplay(string);
		case PROTOCOL:
			return formatProtocol(string);
		case NAME:
			return formatName(string);
		default:
			return "";
		}
	}

	/**
	 * Formats a string for display
	 * @param string The string to format
	 * @return The formatted string
	 */
	@Deprecated
	public static String formatName(String string) {
		boolean afterSpace = false;
		string = string.replaceAll("_", " ");
		StringBuilder b = new StringBuilder(string.length());
		for (int i = 0; i < string.length(); i++) {
			if (i == 0 || afterSpace) {
				b.append(Character.toUpperCase(string.charAt(i)));
				afterSpace = false;
			} else
				b.append(string.charAt(i));
			if (string.charAt(i) == ' ')
				afterSpace = true;
		}
		return b.toString();
	}
	
	/**
	 * Formats a name for display
	 * @param string The string to format
	 * @return The formatted string
	 */
	@Deprecated
	public static String formatDisplay(String string) {
		boolean sentanceEnd = false;
		string = string.replaceAll("_", " ");
		StringBuilder b = new StringBuilder(string.length());
		for (int i = 0; i < string.length(); i++) {
			if (i == 0 || sentanceEnd) {
				b.append(Character.toUpperCase(string.charAt(i)));
				sentanceEnd = false;
			} else
				b.append(string.charAt(i));
			if (string.charAt(i) == '.' || string.charAt(i) == '?' || string.charAt(i) == '!')
				sentanceEnd = true;
		}
		return b.toString();
	}

	/**
	 * Formats a string for protocol
	 * @param string The string to format
	 * @return The formatted string
	 */
	@Deprecated
	public static String formatProtocol(String string) {
		return string.replaceAll(" ", "_").toLowerCase();
	}
	
	/**
	 * If a word needs an n at the end of it
	 * @param word The word to check
	 * @return True if so; false otherwise
	 */
	public static boolean isPlusN(String word) {
		if (word == null)
			return false;
		String s = word.toLowerCase();
		return s.charAt(0) == 'a' || s.charAt(0) == 'e' || s.charAt(0) == 'i' || s.charAt(0) == 'o' || s.charAt(0) == 'u' || (s.charAt(0) == 'h' && s.length() > 1 && s.charAt(1) != 'e');
	}
	
	/**
	 * Returns the typical number format for an integer
	 * @param amount The integer to format
	 * @return The formatted instance
	 */
	public static String getFormattedNumber(int amount) {
		return new DecimalFormat("#,###,##0").format(amount).toString();
	}
}
