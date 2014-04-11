package org.virtue.utility;

import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;

/**
 * Represents a print stream that outputs messages in a specified fasion.
 * @author Taylor
 */
public class ConsoleLogger extends PrintStream {
	
	/**
	 * Represents the {@link Date} to display in the prinstream messages
	 */
	private static final Date TIME = Calendar.getInstance().getTime();

	/**
	 * Constructs a new {@code ConsoleLogger.java}.
	 * @param stream The {@link PrintStream} reference
	 */
	public ConsoleLogger(PrintStream stream) {
		super(stream);
	}

	@Override
	public void println(String message) {
		log(message);
	}
	
	@Override
	public PrintStream printf(String message, Object... objects) {return null;}
	
	@Override
	public void println(boolean message) {
		log(String.valueOf(message));
	}
	
	@Override
	public void println(int message) {
		log(String.valueOf(message));
	}
	
	@Override
	public void println(double message) {
		log(String.valueOf(message));
	}
	
	@Override
	public void println(char message) {}
	
	@Override
	public void println(long message) {}
	
	/**
	 * Method used to log the message.
	 * @param message the message.
	 */
	public void log(final String message) {
		if (message == null || message.length() == 0 || message.contains("INFO") || message.contains("Launching OSBot...") || message.contains("WORKING") || message.contains("//") || message.contains("OSBot") || message.contains("Copyright") || message.contains("Running") || message.contains("Java version") || message.contains("[DEBUG]") || message.contains("[ERROR]")) {
			return;
		}
		super.println(getDisplay() + message);
	}
	
	/**
	 * Gets the display details.
	 * @return the string.
	 */
	public String getDisplay() {
		return "[Virtue Logger]: ";
	}
}
