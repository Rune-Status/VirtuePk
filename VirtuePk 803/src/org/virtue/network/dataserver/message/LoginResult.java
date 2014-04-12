package org.virtue.network.dataserver.message;


/**
 * @author Taylor Moon
 * @date Jan 16, 2014
 */
public enum LoginResult {

	SUCCESS(2), ALREADY_ONLINE(5), BANNED(4), CLIENT_UPDATED(6), CURRENTLY_UPDATING(14), ERROR(13), INVALID_DETAILS(3), IP_BANNED(26), LOGIN_LIMIT_EXCEEDED(9), LOGIN_SERVER_OFFLINE(8), WORLD_FULL(7), MEMBERS_REQUIRED(12);

	/**
	 * Represents the result opcode.
	 */
	private int code;

	/**
	 * Constructs a new {@code LoginResult.java}.
	 * @param code The result opcode.
	 */
	private LoginResult(int code) {
		this.code = code;
	}

	/**
	 * @return the opcode
	 */
	public int getOpcode() {
		return code;
	}
	
	/**
	 * Returns the login result.
	 * @param opcode The opcode.
	 * @return The result.
	 */
	public static LoginResult forOpcode(int opcode) {
		for (LoginResult result : LoginResult.values()) {
			if (result.getOpcode() == opcode) {
				return result;
			}
		}
		return null;
	}
}
