package org.virtue.network.loginserver.message;

/**
 * @author Taylor
 * @date Jan 14, 2014
 */
public enum WorldSubmissionResponse {

	/**
	 * Represents the response saying that the requested world already exists.
	 */
	EXISTING_WORLD,
	
	/**
	 * Represents the response saying that the access was denied.
	 */
	DENIED_ACCESS,

	/**
	 * Represents the response saying that the submission was successful and the
	 * world data is stored in memory.
	 */
	SUCCESSFUL_SUBMISSION;
}
