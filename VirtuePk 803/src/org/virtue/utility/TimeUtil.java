package org.virtue.utility;


/**
 * @author BElthazar
 */
public class TimeUtil {
	
	/**
	 * Converts milliseconds to seconds.
	 * @param milliseconds The milliseconds to convert.
	 * @return The seconds.
	 */
	public static long millisecondsToSeconds(long milliseconds) {
		return Math.round(milliseconds / 1000);
	}
	
	/**
	 * Converts milliseconds to minutes.
	 * @param milliseconds The milliseconds to convert.
	 * @return The minutes.
	 */
	public static long millisecondsToMinutes(long milliseconds) {
		return Math.round(millisecondsToSeconds(milliseconds) / 60);
	}
	
	/**
	 * Converts milliseconds to hours.
	 * @param milliseconds The milliseconds to convert.
	 * @return The hours.
	 */
	public static long millisecondsToHours(long milliseconds) {
		return Math.round(millisecondsToMinutes(milliseconds) / 60);
	}
	
	/**
	 * Converts milliseconds to days.
	 * @param milliseconds The milliseconds to convert.
	 * @return The days.
	 */
	public static long millisecondsToDays(long milliseconds) {
		return Math.round(millisecondsToHours(milliseconds) / 24);
	}
	
	private static long timeCorrection;
	private static long lastTimeUpdate;
	
	public static synchronized long currentTimeMillis() {
		long l = System.currentTimeMillis();
		if (l < lastTimeUpdate)
			timeCorrection += lastTimeUpdate - l;
		lastTimeUpdate = l;
		return l + timeCorrection;
	}
}
